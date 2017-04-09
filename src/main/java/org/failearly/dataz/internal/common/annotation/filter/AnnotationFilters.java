/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.internal.common.annotation.filter;

import org.failearly.dataz.internal.common.annotation.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

/**
 * AnnotationFilters is responsible for ...
 */
public final class AnnotationFilters {
    private AnnotationFilters() {
    }

    /**
     * Creates an impl filter based on {@link Class#isInstance(Object)}.
     *
     * @param annotationClass the impl class.
     * @param <T>             the impl type
     * @return an impl filter.
     */
    public static <T extends Annotation> AnnotationFilter isInstance(Class<T> annotationClass) {
        return new PredicateToAnnotationFilter(annotationClass::isInstance);
    }

    /**
     * Accept any impl (place holder where is nothing to filter).
     *
     * @param <T>             the impl type
     * @return an impl filter.
     */
    public static <T extends Annotation> AnnotationFilter acceptAll() {
        return new PredicateToAnnotationFilter((a) -> true);
    }

    public static <T extends Annotation> AnnotationFilter hasMetaAnnotation(Class<T> metaAnnotationClass) {
        return new PredicateToAnnotationFilter((a) -> hasMetaAnnotation(metaAnnotationClass, a));
    }

    private static <T extends Annotation> boolean hasMetaAnnotation(Class<T> metaAnnotationClass, Annotation annotation) {
        return AnnotationUtils.getAnnotationClass(annotation)
                .isAnnotationPresent(metaAnnotationClass);
    }


    private static class PredicateToAnnotationFilter implements AnnotationFilter {
        private final Predicate<Annotation> predicate;

        private PredicateToAnnotationFilter(Predicate<Annotation> predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean test(Annotation annotation) {
            return predicate.test(annotation);
        }
    }

}
