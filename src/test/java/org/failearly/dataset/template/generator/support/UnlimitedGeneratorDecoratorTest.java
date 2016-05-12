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

package org.failearly.dataset.template.generator.support;

import org.failearly.dataset.internal.template.generator.decorator.GeneratorDecorators;
import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.generator.Generator;
import org.failearly.common.test.ExceptionVerifier;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.JVM)
public class UnlimitedGeneratorDecoratorTest {

    private static final String[] ANY_CONSTANT_ARRAY = new String[] {"A", "B","C"};


    private final GeneratorBase<String> limitedGenerator = Mockito.spy(new LimitedGenerator());
    private final Generator<String> unlimitedGenerator = doInit(GeneratorDecorators.makeUnlimited(limitedGenerator));


    private static <T extends GeneratorBase<?>> T doInit(T generator) {
        generator.init();
        return generator;
    }

    @Test
    public void assume_limited_generator__working_like_expected() throws Exception {
        int count=0;
        for (String val : limitedGenerator) {
            assertThat("next value?", val, is(ANY_CONSTANT_ARRAY[count]));
            count++;
        }

        assertThat("#iterations?", count, is(ANY_CONSTANT_ARRAY.length));
    }

    @Test
    public void assume_on_limited_generator__exhausting_internal_iterator__should_cause_an_exception() throws Exception {
        consumeAllValues(limitedGenerator);

        ExceptionVerifier.on(limitedGenerator::next).expect(InternalIteratorExhaustedException.class).verify();
    }

    private static void consumeAllValues(Generator<String> generator) {
        //noinspection unused
        for (String ignored : generator) {
            generator.next();
        }
    }

    @Test
    public void unlimited_generator__should_delegate_properties__and__toString() throws Exception {
        assertThat("name?", unlimitedGenerator.name(), is(limitedGenerator.name()));
        assertThat("dataset?", unlimitedGenerator.dataset(), is(limitedGenerator.dataset()));
        assertThat("scope?", unlimitedGenerator.scope(), is(limitedGenerator.scope()));
        assertThat("toString?", unlimitedGenerator.toString(), is(limitedGenerator.toString()));
    }

    @Test
    public void unlimited_generator__could_not_be_exhausted() throws Exception {
        final List<String> values=new ArrayList<>();
        for (int i = 0; i < 2*ANY_CONSTANT_ARRAY.length; i++) {
            values.add(unlimitedGenerator.next());
        }

        assertThat(values, is(expectedValues()));
    }

    private List<String> expectedValues() {
        final List<String> expectedValues = new ArrayList<>();
        expectedValues.addAll(Arrays.asList(ANY_CONSTANT_ARRAY));
        expectedValues.addAll(Arrays.asList(ANY_CONSTANT_ARRAY));
        return expectedValues;
    }

    @Test
    public void unlimited_generator__should_not_support_external_iterator() throws Exception {
        ExceptionVerifier.on(unlimitedGenerator::iterator).expect(UnsupportedOperationException.class).expect("Don't use iterator() for unlimited generators! Use next() instead.").verify();
    }

    @Test
    public void unlimited_generator__should_delegate_to_limited_reset() throws Exception {
        // act / when
        unlimitedGenerator.reset();

        // assert / then
        Mockito.verify(limitedGenerator).reset();
    }

    @Test
    public void unlimited_generator_calling_reset__should_repeat_the_first_value() throws Exception {
        // arrange / given
        final List<String> values=new ArrayList<>();

        // act / when
        values.add(unlimitedGenerator.next());
        unlimitedGenerator.reset();

        values.add(unlimitedGenerator.next());
        unlimitedGenerator.reset();


        // assert / then
        assertThat(values, contains("A","A"));
    }

    private static class LimitedGenerator extends LimitedGeneratorBase<String> {

        LimitedGenerator() {
            super("DATASET", "NAME", Scope.DEFAULT);
        }

        @Override
        public Iterator<String> createIterator() {
            return Arrays.asList(ANY_CONSTANT_ARRAY).iterator();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

}