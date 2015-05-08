/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.internal.annotation.resolver;

import org.failearly.dataset.internal.annotation.AnnotationHandler;
import org.failearly.dataset.internal.annotation.filter.AnnotationFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;

/**
 * AnnotationResolverBase is responsible for ...
 */
abstract class AnnotationResolverBase<T extends Annotation> implements AnnotationResolver<T> {
    private final AnnotationFilter annotationFilter;
    final Class<T> annotationClass;

    AnnotationResolverBase(Class<T> annotationClass, AnnotationFilter annotationFilter) {
        this.annotationFilter = annotationFilter;
        this.annotationClass = annotationClass;
    }

    @Override
    public final void resolveClassAnnotations(Class<?> clazz, AnnotationHandler<T> annotationHandler) {
        doResolveAnnotations(clazz, (a) -> annotationHandler.handleClassAnnotation(clazz, a));
    }

    @Override
    public final void resolveMethodAnnotations(Method method, AnnotationHandler<T> annotationHandler) {
        doResolveAnnotations(method, (a)->annotationHandler.handleMethodAnnotation(method, a));
    }

    private void doResolveAnnotations(AnnotatedElement annotatedElement, Consumer<T> consumer) {
        for (Annotation annotation : resolveAnnotations(annotatedElement) ) {
            if( annotationFilter.test(annotation) ) {
                consumer.accept(annotationClass.cast(annotation));
            }
        }
    }

    protected abstract List<Annotation> resolveAnnotations(AnnotatedElement annotatedElement);
}
