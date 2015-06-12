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

import org.failearly.dataset.template.generator.Limit;
import org.failearly.dataset.template.generator.RangeGenerator;
import org.failearly.dataset.template.generator.support.Generator;
import org.failearly.dataset.internal.template.generator.GeneratorTestBase;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class RangeGeneratorFactoryTest extends GeneratorTestBase<Integer, RangeGenerator, RangeGeneratorFactory> {

    public RangeGeneratorFactoryTest() {
        super(RangeGeneratorFactory.class, RangeGenerator.class);
    }

    @Test
    public void rangeGenerator() throws Exception {
        final Generator<Integer> generator=defaultGenerator();
        assertThat("Expected values?", generator, contains(0, 1, 2, 3, 4));
    }

    @Test
    public void singleValueRangeGenerator() throws Exception {
        final Generator<Integer> generator=createGenerator(TestFixture.class, 1);
        assertThat("Expected values?", generator, contains(0));
    }

    @Test
    public void unlimitedRange() throws Exception {
        final Generator<Integer> generator=createGenerator(TestFixture.class, 2);
        assertUnlimitedGenerator(generator);
    }

    @Override
    protected Generator<Integer> defaultGenerator() throws Exception {
        return createGenerator(TestFixture.class);
    }


    @RangeGenerator(name="RG", dataset = "DS", start = 0, end = 4)
    @RangeGenerator(name="RG", dataset = "DS", start = 0, end = 0)
    @RangeGenerator(name="RG", dataset = "DS", start = 0, end = 4, limit = Limit.UNLIMITED)
    private static class TestFixture {}
}