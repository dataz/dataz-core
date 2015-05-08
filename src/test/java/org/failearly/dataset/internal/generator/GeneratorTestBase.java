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
package org.failearly.dataset.internal.generator;

import org.apache.commons.collections4.IteratorUtils;
import org.failearly.dataset.generator.support.Generator;
import org.failearly.dataset.generator.support.GeneratorFactoryDefinition;
import org.failearly.dataset.generator.support.UnlimitedGenerator;
import org.failearly.dataset.generator.support.GeneratorFactory;
import org.failearly.dataset.test.TestUtils;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * AbstractGeneratorTest provides some basic tests for any generator implementation and some utility methods.
 */
public abstract class GeneratorTestBase<T, GA extends Annotation, GF extends GeneratorFactory<T, GA>> {
    private final Class<GF> generatorFactoryClass;
    private final Class<GA> generatorAnnotationClass;
    private GA annotationInstance;


    protected GeneratorTestBase(Class<GF> generatorFactoryClass, Class<GA> generatorAnnotationClass) {
        this.generatorFactoryClass = generatorFactoryClass;
        this.generatorAnnotationClass = generatorAnnotationClass;
    }

    @Test
    public final void testNameAndDataset() throws Exception {
        final Generator generator = defaultGenerator();
        assertThat("generator name?", generator.name(), is(expectedGeneratorName()));
        assertThat("generator dataset?", generator.dataset(), is(expectedDataSetName()));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testGeneratorDefinition() throws Exception {
        assertThat("Annotation class has correct Meta Annotation?", generatorAnnotationClass.isAnnotationPresent(GeneratorFactoryDefinition.class), is(true));
        assertThat("Annotation class has correct GeneratorFactoryDefinition#generatorFactory?",
                generatorAnnotationClass.getDeclaredAnnotation(GeneratorFactoryDefinition.class).generatorFactory().getName(), is(generatorFactoryClass.getName()));
    }

    protected abstract Generator defaultGenerator() throws Exception;

    protected List<T> asList(Generator<T> generator) {
        return IteratorUtils.toList(generator.iterator());
    }

    protected final Generator<T> createGenerator(Class<?> testFixture, int index) throws Exception {
        return createGenerator(resolveDeclaredAnnotations(testFixture, index));
    }

    protected final Generator<T> createGenerator(Class<?> testFixture) throws Exception {
        return createGenerator(resolveDeclaredAnnotations(testFixture, 0));
    }

    private Generator<T> createGenerator(GA annotation) throws Exception {
        final GF generatorFactory = generatorFactoryClass.newInstance();

        return generatorFactory.create(annotation);
    }

    /**
     * Returns the actually annotation (at index) from the {@code testFixtureClass}.
     */
    private GA resolveDeclaredAnnotations(Class<?> testFixtureClass, int index) {
        annotationInstance = testFixtureClass.getAnnotationsByType(generatorAnnotationClass)[index];
        return annotationInstance;
    }


    private String expectedDataSetName() throws Exception {
        return String.valueOf(resolveAnnotationValue("dataset"));
    }

    private String expectedGeneratorName() throws Exception {
        return String.valueOf(resolveAnnotationValue("name"));
    }

    private Object resolveAnnotationValue(String methodName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return generatorAnnotationClass.getMethod(methodName).invoke(annotationInstance);
    }


    protected final void assertUnlimitedGenerator(Generator<T> generator) {
        assertThat("Unlimited Type?", generator, instanceOf(UnlimitedGenerator.class));
        assertUnsupportedIterator(generator);
    }

    protected static void assertUnsupportedIterator(Generator generator) {
        TestUtils.assertException(
                UnsupportedOperationException.class,
                "Don't use iterator() for unlimited generators! Use next() instead.",
                generator::iterator
        );
    }


}