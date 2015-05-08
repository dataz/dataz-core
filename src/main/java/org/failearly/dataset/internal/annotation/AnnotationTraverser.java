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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * AnnotationTraverser is responsible for traversing annotations.
 * <p>
 * Caution: If a annotation is {@link java.lang.annotation.Repeatable} the traverser will
 * resolve the containing annotation, if there is more then one.
 *
 * @see org.failearly.dataset.internal.annotation.AnnotationTraversers
 */
public interface AnnotationTraverser<T extends Annotation> {
    /**
     * Traverse using the {@code method} instance. The method, the declaring class and all super classes will be visited.
     * @param method the method instance.
     * @param annotationHandler the annotation handler.
     */
    void traverse(Method method, AnnotationHandler<T> annotationHandler);

    /**
     * Traverse using {@code clazz}. The class and all super classes will be visited.
     * @param clazz the class object.
     * @param annotationHandler the annotation handler.
     */
    void traverse(Class<?> clazz, AnnotationHandler<T> annotationHandler);

}
