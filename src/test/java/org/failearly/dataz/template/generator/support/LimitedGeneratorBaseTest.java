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