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
 * Top down implementation of AnnotationTraverser.
 */
final class BottomUpAnnotationTraverser<T extends Annotation> extends AnnotationTraverserBase<T> {

    BottomUpAnnotationTraverser(AnnotationResolver<T> annotationResolver, TraverseDepth traverseDepth, TraverserType traverserType) {
        super(annotationResolver, new ClassesCollector(TraverseStrategy.BOTTOM_UP, traverseDepth), traverserType);
    }

    @Override
    public void traverse(Method method, AnnotationHandler<T> annotationHandler) {
        traverse(method.getDeclaringClass(), annotationHandler);
        doStandardMethodTraverse(method, annotationHandler);
    }

    @Override
    public void traverse(Method method, MetaAnnotationHandler<T> annotationHandler) {
        traverse(method.getDeclaringClass(), annotationHandler);
        doMetaMethodTraverse(method, annotationHandler);
    }
}
