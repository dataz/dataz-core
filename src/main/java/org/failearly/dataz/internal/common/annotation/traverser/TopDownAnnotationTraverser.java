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

package org.failearly.dataz.internal.common.annotation.traverser;

import org.failearly.dataz.internal.common.annotation.resolver.AnnotationResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Top down implementation of AnnotationTraverser.
 */
final class TopDownAnnotationTraverser<T extends Annotation> extends AnnotationTraverserBase<T> {

    TopDownAnnotationTraverser(AnnotationResolver<T> annotationResolver, TraverseDepth traverseDepth, TraverserType traverserType) {
        super(annotationResolver, new ClassesCollector(TraverseStrategy.TOP_DOWN, traverseDepth), traverserType);
    }

    @Override
    public void traverse(Method method, AnnotationHandler<T> annotationHandler) {
        doStandardMethodTraverse(method, annotationHandler);
        traverse(method.getDeclaringClass(), annotationHandler);
    }
    @Override
    public void traverse(Method method, MetaAnnotationHandler<T> annotationHandler) {
        doMetaMethodTraverse(method, annotationHandler);
        traverse(method.getDeclaringClass(), annotationHandler);
    }
}
