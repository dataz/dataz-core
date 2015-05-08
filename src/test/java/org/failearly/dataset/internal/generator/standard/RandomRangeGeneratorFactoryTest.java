/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.internal.generator.standard;

import org.failearly.dataset.generator.Limit;
import org.failearly.dataset.generator.RandomRangeGenerator;
import org.failearly.dataset.internal.generator.GeneratorTestBase;
import org.failearly.dataset.generator.support.Generator;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RandomRangeGeneratorFactoryTest extends GeneratorTestBase<Integer,RandomRangeGenerator,RandomRangeGeneratorFactory> {

    private static final int LIMIT_VALUE = 5;

    public RandomRangeGeneratorFactoryTest() {
        super(RandomRangeGeneratorFactory.class, RandomRangeGenerator.class);
    }

    @Test
    public void generatorWithLimitedRange() throws Exception {
        unlimitedRandomGeneratorTest(0, 1_000, expectedValues(0, 1, 2, 3, 4));
    }

    @Test
    public void generatorWithSeed() throws Exception {
        unlimitedRandomGeneratorTest(1, 3, expectedValues(215764588, 1569548985, 880641847));
    }

    @Test
    public void generatorWithLimitedRangeAndSeed() throws Exception {
        // Here we use the unlimited generator (generatorNumber 2), but #iterations == LIMIT_VALUE
        unlimitedRandomGeneratorTest(2, LIMIT_VALUE, expectedValues(0, 2, 3, 4));
    }

    @Test
    public void limitedGenerator() throws Exception {
        // Here we use the Limited generator (generatorNumber 3)
        limitedRandomGeneratorTest(3, LIMIT_VALUE, expectedValues(0, 2, 3, 4));
    }

    @Test
    public void uniqueGenerator() throws Exception {
        // Here we use the unique generator (generatorNumber 4)
        limitedRandomGeneratorTest(4, LIMIT_VALUE, expectedValues(0, 1, 2, 3, 4));
    }

    @Test
    public void uniqueGenerator_with_limit() throws Exception {
        // Here we use the unique generator (generatorNumber 5)
        limitedRandomGeneratorTest(5, LIMIT_VALUE, expectedValues(2, 4, 6, 8, 9));
    }

    private void unlimitedRandomGeneratorTest(int generatorNumber, int numIterations, Set<Integer> expectedValues) throws Exception {
        final Generator<Integer> generator = createGenerator(TestFixture.class, generatorNumber);
        final Set<Integer> values=new HashSet<>();
        for (int i = 0; i < numIterations; i++) {
            values.add(generator.next());
        }

        assertThat("Generated values?", values, is(expectedValues));
        assertUnsupportedIterator(generator);
    }

    private void limitedRandomGeneratorTest(int generatorNumber, int expectedIterations, Set<Integer> expectedValues) throws Exception {
        final Generator<Integer> generator = createGenerator(TestFixture.class, generatorNumber);
        final Set<Integer> values=new HashSet<>();
        int count=0;
        for (Integer value : generator) {
            values.add(value);
            count++;
        }

        assertThat("#Iterations?", count, is(expectedIterations));
        assertThat("Generated values?", values, is(expectedValues));
    }

    @Override
    protected Generator<Integer> defaultGenerator() throws Exception {
        return createGenerator(TestFixture.class);
    }

    @RandomRangeGenerator(name="RG", dataset = "DS", start = 0, end = 4)
    @RandomRangeGenerator(name="RG", dataset = "DS", seed=1)
    @RandomRangeGenerator(name="RG", dataset = "DS", start = 0, end = 4, seed=1)
    @RandomRangeGenerator(name="RG", dataset = "DS", start = 0, end = 4, seed=1, limit = Limit.LIMITED, count = LIMIT_VALUE)
    @RandomRangeGenerator(name="RG", dataset = "DS", start = 0, end = 4, seed=1, limit = Limit.UNLIMITED /*ignored*/, unique = true)
    @RandomRangeGenerator(name="RG", dataset = "DS", start = 0, end = 10, seed=1, limit = Limit.UNLIMITED /*ignored*/, unique = true, count=LIMIT_VALUE)
    private static class TestFixture {}


    private static Set<Integer> expectedValues(Integer... values) {
        return new HashSet<>(Arrays.asList(values));
    }
}