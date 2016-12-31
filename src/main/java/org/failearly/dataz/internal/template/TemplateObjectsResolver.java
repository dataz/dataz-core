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
package org.failearly.dataz.internal.template;

import org.failearly.common.annotation.traverser.MetaAnnotationHandlerBase;
import org.failearly.common.annotation.traverser.MetaAnnotationTraverser;
import org.failearly.common.annotation.traverser.TraverseDepth;
import org.failearly.common.classutils.ObjectCreator;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.template.TemplateObjectFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.LinkedList;

import static org.failearly.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;
import static org.failearly.common.annotation.utils.AnnotationUtils.getMetaAnnotation;

/**
 * TemplateObjectsResolver is responsible for resolving and creating {@link TemplateObjects} from TOAs.
 */
public final class TemplateObjectsResolver {

    private final MetaAnnotationTraverser<TemplateObjectFactory.Definition> toaTraverser;
    private TemplateObjectDuplicateStrategy templateObjectDuplicateStrategy;

    private TemplateObjectsResolver(TraverseDepth traverseDepth, TemplateObjectDuplicateStrategy templateObjectDuplicateStrategy) {
        this.toaTraverser = metaAnnotationTraverser(TemplateObjectFactory.Definition.class)
            .withTraverseStrategy(templateObjectDuplicateStrategy.traverseStrategy())
            .withTraverseDepth(traverseDepth)
            .build();
        this.templateObjectDuplicateStrategy = templateObjectDuplicateStrategy;
    }

    public static TemplateObjectsResolver withStandardSettings() {
        return createTemplateObjectsResolver(
            DataSetProperties.getTemplateObjectTraverseDepth(),
            DataSetProperties.getTemplateObjectDuplicateStrategy()
        );
    }

    public static TemplateObjectsResolver createTemplateObjectsResolver(TraverseDepth traverseDepth, TemplateObjectDuplicateStrategy templateObjectDuplicateStrategy) {
        return new TemplateObjectsResolver(traverseDepth, templateObjectDuplicateStrategy);
    }

    public static TemplateObjectsResolver testTemplateObjectResolver() {
        return createTemplateObjectsResolver(TraverseDepth.CLASS_HIERARCHY, TemplateObjectDuplicateStrategy.IGNORE);
    }


    /**
     * Resolves {@link TemplateObjects} from a class.
     *
     * @param clazz the class object.
     * @return the {@link TemplateObjects}.
     */
    public TemplateObjects resolveFromClass(Class<?> clazz) {
        final TemplateObjectsImpl templateObjects = createTemplateObjects();
        doResolveTemplateObjectCreatorsFromClass(clazz, templateObjects);
        return templateObjects;
    }

    private void doResolveTemplateObjectCreatorsFromClass(Class<?> clazz, final TemplateObjects templateObjects) {
        toaTraverser.traverse(clazz, new MetaAnnotationHandlerBase<TemplateObjectFactory.Definition>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                templateObjects.add(createTemplateObjectCreator(annotation, clazz));
            }
        });
    }

    /**
     * Resolves {@link TemplateObjects} from a method.
     *
     * @param method the method object.
     * @return the {@link TemplateObjects}.
     */
    public TemplateObjects resolveFromMethod(Method method) {
        final TemplateObjectsImpl templateObjects = createTemplateObjects();
        doResolveTemplateObjectCreatorsFromMethod(method, templateObjects, method);
        return templateObjects;
    }

    private void doResolveTemplateObjectCreatorsFromMethod(Method clazz, final TemplateObjects templateObjects, AnnotatedElement annotatedElement) {
        toaTraverser.traverse(clazz, new MetaAnnotationHandlerBase<TemplateObjectFactory.Definition>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                templateObjects.add(createTemplateObjectCreator(annotation, annotatedElement));
            }
        });
    }


    private TemplateObjectsImpl createTemplateObjects() {
        return new TemplateObjectsImpl(new LinkedList<>(), templateObjectDuplicateStrategy, templateObjectDuplicateStrategy);
    }

    private static TemplateObjectCreator createTemplateObjectCreator(Annotation annotation, AnnotatedElement annotatedElement) {
        return new TemplateObjectCreator(
            createTemplateObjectFactory(getMetaAnnotation(TemplateObjectFactory.Definition.class, annotation)),
            annotation,
            annotatedElement
        );
    }

    private static TemplateObjectFactory createTemplateObjectFactory(TemplateObjectFactory.Definition definition) {
        return ObjectCreator.createInstance(resolveTemplateFactoryClass(definition));
    }

    private static Class<? extends TemplateObjectFactory> resolveTemplateFactoryClass(TemplateObjectFactory.Definition definition) {
        return definition.value();
    }

}
