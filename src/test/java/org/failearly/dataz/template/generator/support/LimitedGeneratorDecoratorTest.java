/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.template.generator.support;

import org.apache.commons.lang.mutable.MutableInt;
import org.failearly.dataz.internal.common.test.ExceptionVerifier;
import org.failearly.dataz.internal.template.generator.decorator.GeneratorDecorators;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.generator.Generator;
import org.failearly.dataz.template.simple.Constant;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertThat;

public class LimitedGeneratorDecoratorTest {

    private static final String[] ANY_CONSTANT_ARRAY = new String[]{"val1", "val2"};
    private static final List<String> ANY_CONSTANT_LIST = Arrays.asList(ANY_CONSTANT_ARRAY);
    private static final int LIMIT = 5;

    private final UnlimitedGeneratorBase<String> unlimitedGenerator = new UnlimitedGenerator();
    private final Generator<String> limitedGenerator=doInit(GeneratorDecorators.makeLimited(unlimitedGenerator, LIMIT));

    private static <T extends GeneratorBase<?>> T doInit(T generator) {
        generator.init();
        return generator;
    }

    @Test
    public void assume_unlimited_generator__must_not_support_iterator() throws Exception {
        ExceptionVerifier.on(unlimitedGenerator::iterator).expect(UnsupportedOperationException.class).expect("Don't use iterator() for unlimited generators! Use next() instead.").verify();
    }

    @Test
    public void assume_unlimited_generator__could_not_be_exhausted() throws Exception {
        for (int i = 0; i < 10*ANY_CONSTANT_ARRAY.length; i++) {
            assertThat(unlimitedGenerator.next(), is(isOneOf(ANY_CONSTANT_ARRAY)));
        }
    }

    @Test
    public void limited_generator__should_delegate_properties__and__toString() throws Exception {
        assertThat("name?", limitedGenerator.name(), is(unlimitedGenerator.name()));
        assertThat("dataz?", limitedGenerator.datasets(), is(unlimitedGenerator.datasets()));
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
        ExceptionVerifier.on(limitedGenerator::next).expect(InternalIteratorExhaustedException.class).verify();
    }

    @Test
    public void limited_generator__could_be_exhausted() throws Exception {
        consumeAllValues(limitedGenerator);

        ExceptionVerifier.on(limitedGenerator::next).expect(InternalIteratorExhaustedException.class).verify();
    }

    private static void consumeAllValues(Generator<String> generator) {
        consumeAllValues(generator, (g)->{});
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


    private static TemplateObject templateObjectMock = TemplateObjectMock.createTemplateObjectMock(AnnotationHolder.class);

    @Constant(name="NAME",value = "value", scope = Scope.DEFAULT, datasets = "DATASET")
    private static class AnnotationHolder {}

    private static class UnlimitedGenerator extends UnlimitedGeneratorBase<String> {


        UnlimitedGenerator() {
            super(templateObjectMock);
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