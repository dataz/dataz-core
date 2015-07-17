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

package org.failearly.dataset.template.generator.support;

import org.apache.commons.lang.mutable.MutableInt;
import org.failearly.dataset.internal.template.generator.decorator.GeneratorDecorators;
import org.failearly.dataset.template.common.Scope;
import org.failearly.dataset.template.generator.Generator;
import org.failearly.dataset.template.generator.InternalIteratorExhaustedException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static org.failearly.dataset.test.AssertException.assertException;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class LimitedGeneratorDecoratorTest {

    private static final String[] ANY_CONSTANT_ARRAY = new String[]{"val1", "val2"};
    private static final List<String> ANY_CONSTANT_LIST = Arrays.asList(ANY_CONSTANT_ARRAY);
    private static final int LIMIT = 5;

    private final UnlimitedGeneratorBase<String> unlimitedGenerator = new UnlimitedGenerator();
    private Generator<String> limitedGenerator;

    @Before
    public void setUp() throws Exception {
        unlimitedGenerator.init();
        limitedGenerator = GeneratorDecorators.makeLimited(unlimitedGenerator, LIMIT).init();
    }

    @Test
    public void assume_unlimited_generator__must_not_support_iterator() throws Exception {
        assertException(                                                                     //
                UnsupportedOperationException.class,                                         //
                "Don't use iterator() for unlimited generators! Use next() instead.",        //
                unlimitedGenerator::iterator                                                 //
        );
    }

    @Test
    public void assume_unlimited_generator__could_not_be_exhausted() throws Exception {
        for (int i = 0; i < 10*LIMIT; i++) {
            assertThat(unlimitedGenerator.next(), isOneOf(ANY_CONSTANT_ARRAY));
        }
    }

    @Test
    public void limited_generator__should_delegate_properties__and__toString() throws Exception {
        assertThat("name?", limitedGenerator.name(), is(unlimitedGenerator.name()));
        assertThat("dataset?", limitedGenerator.dataset(), is(unlimitedGenerator.dataset()));
        assertThat("scope?", limitedGenerator.scope(), is(unlimitedGenerator.scope()));
        assertThat("toString?", limitedGenerator.toString(), is(unlimitedGenerator.toString()));
    }


    @Test
    public void limited_generator__should_support_external_iterator() throws Exception {
        int count = 0;
        for (String val : limitedGenerator) {
            assertThat("next value == iterator.next()?", limitedGenerator.next(), is(val));
            count++;
        }

        assertThat("#iterations?", count, is(LIMIT));
        assertException(InternalIteratorExhaustedException.class, limitedGenerator::next);
    }

    @Test
    public void limited_generator__could_be_exhausted() throws Exception {
        consumeAllValues(limitedGenerator, (g)->{});

        assertException(InternalIteratorExhaustedException.class, limitedGenerator::next);
    }

    private static void consumeAllValues(Generator<String> generator, Consumer<Generator<String>> consumer) {
        //noinspection unused
        for (String ignored : generator) {
            generator.next();
            consumer.accept(generator);
        }
    }

    @Test
    public void looping_over_created_iterator__should_ignore_reset() throws Exception {
        final MutableInt count = new MutableInt(0);
        consumeAllValues(limitedGenerator, (g)->{
            count.increment();g.reset();
        });

        assertThat("#iterations?", count.intValue(), is(LIMIT));
    }

    private static class UnlimitedGenerator extends UnlimitedGeneratorBase<String> {

        UnlimitedGenerator() {
            super("DATASET", "NAME", Scope.DEFAULT);
        }

        @Override
        public Iterator<String> createIterator() {
            return ANY_CONSTANT_LIST.iterator();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }

        @Override
        protected void doReset() {
            resetInternalIterator();
        }
    }

}