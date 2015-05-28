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

package org.failearly.dataset.internal.generator.standard;

import org.failearly.dataset.generator.ConstantGenerator;
import org.failearly.dataset.generator.Limit;
import org.failearly.dataset.generator.support.Generator;
import org.failearly.dataset.internal.generator.GeneratorTestBase;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class ConstantGeneratorFactoryTest extends GeneratorTestBase<String, ConstantGenerator, ConstantGeneratorFactory> {

    public static final String ANY_CONSTANT = "any.constant";

    public ConstantGeneratorFactoryTest() {
        super(ConstantGeneratorFactory.class, ConstantGenerator.class);
    }

    @Test
    public void countZero() throws Exception {
        // arrange / given
        final Generator<String> generator = createGenerator(TestFixture.class, 0);

        // assert / then
        Assert.assertThat("next()?", generator.next(), is(nullValue()));
        Assert.assertThat("No values?", asList(generator), Matchers.hasSize(0));
    }

    @Test
    public void countOne() throws Exception {
        // arrange / given
        final Generator<String> generator = createGenerator(TestFixture.class, 1);

        // assert / then
        Assert.assertThat("next()?", generator.next(), is(ANY_CONSTANT));
        Assert.assertThat("Values?", generator, contains(ANY_CONSTANT));
    }

    @Test
    public void countMultiple() throws Exception {
        // arrange / given
        final Generator<String> generator = createGenerator(TestFixture.class, 2);

        // assert / then
        Assert.assertThat("next()?", generator.next(), is(ANY_CONSTANT));
        Assert.assertThat("Values?", generator, contains(ANY_CONSTANT, ANY_CONSTANT, ANY_CONSTANT));
    }

    @Test
    public void unlimited() throws Exception {
        // arrange / given
        final Generator<String> generator = createGenerator(TestFixture.class, 3);

        // assert / then
        assertUnlimitedGenerator(generator);
    }

    @Override
    protected Generator<String> defaultGenerator() throws Exception {
        return createGenerator(TestFixture.class);
    }

    @ConstantGenerator(name = "CG", dataset = "DS", constant = ANY_CONSTANT, count = 0, limit = Limit.LIMITED)
    @ConstantGenerator(name = "CG", dataset = "DS", constant = ANY_CONSTANT, count = 1, limit = Limit.LIMITED)
    @ConstantGenerator(name = "CG", dataset = "DS", constant = ANY_CONSTANT, count = 3, limit = Limit.LIMITED)
    @ConstantGenerator(name = "CG", dataset = "DS", constant = ANY_CONSTANT)
    private static class TestFixture {
    }
}