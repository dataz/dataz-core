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

package org.failearly.dataset.internal.template.generator;

import org.apache.commons.collections4.IteratorUtils;
import org.failearly.dataset.template.generator.support.Generator;
import org.failearly.dataset.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.template.TemplateObjectFactoryDefinition;
import org.failearly.dataset.template.generator.support.UnlimitedGenerator;
import org.failearly.dataset.test.AnnotationHelper;
import org.failearly.dataset.test.AssertException;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * GeneratorTestBase provides some basic tests for any generator implementation and some utility methods.
 *
 * @param <T> target type of the generator (see {@link Generator#next()}).
 * @param <GA> the generator annotation
 * @param <GF> the generator factory implementation class
 */
public abstract class GeneratorTestBase<T, GA extends Annotation, GF extends GeneratorFactoryBase<T, GA>> {
    private final Class<GF> generatorFactoryClass;
    private final Class<GA> generatorAnnotationClass;
    private final AnnotationHelper<GA> annotationHelper;
    private int lastIndex = -1;


    protected GeneratorTestBase(Class<GF> generatorFactoryClass, Class<GA> generatorAnnotationClass) {
        this.generatorFactoryClass = generatorFactoryClass;
        this.generatorAnnotationClass = generatorAnnotationClass;
        this.annotationHelper = AnnotationHelper.createAnnotationHelper(generatorAnnotationClass);
    }

    @Test
    public final void testNameAndDataset() throws Exception {
        final Generator<T> generator = defaultGenerator();
        assertThat("generator name?", generator.name(), is(expectedGeneratorName()));
        assertThat("generator dataset?", generator.dataset(), is(expectedDataSetName()));
    }

    @Test
    public void annotation_class__should_have_correct_meta_annotation() throws Exception {
        assertTrue("Annotation class has correct Meta Annotation?", generatorAnnotationClass.isAnnotationPresent(TemplateObjectFactoryDefinition.class));
    }

    @Test
    public void annotation_class__should_be_associated_with_expected_factory_class() throws Exception {
        assertThat("Annotation class has correct GeneratorFactoryDefinition#factory?",
                generatorAnnotationClass.getDeclaredAnnotation(TemplateObjectFactoryDefinition.class).factory().getName(), is(generatorFactoryClass.getName()));
    }

    protected abstract Generator<T> defaultGenerator() throws Exception;

    protected List<T> asList(Generator<T> generator) {
        return IteratorUtils.toList(generator.iterator());
    }

    protected final Generator<T> createGenerator(Class<?> testFixture, int index) throws Exception {
        return createGenerator(resolveDeclaredAnnotations(testFixture, index));
    }

    protected final Generator<T> createGenerator(Class<?> testFixture) throws Exception {
        return createGenerator(resolveDeclaredAnnotations(testFixture, 0));
    }

    @SuppressWarnings("unchecked")
    private Generator<T> createGenerator(GA annotation) throws Exception {
        final GF generatorFactory = generatorFactoryClass.newInstance();

        return (Generator<T>) generatorFactory.create(annotation);
    }

    /**
     * Returns the actually annotation (at index) from the {@code testFixtureClass}.
     */
    private GA resolveDeclaredAnnotations(Class<?> testFixtureClass, int index) {
        this.lastIndex = index;
        return annotationHelper.withFixtureClass(testFixtureClass).getAnnotation(index);
    }


    private String expectedDataSetName() {
        return resolveAnnotationElementValue("dataset");
    }

    private String expectedGeneratorName() {
        return resolveAnnotationElementValue("name");
    }

    private String resolveAnnotationElementValue(String element) {
        assert lastIndex>=0 : "Please call first createGenerator()";
        return annotationHelper.resolveElementValue(this.lastIndex, element);
    }


    protected final void assertUnlimitedGenerator(Generator<T> generator) {
        assertThat("Unlimited Type?", generator, instanceOf(UnlimitedGenerator.class));
        assertUnsupportedIterator(generator);
    }

    protected void assertUnsupportedIterator(Generator<T> generator) {
        AssertException.assertException(
                UnsupportedOperationException.class,
                "Don't use iterator() for unlimited generators! Use next() instead.",
                generator::iterator
        );
    }


}
