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

package org.failearly.dataz.internal.common.annotation.traverser;

import org.failearly.dataz.internal.common.annotation.resolver.AnnotationResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * AnnotationTraverserBase is the base class for {@link AnnotationTraverser} implementations.
 */
abstract class AnnotationTraverserBase<T extends Annotation> implements AnnotationTraverser<T>, MetaAnnotationTraverser<T> {
    private final AnnotationResolver<T> annotationResolver;
    private final ClassesCollector classesCollector;
    private final TraverserType traverserType;

    AnnotationTraverserBase(AnnotationResolver<T> annotationResolver, ClassesCollector classesCollector, TraverserType traverserType) {
        this.annotationResolver = annotationResolver;
        this.classesCollector = classesCollector;
        this.traverserType = traverserType;
    }


    @Override
    public final void traverse(Class<?> clazz, AnnotationHandler<T> annotationHandler) {
        classesCollector.collect(clazz)
                .forEach(currentClass->annotationResolver.resolveClassAnnotations(currentClass, annotationHandler));
    }

    @Override
    public void traverse(Class<?> clazz, MetaAnnotationHandler<T> annotationHandler) {
        classesCollector.collect(clazz)
                .forEach(currentClass->annotationResolver.resolveClassMetaAnnotations(currentClass, annotationHandler));
    }

    final void doStandardMethodTraverse(Method method, AnnotationHandler<T> annotationHandler) {
        annotationResolver.resolveMethodAnnotations(method, annotationHandler);
    }

    final void doMetaMethodTraverse(Method method, MetaAnnotationHandler<T> annotationHandler) {
        annotationResolver.resolveMethodMetaAnnotations(method, annotationHandler);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public final boolean anyAnnotationAvailable(Class<?> clazz) {
        final CountingHandler<T> counter = new CountingHandler<>();
        switch ( this.traverserType ) {
            case META:
                traverse(clazz, (MetaAnnotationHandler<T>) counter);
                return counter.isAvailable();
            case STANDARD:
                traverse(clazz, (AnnotationHandler<T>) counter);
                return counter.isAvailable();
            default:
                assert false: "Traverser Type is not of META or STANDARD";
        }
        return false;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public final boolean anyAnnotationAvailable(Method method) {
        final CountingHandler<T> counter = new CountingHandler<>();
        switch ( this.traverserType ) {
            case META:
                traverse(method, (MetaAnnotationHandler<T>) counter);
                return counter.isAvailable();
            case STANDARD:
                traverse(method, (AnnotationHandler<T>) counter);
                return counter.isAvailable();
            default:
                assert false: "Traverser Type is not of META or STANDARD";
        }
        return false;
    }

    private static class CountingHandler<XT extends Annotation> implements MetaAnnotationHandler<XT>, AnnotationHandler<XT> {
        private int counter=0;

        private void handleAnnotation() {
            counter++;
        }

        boolean isAvailable() {
            return counter>0;
        }

        @Override
        public void handleMetaClassAnnotation(Class<?> clazz, Annotation annotation, XT metaAnnotation) {
            handleAnnotation();
        }

        @Override
        public void handleMetaMethodAnnotation(Method method, Annotation annotation, XT metaAnnotation) {
            handleAnnotation();
        }

        @Override
        public void handleClassAnnotation(Class<?> clazz, XT annotation) {
            handleAnnotation();
        }

        @Override
        public void handleMethodAnnotation(Method method, XT annotation) {
            handleAnnotation();
        }

        @Override
        public void __do_not_implement___use_AnnotationHandlerBase() {
        }

        @Override
        public void __do_not_implement___use_MetaAnnotationHandlerBase() {
        }

    }

    private static class StandardCountingHandler<XT extends Annotation> extends AnnotationHandlerBase<XT> {
        private int counter=0;

        @Override
        public void handleAnnotation(Annotation annotation) {
            counter++;
        }

        boolean isAvailable() {
            return counter>0;
        }

    }

}
