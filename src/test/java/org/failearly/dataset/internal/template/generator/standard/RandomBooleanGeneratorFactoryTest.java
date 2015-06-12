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
package org.failearly.dataset.internal.template.generator.standard;

import org.failearly.dataset.template.generator.support.Generator;
import org.failearly.dataset.template.generator.RandomBooleanGenerator;
import org.failearly.dataset.internal.template.generator.GeneratorTestBase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * RandomBooleanGeneratorFactoryTest contains tests for RandomBooleanGeneratorFactory
 */
public class RandomBooleanGeneratorFactoryTest extends GeneratorTestBase<Boolean,RandomBooleanGenerator,RandomBooleanGeneratorFactory> {

    public RandomBooleanGeneratorFactoryTest() {
        super(RandomBooleanGeneratorFactory.class, RandomBooleanGenerator.class);
    }

    @Test
    public void percentage20__100_iterations() throws Exception {
        // arrange / given
        final Generator<Boolean> generator = createGenerator(TestFixture.class, 0);

        // act / when
        double percentageTrues = percentageTrues(generator, 100);

        // assert / then
        assertThat("percentage true?", percentageTrues, is(closeTo(20d, 15d)));
    }

    @Test
    public void percentage20__1000_iterations() throws Exception {
        // arrange / given
        final Generator<Boolean> generator = createGenerator(TestFixture.class, 0);

        // act / when
        double percentageTrues = percentageTrues(generator, 1_000);

        // assert / then
        assertThat("percentage true?", percentageTrues, is(closeTo(20d, 5d)));
    }

    @Test
    public void percentage30__1_000_000_iterations() throws Exception {
        // arrange / given
        final Generator<Boolean> generator = createGenerator(TestFixture.class, 1);

        // act / when
        double percentageTrues = percentageTrues(generator, 1_000_000);

        // assert / then
        assertThat("percentage true?", percentageTrues, is(closeTo(30d, 0.1d)));
    }

    private double percentageTrues(Generator<Boolean> generator, int numIterations) {
        int numberOfTrues=0;

        // act / when
        for (int i = 0; i < numIterations; i++) {
            if( generator.next() ) {
                numberOfTrues++;
            }
        }
        return (numberOfTrues*100d)/numIterations;
    }

    @Override
    protected Generator<Boolean> defaultGenerator() throws Exception {
        return createGenerator(TestFixture.class);
    }

    @RandomBooleanGenerator(dataset="DS", name="20%", percent=20)
    @RandomBooleanGenerator(dataset="DS", name="30%", percent=30, seed=1)
    private static class TestFixture {}
}
