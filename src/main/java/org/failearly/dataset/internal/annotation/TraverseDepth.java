/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.failearly.dataset.internal.annotation;

import org.failearly.dataset.util.ClassesCollector;

/**
 * TraverseDepth contains the predefined traversing depths.
 *
 * @see AnnotationTraversers#createAnnotationTraverser(Class, TraverseStrategy, TraverseDepth)
 * @see AnnotationTraversers#createMetaAnnotationTraverser(Class, TraverseStrategy, TraverseDepth)
 */
public enum TraverseDepth {
    /**
     * Only the test method will be used.
     */
    METHOD_ONLY(ClassesCollector.NO_CLASS_DEPTH),
    /**
     * The method and declaring class.
     */
    DECLARING_CLASS(ClassesCollector.ONLY_DECLARING_CLASS_DEPTH),
    /**
     * The method, the declaring class and all super classes of the declaring class.
     */
    CLASS_HIERARCHY(ClassesCollector.ENTIRE_HIERARCHY_DEPTH);

    final int depth;
    TraverseDepth(int depth) {
        this.depth = depth;
    }

    int getDepth() {
        return depth;
    }
}
