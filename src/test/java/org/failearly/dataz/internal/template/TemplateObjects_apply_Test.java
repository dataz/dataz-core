/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.failearly.dataz.internal.template;

import org.failearly.dataz.template.TemplateObject;
import org.failearly.common.test.ExceptionVerifier;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link TemplateObjects#apply(Consumer)} and it's behaviour with the different {@link TemplateObjectDuplicateStrategy}.
 */
public class TemplateObjects_apply_Test extends TemplateObjectsTestBase {

    private final List<String> duplicated = new LinkedList<>();
    private final List<String> accepted = new LinkedList<>();

    private DuplicateHandler collectDuplicated() {
        return templateObject -> duplicated.add(templateObject.toString());
    }

    private Consumer<TemplateObject> collectAccepted() {
        return templateObject -> accepted.add(templateObject.toString());
    }

    @Test
    public void strategy_ignore__should_accept_first_but_ignore_last_template_object() throws Exception {
        // arrange / given
        final TemplateObjectsResolver templateObjectsResolver = buildTemplateObjectResolver(TemplateObjectDuplicateStrategy.IGNORE, collectDuplicated());
        final TemplateObjects templateObjects = templateObjectsResolver.resolveFromTestMethod0(withDuplicatedTemplateObjects());

        // act / when
        templateObjects.apply(collectAccepted());

        // assert / then
        assertThat(duplicated, containsInAnyOrder(
                "@org.failearly.dataz.test.MyTemplateObjectAnnotation(description=(6) On method withDuplicatedTemplateObjects, dataz=OTHER-DATASET, name=G0)",
                "@org.failearly.dataz.test.MyTemplateObjectAnnotation(description=(7) On method withDuplicatedTemplateObjects. OVERWRITE G0, dataz=SAME-DATASET, name=G0)"
        ));
        assertThat(accepted, containsInAnyOrder(
                "@org.failearly.dataz.test.MyTemplateObjectAnnotation(description=(5) On method withDuplicatedTemplateObjects, dataz=SAME-DATASET, name=G0)"
        ));
    }

    @Test
    public void strategy_overwrite__should_ignore_first_but_accept_last_template_object() throws Exception {
        // arrange / given
        final TemplateObjectsResolver templateObjectsResolver = buildTemplateObjectResolver(TemplateObjectDuplicateStrategy.OVERWRITE, collectDuplicated());
        final TemplateObjects templateObjects = templateObjectsResolver.resolveFromTestMethod0(withDuplicatedTemplateObjects());

        // act / when
        templateObjects.apply(collectAccepted());

        // assert / then
        assertThat(duplicated, containsInAnyOrder(
                "@org.failearly.dataz.test.MyTemplateObjectAnnotation(description=(6) On method withDuplicatedTemplateObjects, dataz=OTHER-DATASET, name=G0)",
                "@org.failearly.dataz.test.MyTemplateObjectAnnotation(description=(5) On method withDuplicatedTemplateObjects, dataz=SAME-DATASET, name=G0)"
        ));
        assertThat(accepted, containsInAnyOrder(
                "@org.failearly.dataz.test.MyTemplateObjectAnnotation(description=(7) On method withDuplicatedTemplateObjects. OVERWRITE G0, dataz=SAME-DATASET, name=G0)"
        ));
    }

    @Test
    public void strategy_strict__should_throw_exception_on_first_duplicated() throws Exception {
        // arrange / given
        final TemplateObjectsResolver templateObjectsResolver = buildTemplateObjectResolverNoDuplicateHandler(TemplateObjectDuplicateStrategy.STRICT);
        final TemplateObjects templateObjects = templateObjectsResolver.resolveFromTestMethod0(withDuplicatedTemplateObjects());

        // assert / then
        ExceptionVerifier.TestAction action=() -> templateObjects.apply(collectAccepted());
        ExceptionVerifier.on(action).expect(DuplicateTemplateObjectException.class).expect("Duplicate template object found: @org.failearly.dataz.test.MyTemplateObjectAnnotation(description=(6) On method withDuplicatedTemplateObjects, dataz=OTHER-DATASET, name=G0)").verify();
    }


    @Test
    public void on_filtered_and_strategy_ignore__should_accept_first_but_ignore_last_template_object() throws Exception {
        // arrange / given
        final TemplateObjectsResolver templateObjectsResolver = buildTemplateObjectResolver(TemplateObjectDuplicateStrategy.IGNORE, collectDuplicated());

        // act / when
        final TemplateObjects templateObjects = templateObjectsResolver                               //
                                .resolveFromTestMethod0(withDuplicatedTemplateObjects())        //
                                .filterByDataSet("SAME-DATASET");
        templateObjects.apply(collectAccepted());

        // assert / then
        assertThat(duplicated, containsInAnyOrder(
                "@org.failearly.dataz.test.MyTemplateObjectAnnotation(description=(7) On method withDuplicatedTemplateObjects. OVERWRITE G0, dataz=SAME-DATASET, name=G0)"
        ));
        assertThat(accepted, containsInAnyOrder(
                "@org.failearly.dataz.test.MyTemplateObjectAnnotation(description=(5) On method withDuplicatedTemplateObjects, dataz=SAME-DATASET, name=G0)"
        ));
    }

}