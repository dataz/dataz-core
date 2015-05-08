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

package org.failearly.dataset.internal.generator.resolver;

import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.generator.support.GeneratorFactoryDefinition;
import org.failearly.dataset.generator.support.GeneratorFactory;
import org.failearly.dataset.internal.annotation.*;
import org.failearly.dataset.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static org.failearly.dataset.internal.annotation.AnnotationUtils.getMetaAnnotation;

/**
 * GeneratorResolver is responsible for resolving
 */
public final class GeneratorResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorResolver.class);

    private GeneratorResolver() {
    }

    /**
     * Resolves {@link org.failearly.dataset.internal.generator.resolver.GeneratorCreator} from a test method.
     *
     * @param testMethod the test method.
     * @return List of {@link org.failearly.dataset.internal.generator.resolver.GeneratorCreator}s.
     */
    public static List<GeneratorCreator> resolveFromTestMethod(Method testMethod) {
        final List<GeneratorCreator> creators = new LinkedList<>();
        doResolveGenerators(testMethod, creators);
        return creators;
    }

    /**
     * Resolves {@link org.failearly.dataset.internal.generator.resolver.GeneratorCreator} from a test class.
     *
     * @param testClass the test class.
     * @return List of {@link org.failearly.dataset.internal.generator.resolver.GeneratorCreator}s.
     */
    public static List<GeneratorCreator> resolveFromTestClass(Class<?> testClass) {
        final List<GeneratorCreator> creators = new LinkedList<>();
        doResolveGenerators(testClass, creators);
        return creators;
    }

    private static void doResolveGenerators(Method testMethod, final List<GeneratorCreator> creators) {
        final AnnotationTraverser<Annotation> traverser = AnnotationTraversers.createMetaAnnotationTraverser(
                GeneratorFactoryDefinition.class,
                DataSetProperties.getGeneratorTraversingStrategy(),
                Order.UNCHANGED);
        traverser.traverse(testMethod, new AnnotationHandlerBase<Annotation>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                creators.add(createGeneratorCreator(annotation));
            }
        });
    }

    private static void doResolveGenerators(Class<?> testClass, final List<GeneratorCreator> creators) {
        final AnnotationTraverser<Annotation> traverser = AnnotationTraversers.createMetaAnnotationTraverser(
                GeneratorFactoryDefinition.class,
                DataSetProperties.getGeneratorTraversingStrategy(),
                Order.UNCHANGED);
        traverser.traverse(testClass, new AnnotationHandlerBase<Annotation>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                creators.add(createGeneratorCreator(annotation));
            }
        });
    }

    private static GeneratorCreator createGeneratorCreator(Annotation annotation) {
        return new GeneratorCreator(
                createGeneratorFactoryFromDefinition(getMetaAnnotation(GeneratorFactoryDefinition.class, annotation)),
                annotation
        );
    }

    private static GeneratorFactory createGeneratorFactoryFromDefinition(GeneratorFactoryDefinition generatorFactoryDefinition) {
        return ClassUtils.createInstance(generatorFactoryDefinition.generatorFactory());
    }

}
