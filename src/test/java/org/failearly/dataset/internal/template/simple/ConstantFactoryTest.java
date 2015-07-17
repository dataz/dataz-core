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

package org.failearly.dataset.internal.template.simple;

import org.failearly.dataset.template.common.Scope;
import org.failearly.dataset.template.common.TemplateObject;
import org.failearly.dataset.template.simple.Constant;
import org.failearly.dataset.template.support.test.AnnotationHelper;
import org.junit.Before;
import org.junit.Test;

import static org.failearly.dataset.test.TemplateObjectMatchers.isTemplateObjectAttributes;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * ConstantFactoryTest contains tests for {@link ConstantFactory} and {@link Constant}.
 */
public class ConstantFactoryTest {

    private static final String DATASET = "DS";
    private static final String NAME = "TO-NAME";

    private static final String VALUE = "Any constant value";

    private AnnotationHelper<Constant> annotationHelper;

    @Before
    public void setUp() throws Exception {
        annotationHelper = AnnotationHelper.createAnnotationHelper(Constant.class)
                .withFixtureClass(TestFixture.class);
    }

    @Test
    public void should_set_standard_element_values() throws Exception {
        // arrange / given
        final TemplateObject templateObject = createTemplateObjectByUsingConstantFactory();

        // assert / then
        assertThat("Standard element attributes?", templateObject, isTemplateObjectAttributes(NAME, DATASET, Scope.GLOBAL));
    }

    @Test
    public void should_set_none_standard_attributes() throws Exception {
        // arrange / given
        final ConstantFactory.ConstantImpl templateObject = createTemplateObjectByUsingConstantFactory();

        // assert / then
        assertThat("attribute value?", templateObject.getValue(), is(VALUE));
    }

    @Test
    public void toString__should_have_same_result_like_getValue() throws Exception {
        // arrange / given
        final ConstantFactory.ConstantImpl templateObject = createTemplateObjectByUsingConstantFactory();

        // assert / then
        assertThat("toString()==getStringValue()?", templateObject.toString(), is(templateObject.getValue()));
    }

    private ConstantFactory.ConstantImpl createTemplateObjectByUsingConstantFactory() {
        return (ConstantFactory.ConstantImpl)new ConstantFactory()
                    .create(annotationHelper.getAnnotation(0));
    }

    @Constant(name = NAME, dataset = DATASET, value = VALUE, scope = Scope.GLOBAL)
    private static class TestFixture {}
}