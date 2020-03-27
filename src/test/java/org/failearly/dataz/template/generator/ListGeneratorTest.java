/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.template.generator;

import org.failearly.dataz.common.test.ExceptionVerifier;
import org.failearly.dataz.common.test.annotations.Subject;
import org.failearly.dataz.internal.template.generator.ListGeneratorFactory;
import org.failearly.dataz.internal.template.generator.ListGeneratorFactory.ListGeneratorImpl;
import org.failearly.dataz.template.generator.support.InternalIteratorExhaustedException;
import org.failearly.dataz.template.generator.support.test.LimitedGeneratorTestBase;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * ListGeneratorTest contains tests for {@link ListGenerator}.
 */
@Subject({ListGenerator.class, ListGeneratorFactory.class, ListGeneratorImpl.class})
public class ListGeneratorTest extends LimitedGeneratorTestBase<String, ListGenerator, ListGeneratorFactory, ListGeneratorImpl> {
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
