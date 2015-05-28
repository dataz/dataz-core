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
import java.util.List;

/**
 * AnnotationTraverserBase is the base class for {@link AnnotationTraverser} implementations.
 */
abstract class AnnotationTraverserBase<T extends Annotation> implements AnnotationTraverser<T> {
    final AnnotationResolver<T> annotationResolver;
    private final ClassesCollector classesCollector;

    AnnotationTraverserBase(AnnotationResolver<T> annotationResolver, ClassesCollector classesCollector) {
        this.annotationResolver = annotationResolver;
        this.classesCollector = classesCollector;
    }


    @Override
    public final void traverse(Class<?> clazz, AnnotationHandler<T> annotationHandler) {
        final List<Class<?>> classes = classesCollector.collect(clazz);
        for (Class<?> currClass : classes) {
            annotationResolver.resolveClassAnnotations(currClass, annotationHandler);
        }
    }

    protected final void doTraverse(Method method, AnnotationHandler<T> annotationHandler) {
        annotationResolver.resolveMethodAnnotations(method, annotationHandler);
    }

}
