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
 * AnnotationHandlerBase is the base class for handling traversed annotations.
 */
public abstract class AnnotationHandlerBase<T extends Annotation> implements AnnotationHandler<T> {
    @Override
    public void handleClassAnnotation(Class<?> clazz, T annotation) {
        handleAnnotation(annotation);
    }

    @Override
    public void handleMethodAnnotation(Method method, T annotation) {
        handleAnnotation(annotation);
    }

    /**
     * Handles any annotation, if the associated element is not important.
     * @param annotation the annotation.
     */
    public void handleAnnotation(T annotation) {
        /* no op */
    }

    @Override
    public final void __do_not_implement___use_AnnotationHandlerBase() {
        throw new UnsupportedOperationException("Don't use __do_not_implement___use_AnnotationHandlerBase()");
    }
}
