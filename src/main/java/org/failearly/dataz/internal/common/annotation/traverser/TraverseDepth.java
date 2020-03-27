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

import java.lang.reflect.Method;

/**
 * TraverseDepth contains the predefined traversing depths. The direction will be defined by {@link TraverseStrategy}.
 * <br><br>
 * <b>Remarks</b>:
 * <br><br>
 * <ul>
 *    <li>In case of {@link java.lang.annotation.Repeatable} annotations, the annotations will keep there order, even
 * by using {@link TraverseStrategy#BOTTOM_UP}.</li>
 *    <li>Using {@link AnnotationTraverser#traverse(Class, AnnotationHandler)} </li>
 * </ul>
 *
 *
 * @see AnnotationTraverserBuilder#withTraverseDepth(TraverseDepth)
 */
public enum TraverseDepth {
    /**
     * Only the method object will be used. Useless on class objects.
     *
     * @see AnnotationTraverser#traverse(Method, AnnotationHandler)
     */
    METHOD_ONLY(ClassesCollector.NO_CLASS_DEPTH),

    /**
     * The method and declaring class will be used.
     * The collected impl (in case of {@link TraverseStrategy#TOP_DOWN}) will be:
     * <br><br>
     * <ol>
     * <li>Method annotations (only visible by using {@link AnnotationTraverser#traverse(Method, AnnotationHandler)})</li>
     * <li>The annotations of the method's declaring class or the first class</li>
     * </ol>
     */
    DECLARED_CLASS(ClassesCollector.ONLY_DECLARED_CLASS_DEPTH),

    /**
     * The method, the declaring class and all super classes of the declaring class.
     * The collected impl (in case of {@link TraverseStrategy#TOP_DOWN}) will be:
     * <br><br>
     * <ol>
     * <li>Method annotations (only visible by using {@link AnnotationTraverser#traverse(Method, AnnotationHandler)})</li>
     * <li>The annotations of the method's declaring class or the first class</li>
     * <li>The annotations of the super classes</li>
     * </ol>
     */
    CLASS_HIERARCHY(ClassesCollector.CLASS_HIERARCHY_DEPTH),

    /**
     * The entire hierarchy (including interfaces). The method, the declaring class, all super classes of the declaring class and all interfaces
     * (including sub interfaces). The collected impl (in case of {@link TraverseStrategy#TOP_DOWN}) will be:
     * <br><br>
     * <ol>
     * <li>Method annotations (only visible by using {@link AnnotationTraverser#traverse(Method, AnnotationHandler)})</li>
     * <li>The annotations of the method's declaring class or the first class</li>
     * <li>The annotations of the super classes</li>
     * <li>The annotations of the interfaces</li>
     * </ol>
     * <br><br>
     * <b>Remark</b>: In case of {@link java.lang.annotation.Repeatable} annotations, the annotations will keep there order, even
     * by using {@link TraverseStrategy#BOTTOM_UP}.
     */
    HIERARCHY(ClassesCollector.CLASS_HIERARCHY_DEPTH, true);

    final int depth;
    final boolean useInterfaces;

    TraverseDepth(int depth, boolean useInterfaces) {
        this.depth = depth;
        this.useInterfaces = useInterfaces;
    }

    TraverseDepth(int depth) {
        this.depth = depth;
        this.useInterfaces = false;
    }

    public int getDepth() {
        return depth;
    }

    public boolean useInterface() {
        return useInterfaces;
    }
}
