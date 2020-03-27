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

package org.failearly.dataz.internal.common.annotation.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * AnnotationUtils provides some basic utility methods for annotations.
 */
public final class AnnotationUtils {
    private AnnotationUtils() {
    }

    /**
     * Resolve the impl's class.
     *
     * @param annotation the impl
     *
     * @return the class object of the impl.
     */
    public static Class<? extends Annotation> getAnnotationClass(Annotation annotation) {
        return annotation.annotationType();
    }

    /**
     * Get the meta impl from given impl.
     *
     * @param <T>                 the expect meta impl
     * @param metaAnnotationClass the meta impl class
     * @param annotation          the impl
     *
     * @return the meta impl
     */
    public static <T extends Annotation> T getMetaAnnotation(Class<T> metaAnnotationClass, Annotation annotation) {
        return getAnnotationClass(annotation).getAnnotation(metaAnnotationClass);
    }

    public static <T> T resolveValueOfAnnotationAttribute(Annotation annotation, String attributeName, Class<T> targetType) {
        final Class<? extends Annotation> annotationClass=getAnnotationClass(annotation);
        try {
            final Method method=annotationClass.getMethod(attributeName);
            return targetType.cast(method.invoke(annotation));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(
                "Can't access or resolve value of attribute '" + attributeName + "()' " +
                "of impl @" + annotationClass.getSimpleName()
            );
        }
    }
}
