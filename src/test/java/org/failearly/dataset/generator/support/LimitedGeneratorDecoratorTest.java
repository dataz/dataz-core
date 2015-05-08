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

package org.failearly.dataset.generator.support;

import org.failearly.dataset.internal.generator.decorator.GeneratorDecorators;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class LimitedGeneratorDecoratorTest {

    private static final String[] ANY_CONSTANT_ARRAY = new String[]{"v1", "v2"};
    private static final List<String> ANY_CONSTANT_LIST = Arrays.asList(ANY_CONSTANT_ARRAY);
    private static final int LIMIT = 5;

    private final UnlimitedGeneratorBase<String> unlimitedGenerator = new UnlimitedGenerator();
    private Generator<String> limitedGenerator;

    @Before
    public void setUp() throws Exception {
        unlimitedGenerator.init();
        limitedGenerator = GeneratorDecorators.makeLimited(unlimitedGenerator, LIMIT).init();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void assumeIteratorNotSupported() throws Exception {
        unlimitedGenerator.iterator();
    }

    @Test
    public void assumeUnlimitedNext() throws Exception {
        for (int i = 0; i < 10*LIMIT; i++) {
            assertThat(unlimitedGenerator.next(), isOneOf(ANY_CONSTANT_ARRAY));
        }
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
    public void limitedGeneratorNext() throws Exception {
        int count = 0;
        for (String val : limitedGenerator) {
            assertThat("next value == iterator.next()?", limitedGenerator.next(), is(val));
            count++;
        }

        assertThat("#iterations?", count, is(LIMIT));
        assertThat("after consuming the iterator -> next should return null?", limitedGenerator.next(), nullValue());
    }

    @Test
    public void twoLimitedGeneratorNext() throws Exception {
        int count = 0;
        for (String val : limitedGenerator) {
            assertThat("next value == iterator.next()?", limitedGenerator.next(), is(val));
            count++;
        }
        // new iterator. but next now returns always null.
        for (String notUsed : limitedGenerator) {
            assertThat("next value is null?", limitedGenerator.next(), nullValue());
            count++;
        }

        assertThat("#iterations?", count, is(2 * LIMIT));
    }


    private static class UnlimitedGenerator extends UnlimitedGeneratorBase<String> {

        UnlimitedGenerator() {
            super("NAME", "DATASET");
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