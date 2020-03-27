/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.template.generator.support;

import org.failearly.dataz.common.test.ExceptionVerifier;
import org.failearly.dataz.internal.template.generator.decorator.GeneratorDecorators;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.generator.Generator;
import org.failearly.dataz.template.simple.Constant;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import java.util.*;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
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
        assertThat("datasets?", unlimitedGenerator.datasets(), is(limitedGenerator.datasets()));
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

    private static TemplateObject templateObjectMock = TemplateObjectMock.createTemplateObjectMock(AnnotationHolder.class);

    @Constant(name="NAME",value = "value", scope = Scope.DEFAULT, datasets = "DATASET")
    private static class AnnotationHolder {}

    private static class LimitedGenerator extends LimitedGeneratorBase<String> {

        LimitedGenerator() {
            super(templateObjectMock);
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