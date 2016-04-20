/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */

package org.failearly.dataset.template.generator;

import org.failearly.dataset.internal.template.generator.ListGeneratorFactory;
import org.failearly.dataset.template.generator.support.InternalIteratorExhaustedException;
import org.failearly.dataset.template.generator.support.test.GeneratorTestBase;
import org.failearly.common.test.ExceptionVerifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * ListGeneratorTest contains tests for {@link ListGenerator}.
 */
public class ListGeneratorTest extends GeneratorTestBase<String, ListGenerator, ListGeneratorFactory> {
    private static final int LIST_WITH_VALUES=0;
    private static final int EMPTY_LIST=1;

    public ListGeneratorTest() {
        super(ListGenerator.class, ListGeneratorFactory.class, TestFixture.class);
    }

    @Test
    public void external_iterator__should_generate_all_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createGenerator(LIST_WITH_VALUES)
        );

        // assert / then
        assertThat(generated, is("A;B;C;"));
    }

    @Test
    public void internal_iterator__should_generate_all_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_INTERNAL_ITERATOR, 3),
                createGenerator(LIST_WITH_VALUES)
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
                        createGenerator(EMPTY_LIST)             //
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
                createGenerator(EMPTY_LIST)
        );

        // assert / then
        assertThat(generated, is(""));
    }


    @ListGenerator(name=TEMPLATE_OBJECT_NAME, values={"A", "B", "C"})
    @ListGenerator(name=TEMPLATE_OBJECT_NAME, values={})
    private static class TestFixture {
    }

}
