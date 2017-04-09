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

import java.lang.reflect.Method;

/**
 * The callback handler for a single impl resolved by {@link AnnotationTraverser}. The impl could be
 *
 * Do not implement the interface extends {@link AnnotationHandlerBase}.
 *
 * @param <T> the impl type
 */
public interface AnnotationHandler<T> {

    /**
     * Handle a class impl.
     *
     * @param clazz      the clazz where the impl has been declared.
     * @param annotation the class impl.
     */
    void handleClassAnnotation(Class<?> clazz, T annotation);

    /**
     * Handle a method impl.
     *
     * @param method     the method where the impl has been declared.
     * @param annotation the class impl.
     */
    void handleMethodAnnotation(Method method, T annotation);

    void __do_not_implement___use_AnnotationHandlerBase();
}
