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

package org.failearly.dataz.template.generator;

import org.failearly.common.test.ExceptionVerifier;
import org.failearly.common.test.annotations.Subject;
import org.failearly.dataz.internal.template.generator.ListGeneratorFactory;
import org.failearly.dataz.internal.template.generator.ListGeneratorFactory.ListGeneratorImpl;
import org.failearly.dataz.template.generator.support.InternalIteratorExhaustedException;
import org.failearly.dataz.template.generator.support.test.GeneratorTestBase;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * ListGeneratorTest contains tests for {@link ListGenerator}.
 */
@Subject({ListGenerator.class, ListGeneratorFactory.class, ListGeneratorImpl.class})
public class ListGeneratorTest extends GeneratorTestBase<String, ListGenerator, ListGeneratorFactory, ListGeneratorImpl> {
    private static final int LIST_WITH_VALUES=0;
    private static final int EMPTY_LIST=1;

    public ListGeneratorTest() {
        super(ListGenerator.class, ListGeneratorFactory.class, ListGeneratorImpl.class, TestFixture.class);
    }

    @Test
    public void external_iterator__should_generate_all_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createTemplateObjectFromAnnotation(LIST_WITH_VALUES)
        );

        // assert / then
        assertThat(generated, is("A;B;C;"));
    }

    @Test
    public void internal_iterator__should_generate_all_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_INTERNAL_ITERATOR, 3),
                createTemplateObjectFromAnnotation(LIST_WITH_VALUES)
        );

        // assert / then
        assertThat(generated, is(
            "(1) next=A,last=A/" +
                "(2) next=B,last=B/" +
                "(3) next=C,last=C/"
        ));
    }

    @Test
    public void internal_iterator_on_empty_list__should_throw_exception() throws Exception {
        // assert / then
        ExceptionVerifier.on(                                   //
                () -> generate(                                 //
                        template(TEMPLATE_INTERNAL_ITERATOR),   //
                        createTemplateObjectFromAnnotation(EMPTY_LIST)             //
                )                                               //
        )                                                       //
                .expectRootCause(InternalIteratorExhaustedException.class)
                .expectRootCause("Internal iterator of generator TO is exhausted!")
                .verify();
    }

    @Test
    public void external_iterator_on_empty_list__should_generate_empty_string() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createTemplateObjectFromAnnotation(EMPTY_LIST)
        );

        // assert / then
        assertThat(generated, is(""));
    }


    @ListGenerator(name= DTON, values={"A", "B", "C"})
    @ListGenerator(name= DTON, values={})
    private static class TestFixture {
    }

}