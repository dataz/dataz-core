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

import org.failearly.dataset.internal.annotation.resolver.AnnotationResolver;
import org.failearly.dataset.util.ClassesCollector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Top down implementation of AnnotationTraverser.
 */
final class BottomUpAnnotationTraverser<T extends Annotation> extends AnnotationTraverserBase<T> {

    BottomUpAnnotationTraverser(AnnotationResolver<T> annotationResolver, TraverseDepth traverseDepth) {
        super(annotationResolver, new ClassesCollector(ClassesCollector.Order.BOTTOM_UP, traverseDepth.getDepth()));
    }

    @Override
    public void traverse(Method method, AnnotationHandler<T> annotationHandler) {
        traverse(method.getDeclaringClass(), annotationHandler);
        doTraverse(method, annotationHandler);
    }
}
