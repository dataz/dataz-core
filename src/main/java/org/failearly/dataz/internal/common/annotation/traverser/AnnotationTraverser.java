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
 * AnnotationTraverser is responsible for traversing annotations.
 * <p>
 * Caution: If a impl is {@link java.lang.annotation.Repeatable} the traverser will
 * resolve the containing impl, if there is more then one.
 *
 * @see AnnotationTraverserBuilder#annotationTraverser(Class)
 */
public interface AnnotationTraverser<T extends Annotation> {
    /**
     * Traverse using the {@code method} instance. The method, the declaring class and all super classes will be visited.
     * @param method the method instance.
     * @param annotationHandler the impl handler.
     */
    void traverse(Method method, AnnotationHandler<T> annotationHandler);

    /**
     * Traverse using {@code clazz}. The class and all super classes will be visited.
     * @param clazz the class object.
     * @param annotationHandler the impl handler.
     */
    void traverse(Class<?> clazz, AnnotationHandler<T> annotationHandler);

    /**
     * Check if any impl (of specified type) is available on {@code method} and so on depending of the current
     * {@link TraverseDepth}.
     *
     * @param method the method to be checked
     * @return  if the impl is available returns {@code true} otherwise {@code false}.
     */
    boolean anyAnnotationAvailable(Method method);

    /**
     * Check if any impl (of specified type) is available on {@code clazz} and so on depending of the current
     * {@link TraverseDepth}.
     *
     * @param clazz the class to be checked
     * @return  if the impl is available returns {@code true} otherwise {@code false}.
     */
    boolean anyAnnotationAvailable(Class<?> clazz);


}
