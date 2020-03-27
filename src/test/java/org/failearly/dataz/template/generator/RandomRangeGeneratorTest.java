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
import org.failearly.dataz.internal.template.generator.RandomRangeGeneratorFactory;
import org.failearly.dataz.internal.template.generator.RandomRangeGeneratorFactory.RandomRangeGeneratorImpl;
import org.failearly.dataz.internal.template.generator.decorator.LimitedGeneratorDecorator;
import org.failearly.dataz.internal.template.generator.decorator.UniqueGeneratorDecorator;
import org.failearly.dataz.template.InvariantViolationException;
import org.failearly.dataz.template.generator.support.UnlimitedGenerator;
import org.failearly.dataz.template.generator.support.test.UnlimitedGeneratorTestBase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 *  RandomRangeGeneratorTest contains tests for {@link RandomRangeGenerator}.
 */
public class RandomRangeGeneratorTest extends UnlimitedGeneratorTestBase<Integer, RandomRangeGenerator, RandomRangeGeneratorFactory, RandomRangeGeneratorImpl> {

    private static final int DEFAULT_RANDOM_RANGE=0;
    private static final int SIMPLE_RANGE_WITH_SEED_42=1;
    private static final int SIMPLE_RANGE_WITH_SEED_314=2;
    private static final int LIMITED_RANGE_WITH_DEFAULT_COUNT=3;
    private static final int LIMITED_RANGE_WITH_VALID_COUNT=4;
    private static final int UNIQUE_RANGE_WITH_DEFAULT_COUNT=5;
    private static final int UNIQUE_RANGE_WITH_COUNT_LESS_THEN_RANGE_SIZE=6;
    private static final int UNIQUE_RANGE_WITH_COUNT_GREATER_THEN_RANGE_SIZE=7;
    private static final int INVALID_START_EQ_END=8;
    private static final int INVALID_START_GT_END=9;


    public RandomRangeGeneratorTest() {
        super(RandomRangeGenerator.class, RandomRangeGeneratorFactory.class, RandomRangeGeneratorImpl.class, TestFixture.class);
    }

    @Test
    public void default_random_range__should_be_unlimited_generator() throws Exception {
        assertThat(super.createTemplateObjectFromAnnotation(DEFAULT_RANDOM_RANGE), instanceOf(UnlimitedGenerator.class));
    }

    @Test
    public void unlimited_random_range__should_create_a_stream_of_none_unique_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_INTERNAL_ITERATOR_SIMPLE, 6),
                super.createTemplateObjectFromAnnotation(SIMPLE_RANGE_WITH_SEED_42)
        );

        // assert / then
        assertThat(generated, is("8,3,8,3,5,10,"));
    }

    @Test
    public void different_seed__should_create_values_in_different_order() throws Exception {
        // act / when
        final String generated314=generate(
                template(TEMPLATE_INTERNAL_ITERATOR_SIMPLE, 6),
                super.createTemplateObjectFromAnnotation(SIMPLE_RANGE_WITH_SEED_314)
        );
        final String generated42=generate(
                template(TEMPLATE_INTERNAL_ITERATOR_SIMPLE, 6),
                super.createTemplateObjectFromAnnotation(SIMPLE_RANGE_WITH_SEED_42)
        );

        // assert / then
        assertThat(generated314, not(generated42));
    }

    @Test
    public void limited_random_range_with_default_count__should_generate_range_size_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createTemplateObjectFromAnnotation(LIMITED_RANGE_WITH_DEFAULT_COUNT, LimitedGeneratorDecorator.class)
        );

        // assert / then
        assertThat(generated, is(
            "8;3;8;3;5;10;5;8;"
        ));
    }

    @Test
    public void limited_random_range_with_valid_count__should_generate_count_none_unique_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                super.createTemplateObjectFromAnnotation(LIMITED_RANGE_WITH_VALID_COUNT, LimitedGeneratorDecorator.class)
        );

        // assert / then
        assertThat(generated, is(
            "8;3;8;3;5;10;5;8;8;3;"
        ));
    }

    @Test
    public void unique_random_range_with_default_count__should_generate_all_values_in_range__but_in_random_order() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                super.createTemplateObjectFromAnnotation(UNIQUE_RANGE_WITH_DEFAULT_COUNT, UniqueGeneratorDecorator.class)
        );

        // assert / then
        assertThat(generated, is(
            "8;3;5;10;6;9;4;7;"
        ));
    }

    @Test
    public void unique_random_range_with_count_less_then_range_size__should_only_generate_count_unique_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createTemplateObjectFromAnnotation(UNIQUE_RANGE_WITH_COUNT_LESS_THEN_RANGE_SIZE, UniqueGeneratorDecorator.class)
        );

        // assert / then
        assertThat(generated, is(
            "8;3;5;10;"
        ));
    }

    @Test
    public void unique_random_range_with_count_greater_then_range_size__should_ignore_count() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                super.createTemplateObjectFromAnnotation(UNIQUE_RANGE_WITH_COUNT_GREATER_THEN_RANGE_SIZE, UniqueGeneratorDecorator.class)
        );

        // assert / then
        assertThat(generated, is(
            "8;3;5;10;6;9;4;7;"
        ));
    }

    @Test
    public void invalid_range__should_throw_InvariantViolationException() throws Exception {
        // act / when
        ExceptionVerifier.on(() -> super.createTemplateObjectFromAnnotation(INVALID_START_EQ_END))
                .expect(InvariantViolationException.class)
                .expect("Invariant of RandomRangeGenerator has been violated: start < end!" +
                        "\nCurrent impl is '" + resolveTestFixtureAnnotation(INVALID_START_EQ_END) + "'" )
                .verify();

        ExceptionVerifier.on(() -> super.createTemplateObjectFromAnnotation(INVALID_START_GT_END))
                .expect(InvariantViolationException.class)
                .expect("Invariant of RandomRangeGenerator has been violated: start < end!" +
                        "\nCurrent impl is '" + resolveTestFixtureAnnotation(INVALID_START_GT_END)+ "'" )
                .verify();
    }

    // Unlimited
    @RandomRangeGenerator(name= DTON)
    @RandomRangeGenerator(name= DTON, start=3, end=10, seed=42)
    @RandomRangeGenerator(name= DTON, start=3, end=10, seed=314)
    // Limited
    @RandomRangeGenerator(name= DTON, start=3, end=10, seed=42, limit = Limit.LIMITED)
    @RandomRangeGenerator(name= DTON, start=3, end=10, seed=42, limit = Limit.LIMITED, count = 10)
    // Uniqiue
    @RandomRangeGenerator(name= DTON, start=3, end=10, seed=42, unique = true)
    @RandomRangeGenerator(name= DTON, start=3, end=10, seed=42, unique = true, count = 4)
    @RandomRangeGenerator(name= DTON, start=3, end=10, seed=42, unique = true, count = 1_000_000)
    // Invalid
    @RandomRangeGenerator(name= DTON, start=1, end=1, seed=42)
    @RandomRangeGenerator(name= DTON, start=3, end=0, seed=42)
    private static class TestFixture {
    }
}