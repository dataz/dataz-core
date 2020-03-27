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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * AnnotationHandlerBase is the base class for handling traversed annotations.
 */
@SuppressWarnings({"WeakerAccess", "UnusedParameters"})
public abstract class AnnotationHandlerBase<T extends Annotation> implements AnnotationHandler<T> {
    @Override
    public void handleClassAnnotation(Class<?> clazz, T annotation) {
        handleAnnotation(annotation);
    }

    @Override
    public void handleMethodAnnotation(Method method, T annotation) {
        handleAnnotation(annotation);
    }

    /**
     * Handles any impl. <b>Caution</b>: In case of traversing meta impl, this will be the meta impl.
     *
     * @param annotation the impl (or meta impl).
     *
     * @see #handleMetaAnnotation(Annotation, Annotation)
     */
    public void handleAnnotation(T annotation) {
        /* no op */
    }

    /**
     * Handles any impl (which annotated with {@code metaAnnotation}.
     *
     * @param annotation the impl.
     * @param metaAnnotation the associated meta impl.
     *
     * @see AnnotationTraverserBuilder#metaAnnotationTraverser(Class)
     */
    public void handleMetaAnnotation(Annotation annotation, T metaAnnotation) {
        handleAnnotation(metaAnnotation);
    }

    @Override
    public final void __do_not_implement___use_AnnotationHandlerBase() {
        throw new UnsupportedOperationException("Don't use __do_not_implement___use_AnnotationHandlerBase()");
    }
}
