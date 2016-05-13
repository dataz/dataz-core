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

package org.failearly.dataset.template.simple;

import org.failearly.common.test.annotations.Subject;
import org.failearly.dataset.internal.template.simple.ConstantFactory;
import org.failearly.dataset.internal.template.simple.ConstantFactory.ConstantImpl;
import org.failearly.dataset.template.support.test.TemplateObjectTestBase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * ConstantTest contains tests for {@link ConstantFactory} and {@link Constant}.
 */
@Subject({Constant.class, ConstantFactory.class, ConstantImpl.class})
public class ConstantTest extends TemplateObjectTestBase<Constant, ConstantFactory, ConstantImpl> {

    private static final int ANY_CONSTANT=0;
    private static final int OTHER_CONSTANT=1;

    private static final String ANY_CONSTANT_VALUE="Any constant value";
    private static final String OTHER_CONSTANT_VALUE="Different value";

    public ConstantTest() {
        super(Constant.class, ConstantFactory.class, ConstantImpl.class, TestFixture.class);
    }

    @Test
    public void using_a_constant_annotation__should_return_the_value_of_the_annotation() throws Exception {
        // arrange / given
        final ConstantImpl constant=createConstant(ANY_CONSTANT);

        // assert / then
        assertThat("attribute value?", constant.getValue(), is(ANY_CONSTANT_VALUE));
    }

    @Test
    public void using_a_different_constant_annotation__should_return_other_value_of_the_annotation() throws Exception {
        // arrange / given
        final ConstantFactory.ConstantImpl constant=createConstant(OTHER_CONSTANT);

        // assert / then
        assertThat("attribute value?", constant.getValue(), is(OTHER_CONSTANT_VALUE));
    }

    @Test
    public void toString__should_have_same_result_like_getValue() throws Exception {
        // arrange / given
        final ConstantFactory.ConstantImpl constant=createConstant(ANY_CONSTANT);

        // assert / then
        assertThat("toString()==getStringValue()?", constant.toString(), is(constant.getValue()));
    }

    @Test
    public void using_template__should_return_value() throws Exception {
        // act / when
        final String generated=generate(
            template("method='%ton%.getValue()';property='%ton%.value';object='%ton%'"),
            createConstant(ANY_CONSTANT)
        );

        // assert / then
        assertThat(generated, is(
            "method='Any constant value';" +
                "property='Any constant value';" +
                "object='Any constant value'"
        ));
    }

    private ConstantImpl createConstant(int annotationNumber) throws Exception {
        return createTemplateObjectFromAnnotation(annotationNumber);
    }

    @Constant(name= DTON, value=ANY_CONSTANT_VALUE)
    @Constant(name= DTON, value=OTHER_CONSTANT_VALUE)
    private static class TestFixture {
    }
}