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

package org.failearly.dataset.internal.annotation;

import org.failearly.dataset.internal.annotation.resolver.AnnotationResolver;
import org.failearly.dataset.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Top down implementation of AnnotationTraverser.
 */
final class BottomUpAnnotationTraverser<T extends Annotation> extends AnnotationTraverserBase<T> {

    BottomUpAnnotationTraverser(AnnotationResolver<T> annotationResolver) {
        super(annotationResolver);
    }

    @Override
    public void traverse(Method method, AnnotationHandler<T> annotationHandler) {
        traverse(method.getDeclaringClass(), annotationHandler);
        annotationResolver.resolveMethodAnnotations(method, annotationHandler);
    }

    protected List<Class<?>> doCollectClasses(Class<?> clazz) {
        return ClassUtils.collectClassesReverted(clazz);
    }
}
