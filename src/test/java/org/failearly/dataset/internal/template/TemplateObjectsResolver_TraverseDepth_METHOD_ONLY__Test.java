/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.internal.template;

import org.failearly.dataset.internal.annotation.TraverseDepth;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Tests for {@link TemplateObjectsResolver#resolveFromTestMethod0(Method)} and {@link TemplateObjectsResolver#resolveFromTestClass0(Class)} and
 * {@link TraverseDepth#METHOD_ONLY}.
 */
public class TemplateObjectsResolver_TraverseDepth_METHOD_ONLY__Test extends TemplateObjectsTestBase {

    private TemplateObjectsResolver buildTemplateObjectsResolver() {
        return buildTemplateObjectsResolver(TraverseDepth.METHOD_ONLY);
    }

    @Test
    public void on_method_without_template_objects__should_empty_template_objects() throws Exception {
        // arrange / given
        final TemplateObjectsResolver templateObjectsResolver = buildTemplateObjectsResolver();

        // act / when
        final TemplateObjects templateObjects = templateObjectsResolver.resolveFromTestMethod0(withoutTemplateObjects());

        // assert / then
        assertEmptyTemplateObjects(templateObjects);
    }

    @Test
    public void on_method_with_template_objects__should_resolve_method_template_objects() throws Exception {
        // arrange / given
        final TemplateObjectsResolver templateObjectsResolver = buildTemplateObjectsResolver();

        // act / when
        final TemplateObjects templateObjects = templateObjectsResolver.resolveFromTestMethod0(withTemplateObjects());

        // assert / then
        assertTemplateObjects(templateObjects,
                "@org.failearly.dataset.test.MyTemplateObjectAnnotation(description=(3) On method withTemplateObjects, dataset=D3, name=G2)",
                "@org.failearly.dataset.test.MyTemplateObjectAnnotation(description=(4) On method withTemplateObjects, dataset=D3, name=G3)"
        );
    }

    @Test
    public void on_class__should_resolve_empty_template_objects() throws Exception {
        // arrange / given
        final TemplateObjectsResolver templateObjectsResolver = buildTemplateObjectsResolver();

        // act / when
        final TemplateObjects templateObjects = templateObjectsResolver.resolveFromTestClass0(AClass.class);

        // assert / then
        assertEmptyTemplateObjects(templateObjects);
    }


}