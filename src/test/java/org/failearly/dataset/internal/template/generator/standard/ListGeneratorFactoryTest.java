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
import org.failearly.dataset.template.generator.ListGenerator;
import org.failearly.dataset.template.generator.support.Generator;
import org.failearly.dataset.internal.template.generator.GeneratorTestBase;
import org.junit.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;

public class ListGeneratorFactoryTest extends GeneratorTestBase<String, ListGenerator, ListGeneratorFactory> {

    private static final String[] VALUES = new String[]{"v1", "v2", "v3"};

    public ListGeneratorFactoryTest() {
        super(ListGeneratorFactory.class, ListGenerator.class);
    }

    @Test
    public void noneEmptyList() throws Exception {
        final Generator<String> generator=defaultGenerator();
        assertThat("Expected values?", generator, contains(VALUES));
    }

    @Test
    public void emptyList() throws Exception {
        final Generator<String> generator=createGenerator(TestFixture.class, 1);
        assertThat("No values?", asList(generator), hasSize(0));
    }

    @Test
    public void unlimitedList() throws Exception {
        final Generator<String> generator=createGenerator(TestFixture.class, 2);
        assertUnlimitedGenerator(generator);
    }

    @Override
    protected Generator<String> defaultGenerator() throws Exception {
        return createGenerator(TestFixture.class);
    }

    @ListGenerator(name = "LG", dataset = "DS", values = {"v1", "v2", "v3"})
    @ListGenerator(name = "LG", dataset = "DS", values = {})
    @ListGenerator(name = "LG", dataset = "DS", values = {"v1", "v2", "v3"}, limit = Limit.UNLIMITED)
    private static class TestFixture {
    }

}