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
package org.failearly.dataz.internal.template;

import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotationHandlerBase;
import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotationTraverser;
import org.failearly.dataz.internal.common.annotation.traverser.TraverseDepth;
import org.failearly.dataz.internal.common.classutils.ObjectCreatorUtil;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.TemplateObjectFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;

import static org.failearly.dataz.internal.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;
import static org.failearly.dataz.template.TemplateObjectAnnotationContext.createAnnotationContext;

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

    private void doResolveTemplateObjectCreatorsFromClass(Class<?> startingClass, final TemplateObjects templateObjects) {
        toaTraverser.traverse(startingClass, new MetaAnnotationHandlerBase<TemplateObjectFactory.Definition>() {
            @Override
            public void handleMetaClassAnnotation(final Class<?> clazz, Annotation annotation, TemplateObjectFactory.Definition metaAnnotation) {
                templateObjects.add(createTemplateObjectCreator(annotation, createAnnotationContext(clazz), metaAnnotation));
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
        doResolveTemplateObjectCreatorsFromMethod(method, templateObjects);
        return templateObjects;
    }

    private void doResolveTemplateObjectCreatorsFromMethod(Method method, final TemplateObjects templateObjects) {
        toaTraverser.traverse(method, new MetaAnnotationHandlerBase<TemplateObjectFactory.Definition>() {
            @Override
            public void handleMetaClassAnnotation(Class<?> clazz, Annotation annotation, TemplateObjectFactory.Definition metaAnnotation) {
                templateObjects.add(createTemplateObjectCreator(annotation, createAnnotationContext(clazz), metaAnnotation));
            }

            @Override
            public void handleMetaMethodAnnotation(Method method, Annotation annotation, TemplateObjectFactory.Definition metaAnnotation) {
                templateObjects.add(createTemplateObjectCreator(annotation, createAnnotationContext(method), metaAnnotation));
            }
        });
    }


    private TemplateObjectsImpl createTemplateObjects() {
        return new TemplateObjectsImpl(new LinkedList<>(), templateObjectDuplicateStrategy, templateObjectDuplicateStrategy);
    }

    private static TemplateObjectCreator createTemplateObjectCreator(Annotation annotation, TemplateObjectAnnotationContext templateObjectAnnotationContext, TemplateObjectFactory.Definition definition) {
        return new TemplateObjectCreator(
            createTemplateObjectFactory(definition),
            annotation,
            templateObjectAnnotationContext
        );
    }

    private static TemplateObjectFactory createTemplateObjectFactory(TemplateObjectFactory.Definition definition) {
        return ObjectCreatorUtil.createInstance(definition.value());
    }

}
