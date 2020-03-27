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

/**
* TraverseDirection defines strategies, how the {@link AnnotationTraverser} will work.
 *
 * @see org.failearly.dataz.internal.common.annotation.traverser.AnnotationTraverserBuilder#annotationTraverser(Class)
 * @see org.failearly.dataz.internal.common.annotation.traverser.AnnotationTraverserBuilder#metaAnnotationTraverser(Class)
*/
public enum TraverseStrategy {
    /**
     * Traverse from current method or class to the base classes and then interfaces.
     * So the traversing order is:
     * <br><br>
     * <ol>
     *    <li>method (optional)</li>
     *    <li>declaring class or class</li>
     *    <li>base class(es)</li>
     *    <li>interfaces</li>
     * </ol>
     */
    TOP_DOWN,

    /**
     * Traverse from interfaces, base classes to current method or class. So it's a depth first strategy.
     * So the traversing order is:
     * <br><br>
     * <ol>
     *    <li>interfaces</li>
     *    <li>base class(es)</li>
     *    <li>declaring class or class</li>
     *    <li>method (optional)</li>
     * </ol>
     */
    BOTTOM_UP
}
