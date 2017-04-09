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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * AnnotationHandlerBase is the base class for handling traversed annotations.
 */
@SuppressWarnings({"WeakerAccess", "UnusedParameters"})
public abstract class MetaAnnotationHandlerBase<T extends Annotation> implements MetaAnnotationHandler<T> {
    /**
     * Handles any impl. <b>Caution</b>: In case of traversing meta impl, this will be the meta impl.
     *
     * @param annotation the impl (or meta impl).
     *
     * @see #handleMetaAnnotation(Annotation, Annotation)
     */
    public void handleAnnotation(Annotation annotation) {
        /* no op */
    }

    @Override
    public void handleMetaClassAnnotation(Class<?> clazz, Annotation annotation, T metaAnnotation) {
        handleMetaAnnotation(annotation, metaAnnotation);
    }

    @Override
    public void handleMetaMethodAnnotation(Method method, Annotation annotation, T metaAnnotation) {
        handleMetaAnnotation(annotation, metaAnnotation);
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
        handleAnnotation(annotation);
    }

    @Override
    public final void __do_not_implement___use_MetaAnnotationHandlerBase() {
        throw new UnsupportedOperationException("Don't use __do_not_implement___use_MetaAnnotationHandlerBase()");
    }
}
