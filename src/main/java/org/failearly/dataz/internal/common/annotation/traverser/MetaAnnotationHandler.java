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
 * The callback handler for a single impl resolved by {@link AnnotationTraverser}. The impl could be
 *
 * Do not implement the interface extends {@link AnnotationHandlerBase}.
 *
 * @param <T> the impl type
 */
public interface MetaAnnotationHandler<T extends Annotation> {

    /**
     * Handle a class impl and it's meta impl.
     *
     * @param clazz      the clazz where the impl has been declared.
     * @param annotation the impl (which are annotated with {@code metaAnnotation}.
     * @param metaAnnotation the meta impl.
     */
    void handleMetaClassAnnotation(Class<?> clazz, Annotation annotation, T metaAnnotation);

    /**
     * Handle a method impl.
     *
     * @param method      the clazz where the impl has been declared.
     * @param annotation the impl (which are annotated with {@code metaAnnotation}.
     * @param metaAnnotation the meta impl.
     */
    void handleMetaMethodAnnotation(Method method, Annotation annotation, T metaAnnotation);

    void __do_not_implement___use_MetaAnnotationHandlerBase();
}
