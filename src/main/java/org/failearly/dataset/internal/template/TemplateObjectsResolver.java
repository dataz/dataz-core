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

package org.failearly.dataset.internal.template;

import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.internal.annotation.*;
import org.failearly.dataset.template.TemplateObjectFactory;
import org.failearly.dataset.template.TemplateObjectFactoryDefinition;
import org.failearly.dataset.util.ObjectCreator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.failearly.dataset.internal.annotation.AnnotationUtils.getMetaAnnotation;

/**
 * GeneratorResolver is responsible for resolving
 */
public final class TemplateObjectsResolver {

    private TemplateObjectsResolver() {
    }

    /**
     * Resolves {@link TemplateObjects} from a test method.
     *
     * @param testMethod the test method.
     * @return the template {@link TemplateObjects}.
     */
    public static TemplateObjects resolveFromTestMethod(Method testMethod) {
        final TemplateObjects creators = new TemplateObjects();
        doResolveTemplateObjectCreators(testMethod, creators);
        return creators;
    }

    /**
     * Resolves {@link TemplateObjects} from a test class.
     *
     * @param testClass the test class.
     * @return the template {@link TemplateObjects}.
     */
    public static TemplateObjects resolveFromTestClass(Class<?> testClass) {
        final TemplateObjects creators = new TemplateObjects();
        doResolveTemplateObjectCreators(testClass, creators);
        return creators;
    }

    private static void doResolveTemplateObjectCreators(Class<?> testClass, final TemplateObjects creators) {
        final AnnotationTraverser<Annotation> traverser = AnnotationTraversers.createMetaAnnotationTraverser(
                TemplateObjectFactoryDefinition.class,
                TraverseStrategy.TOP_DOWN,
                TraverseDepth.CLASS_HIERARCHY
        );
        traverser.traverse(testClass, new AnnotationHandlerBase<Annotation>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                creators.add(createTemplateObjectCreator(annotation));
            }
        });
    }

    private static void doResolveTemplateObjectCreators(Method testClass, final TemplateObjects creators) {
        final AnnotationTraverser<Annotation> traverser = AnnotationTraversers.createMetaAnnotationTraverser(
                TemplateObjectFactoryDefinition.class,
                TraverseStrategy.TOP_DOWN,
                TraverseDepth.CLASS_HIERARCHY
        );
        traverser.traverse(testClass, new AnnotationHandlerBase<Annotation>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                creators.add(createTemplateObjectCreator(annotation));
            }
        });
    }

    private static TemplateObjectCreator createTemplateObjectCreator(Annotation annotation) {
        return new TemplateObjectCreator(
                createTemplateObjectFactoryFromDefinition(getMetaAnnotation(TemplateObjectFactoryDefinition.class, annotation)),
                annotation
        );
    }

    private static TemplateObjectFactory createTemplateObjectFactoryFromDefinition(TemplateObjectFactoryDefinition templateObjectFactoryDefinition) {
        return ObjectCreator.createInstance(templateObjectFactoryDefinition.factory());
    }

}
