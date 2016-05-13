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

package org.failearly.dataz.template.generator.support;

import org.failearly.dataz.internal.template.generator.ListGeneratorFactory;
import org.failearly.dataz.internal.template.generator.ListGeneratorFactory.ListGeneratorImpl;
import org.failearly.dataz.template.generator.Limit;
import org.failearly.dataz.template.generator.ListGenerator;
import org.failearly.dataz.template.generator.support.test.GeneratorTestBase;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * LimitedGeneratorBaseTest contains tests for {@link LimitedGeneratorBase} using {@link ListGenerator}.
 */
public class LimitedGeneratorBaseTest extends GeneratorTestBase<String, ListGenerator, ListGeneratorFactory, ListGeneratorImpl> {

    public LimitedGeneratorBaseTest() {
        super(ListGenerator.class, ListGeneratorFactory.class, ListGeneratorImpl.class, TestFixture.class);
    }

    @Test
    public void external_iterator__should_create_none_empty_content() throws Exception {
        // act / when
        final String generated = generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createTemplateObjectFromAnnotation()
        );

        // assert / then
        assertThat(generated, is("val1;val2;val3;"));
    }

    @Test
    public void two_or_more_external_iterators__should_be_independent() throws Exception {
        // act / when
        final String generated = generate(
                template(TEMPLATE_EXTERNAL_TWO_ITERATORS),
                createTemplateObjectFromAnnotation()
        );

        // assert / then
        assertThat(generated, is("val1={val1,val2,val3,};val2={val1,val2,val3,};val3={val1,val2,val3,};"));
    }

    @Test
    public void external_and_the_internal_iterator__should_be_independent() throws Exception {
        // act / when
        final String generated = generate(
                template(TEMPLATE_EXTERNAL_AND_INTERNAL_ITERATOR),
                createTemplateObjectFromAnnotation()
        );

        // assert / then
        assertThat(generated, is("Internal iterator value is val1. External iterator=(ext=val1,int=val1)/(ext=val2,int=val1)/(ext=val3,int=val1)/"));
    }

    @Test
    public void using_internal_iterator_with_max_limit__should_generate_all_values() throws Exception {
        // act / when
        final String generated = generate(
                template(TEMPLATE_INTERNAL_ITERATOR, 3),
                createTemplateObjectFromAnnotation()
        );

        // assert / then
        assertThat(generated, is(
            "(1) next=val1,last=val1/(2) next=val2,last=val2/(3) next=val3,last=val3/"));
    }

    @Test
    public void reseting_internal_iterator__should_repeat_the_same_value() throws Exception {
        // act / when
        final String generated = generate(
                template(TEMPLATE_INTERNAL_ITERATOR_USING_RESET, 4),
                createTemplateObjectFromAnnotation()
        );

        // assert / then
        assertThat(generated, is(
            "(1) next=val1/" +
                "(2) next=val1/" +
                "(3) next=val1/" +
                "(4) next=val1/"
        ));
    }


    @ListGenerator(name = DTON, values = {"val1", "val2", "val3"}, limit = Limit.LIMITED)
    private static class TestFixture {
    }

}