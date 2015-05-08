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

import java.lang.reflect.Method;

/**
 * The callback handler for a single annotation resolved by {@link AnnotationTraverser}. The annotation could be
 *
 * Do not implement the interface extends {@link AnnotationHandlerBase}.
 *
 * @param <T> the annotation type
 */
public interface AnnotationHandler<T> {

    /**
     * Handle a class annotation.
     *
     * @param clazz      the clazz where the annotation has been declared.
     * @param annotation the class annotation.
     */
    void handleClassAnnotation(Class<?> clazz, T annotation);

    /**
     * Handle a method annotation.
     *
     * @param method     the method where the annotation has been declared.
     * @param annotation the class annotation.
     */
    void handleMethodAnnotation(Method method, T annotation);

    void __do_not_implement___use_AnnotationHandlerBase();
}
