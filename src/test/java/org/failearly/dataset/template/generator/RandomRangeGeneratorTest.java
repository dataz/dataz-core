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

import org.failearly.dataset.internal.template.generator.RandomRangeGeneratorFactory;
import org.failearly.dataset.template.InvariantViolationException;
import org.failearly.dataset.template.generator.support.UnlimitedGenerator;
import org.failearly.dataset.template.generator.support.test.GeneratorTestBase;
import org.failearly.common.test.ExceptionVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 *  RandomRangeGeneratorTest contains tests for {@link RandomRangeGenerator}.
 */
public class RandomRangeGeneratorTest extends GeneratorTestBase<Integer, RandomRangeGenerator, RandomRangeGeneratorFactory> {

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
        super(RandomRangeGenerator.class, RandomRangeGeneratorFactory.class, TestFixture.class);
    }

    @Test
    public void default_random_range__should_be_unlimited_generator() throws Exception {
        assertThat(createGenerator(DEFAULT_RANDOM_RANGE), instanceOf(UnlimitedGenerator.class));
    }

    @Test
    public void unlimited_random_range__should_create_a_stream_of_none_unique_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_INTERNAL_ITERATOR_SIMPLE, 6),
                createGenerator(SIMPLE_RANGE_WITH_SEED_42)
        );

        // assert / then
        assertThat(generated, is("8,3,8,3,5,10,"));
    }

    @Test
    public void different_seed__should_create_values_in_different_order() throws Exception {
        // act / when
        final String generated314=generate(
                template(TEMPLATE_INTERNAL_ITERATOR_SIMPLE, 6),
                createGenerator(SIMPLE_RANGE_WITH_SEED_314)
        );
        final String generated42=generate(
                template(TEMPLATE_INTERNAL_ITERATOR_SIMPLE, 6),
                createGenerator(SIMPLE_RANGE_WITH_SEED_42)
        );

        // assert / then
        assertThat(generated314, not(generated42));
    }

    @Test
    public void limited_random_range_with_default_count__should_generate_range_size_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createTemplateObjectFromAnnotationIndex(LIMITED_RANGE_WITH_DEFAULT_COUNT)
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
                createGenerator(LIMITED_RANGE_WITH_VALID_COUNT)
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
                createGenerator(UNIQUE_RANGE_WITH_DEFAULT_COUNT)
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
                createGenerator(UNIQUE_RANGE_WITH_COUNT_LESS_THEN_RANGE_SIZE)
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
                createGenerator(UNIQUE_RANGE_WITH_COUNT_GREATER_THEN_RANGE_SIZE)
        );

        // assert / then
        assertThat(generated, is(
            "8;3;5;10;6;9;4;7;"
        ));
    }

    @Test
    public void invalid_range__should_throw_InvariantViolationException() throws Exception {
        // act / when
        ExceptionVerifier.on(() -> createGenerator(INVALID_START_EQ_END))
                .expect(InvariantViolationException.class)
                .expect("Invariant of RandomRangeGenerator has been violated: start < end!" +
                        "\nCurrent annotation is '" + getDeclaredAnnotation(INVALID_START_EQ_END) + "'" )
                .verify();

        ExceptionVerifier.on(() -> createGenerator(INVALID_START_GT_END))
                .expect(InvariantViolationException.class)
                .expect("Invariant of RandomRangeGenerator has been violated: start < end!" +
                        "\nCurrent annotation is '" + getDeclaredAnnotation(INVALID_START_GT_END)+ "'" )
                .verify();
    }

    // Unlimited
    @RandomRangeGenerator(name=TEMPLATE_OBJECT_NAME)
    @RandomRangeGenerator(name=TEMPLATE_OBJECT_NAME, start=3, end=10, seed=42)
    @RandomRangeGenerator(name=TEMPLATE_OBJECT_NAME, start=3, end=10, seed=314)
    // Limited
    @RandomRangeGenerator(name=TEMPLATE_OBJECT_NAME, start=3, end=10, seed=42, limit = Limit.LIMITED)
    @RandomRangeGenerator(name=TEMPLATE_OBJECT_NAME, start=3, end=10, seed=42, limit = Limit.LIMITED, count = 10)
    // Uniqiue
    @RandomRangeGenerator(name=TEMPLATE_OBJECT_NAME, start=3, end=10, seed=42, unique = true)
    @RandomRangeGenerator(name=TEMPLATE_OBJECT_NAME, start=3, end=10, seed=42, unique = true, count = 4)
    @RandomRangeGenerator(name=TEMPLATE_OBJECT_NAME, start=3, end=10, seed=42, unique = true, count = 1_000_000)
    // Invalid
    @RandomRangeGenerator(name=TEMPLATE_OBJECT_NAME, start=1, end=1, seed=42)
    @RandomRangeGenerator(name=TEMPLATE_OBJECT_NAME, start=3, end=0, seed=42)
    private static class TestFixture {
    }
}