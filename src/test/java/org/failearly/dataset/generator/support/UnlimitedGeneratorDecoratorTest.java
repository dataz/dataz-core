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

package org.failearly.dataset.generator.support;

import org.failearly.dataset.internal.generator.decorator.GeneratorDecorators;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.JVM)
public class UnlimitedGeneratorDecoratorTest {

    private static final String[] ANY_CONSTANT_ARRAY = new String[] {"v1", "v2"};
    private static final List<String> ANY_CONSTANT_LIST = Arrays.asList(ANY_CONSTANT_ARRAY);


    private final GeneratorBase<String> limitedGenerator = new LimitedGenerator().init();
    private final Generator<String> unlimitedGenerator = GeneratorDecorators.makeUnlimited(limitedGenerator).init();

    @Test
    public void assumeLimitedGeneratorIterator() throws Exception {
        int count=0;
        for (String val : limitedGenerator) {
            assertThat("next value?", val, isOneOf(ANY_CONSTANT_ARRAY));
            count++;
        }

        assertThat("#iterations?", count, is(ANY_CONSTANT_ARRAY.length));
    }

    @Test
    public void assumeLimitedGeneratorNext() throws Exception {
        int count=0;
        for (String val : limitedGenerator) {
            assertThat("next value == iterator.next()?", limitedGenerator.next(), is(val));
            count++;
        }

        for (int i=0; i<100; i++) {
            assertThat("after consuming the iterator -> next should return null?", limitedGenerator.next(), nullValue());
            count++;
        }

        assertThat("#iterations?", count, is(ANY_CONSTANT_ARRAY.length+100));
    }

    @Test
    public void nameDelegated() throws Exception {
        assertThat("name?", unlimitedGenerator.name(), is(limitedGenerator.name()));
    }

    @Test
    public void datasetDelegated() throws Exception {
        assertThat("dataset?", unlimitedGenerator.dataset(), is(limitedGenerator.dataset()));
    }

    @Test
    public void nextHasNoLimit() throws Exception {
        for (int i = 0; i < 3_043; i++) {
            assertThat("next value?", unlimitedGenerator.next(), isOneOf(ANY_CONSTANT_ARRAY));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorNotSupported() throws Exception {
        unlimitedGenerator.iterator();
    }

    @Test
    public void decoratedToString() throws Exception {
        assertThat("toString?", unlimitedGenerator.toString(), is("UnlimitedGeneratorDecorator(generator="+limitedGenerator+")"));
    }

    private static class LimitedGenerator extends GeneratorBase<String> {

        LimitedGenerator() {
            super("DATASET", "NAME");
        }

        @Override
        public Iterator<String> createIterator() {
            return ANY_CONSTANT_LIST.iterator();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

}