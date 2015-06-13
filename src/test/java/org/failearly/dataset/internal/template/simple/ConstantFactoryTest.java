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

import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.simple.Constant;
import org.failearly.dataset.test.AnnotationHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class ConstantFactoryTest {

    private static final String VALUE = "Any constant value";
    private static final String DATASET = "DS";
    private static final String NAME = "TO-NAME";

    private AnnotationHelper<Constant> constantAnnotationHelper;

    @Before
    public void setUp() throws Exception {
        constantAnnotationHelper = AnnotationHelper.createAnnotationHelper(Constant.class)
                .withFixtureClass(TestFixture.class);
    }

    @Test
    public void created_template_object__should_set_all_element_values_of_the_annotation() throws Exception {
        // arrange / given
        final ConstantFactory.ConstantImpl templateObject = createTemplateObjectByUsingConstantFactory();

        // assert / then
        Assert.assertThat("Data Set?", templateObject.dataset(), is(DATASET));
        Assert.assertThat("Name?", templateObject.name(), is(NAME));
        Assert.assertThat("Scope?", templateObject.scope(), is(Scope.GLOBAL));
        Assert.assertThat("Value?", templateObject.getValue(), is(VALUE));
    }

    @Test
    public void toString__should_have_same_result_like_getValue() throws Exception {
        // arrange / given
        final ConstantFactory.ConstantImpl templateObject = createTemplateObjectByUsingConstantFactory();

        // assert / then
        Assert.assertThat("toString()==getValue()?", templateObject.toString(), is(templateObject.getValue()));
    }

    private ConstantFactory.ConstantImpl createTemplateObjectByUsingConstantFactory() {
        return (ConstantFactory.ConstantImpl)new ConstantFactory()
                    .create(constantAnnotationHelper.getAnnotation(0));
    }

    @Constant(name = NAME, dataset = DATASET, value = VALUE, scope = Scope.GLOBAL)
    private static class TestFixture {
    }
}