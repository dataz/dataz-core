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

package org.failearly.dataz.internal.common.annotation.resolver;

import org.failearly.dataz.internal.common.annotation.traverser.AnnotationHandler;
import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotationHandler;
import org.failearly.dataz.internal.common.annotation.filter.AnnotationFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;

/**
 * AnnotationResolverBase is the base implementation for {@link AnnotationResolver}.
 */
abstract class AnnotationResolverBase<T extends Annotation> implements AnnotationResolver<T> {
    final AnnotationFilter annotationFilter;
    final Class<T> annotationClass;

    AnnotationResolverBase(Class<T> annotationClass, AnnotationFilter annotationFilter) {
        this.annotationFilter = annotationFilter;
        this.annotationClass = annotationClass;
    }

    @Override
    public void resolveClassAnnotations(Class<?> clazz, AnnotationHandler<T> annotationHandler) {
        doResolveAnnotations(clazz, (annotation) -> annotationHandler.handleClassAnnotation(clazz, annotation));
    }

    @Override
    public void resolveMethodAnnotations(Method method, AnnotationHandler<T> annotationHandler) {
        doResolveAnnotations(method, (annotation)->annotationHandler.handleMethodAnnotation(method, annotation));
    }

    @Override
    public void resolveClassMetaAnnotations(Class<?> clazz, MetaAnnotationHandler<T> annotationHandler) {
    }

    @Override
    public void resolveMethodMetaAnnotations(Method method, MetaAnnotationHandler<T> annotationHandler) {
    }

    final void doResolveAnnotations(AnnotatedElement annotatedElement, Consumer<T> consumer) {
        doResolveAnnotations(annotatedElement).stream()
                .filter(annotationFilter)
                .forEach(annotation -> consumer.accept(annotationClass.cast(annotation)));
    }

    /**
     * Do the actually resolving action.
     *
     * @param annotatedElement the annotated element (class or method)
     * @return the List of annotations
     */
    protected abstract List<Annotation> doResolveAnnotations(AnnotatedElement annotatedElement);
}
