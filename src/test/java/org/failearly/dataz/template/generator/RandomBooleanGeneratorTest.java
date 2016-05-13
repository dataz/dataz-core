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

import org.failearly.common.test.annotations.Subject;
import org.failearly.dataz.internal.template.generator.RandomBooleanGeneratorFactory;
import org.failearly.dataz.internal.template.generator.RandomBooleanGeneratorFactory.RandomBooleanGeneratorImpl;
import org.failearly.dataz.template.generator.support.test.GeneratorTestBase;
import org.junit.Test;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * RandomBooleanGeneratorFactoryTest contains tests for RandomBooleanGeneratorFactory
 */
@Subject({RandomBooleanGenerator.class, RandomBooleanGeneratorFactory.class, RandomBooleanGeneratorImpl.class})
public class RandomBooleanGeneratorTest extends GeneratorTestBase<Boolean, RandomBooleanGenerator, RandomBooleanGeneratorFactory, RandomBooleanGeneratorImpl> {

    private static final int TWENTY_PERCENT_WITH_SEED_42=0;
    private static final int SEVENTY_PERCENT_WITH_SEED_1=1;

    public RandomBooleanGeneratorTest() {
        super(RandomBooleanGenerator.class, RandomBooleanGeneratorFactory.class, RandomBooleanGeneratorImpl.class, TestFixture.class);
    }

    private static double percentageTrues(Generator<Boolean> generator, int numIterations) {
        int numberOfTrues=0;

        // act / when
        for (int i=0; i < numIterations; i++) {
            if (generator.next()) {
                numberOfTrues++;
            }
        }
        return (numberOfTrues * 100d) / (double) numIterations;
    }

    private void assertNumberOfGeneratedTrues(
            int generatorAnnotationIdx,
            int iterations,
            double expectedPercent,
            double expectedDeviation
    ) throws Exception {
        // arrange / given
        final Generator<Boolean> generator= super.createTemplateObjectFromAnnotation(generatorAnnotationIdx);

        // act / when
        final double percentageTrues=percentageTrues(generator, iterations);

        // assert / then
        assertThat("Percentage with expected deviation?", percentageTrues, is(closeTo(expectedPercent, expectedDeviation)));

    }

    @Test
    public void _100_iterations__should_be_in_a_10_percent_range() throws Exception {
        final int iterations=100;
        final double expectedDeviation=10d;

        assertNumberOfGeneratedTrues(TWENTY_PERCENT_WITH_SEED_42, iterations, 20d, expectedDeviation);
        assertNumberOfGeneratedTrues(SEVENTY_PERCENT_WITH_SEED_1, iterations, 70d, expectedDeviation);
    }

    @Test
    public void _1000_iterations__should_be_in_a_5_percent_range() throws Exception {
        final int iterations=1_000;
        final double expectedDeviation=5d;

        assertNumberOfGeneratedTrues(TWENTY_PERCENT_WITH_SEED_42, iterations, 20d, expectedDeviation);
        assertNumberOfGeneratedTrues(SEVENTY_PERCENT_WITH_SEED_1, iterations, 70d, expectedDeviation);
    }

    @Test
    public void _1_000_000_iterations__should_be_in_a_1_per_mil_range() throws Exception {
        final int iterations=1_000_000;
        final double expectedDeviation=0.1d;

        assertNumberOfGeneratedTrues(TWENTY_PERCENT_WITH_SEED_42, iterations, 20d, expectedDeviation);
        assertNumberOfGeneratedTrues(SEVENTY_PERCENT_WITH_SEED_1, iterations, 70d, expectedDeviation);
    }

    @Test
    public void use_within_template__should_be_usable_in_if_statements() throws Exception {
        // arrange / given
        // act / when
        final String generated=generate(
                template("#if( %ton%.next() ) X#else -#end", 20),
                super.createTemplateObjectFromAnnotation(SEVENTY_PERCENT_WITH_SEED_1)
        );

        // assert / then
        assertThat(generated, is(" X X - X X - X X X - X X X X X X X X - -"));
    }

    @RandomBooleanGenerator(name= DTON, percent=20, seed=42)
    @RandomBooleanGenerator(name= DTON, percent=70, seed=1)
    private static class TestFixture {
    }
}
