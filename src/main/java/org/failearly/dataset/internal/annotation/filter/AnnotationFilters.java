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
package org.failearly.dataset.internal.annotation.filter;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

import static org.failearly.dataset.internal.annotation.AnnotationUtils.getAnnotationClass;

/**
 * AnnotationFilters is responsible for ...
 */
public final class AnnotationFilters {
    private AnnotationFilters() {
    }

    /**
     * Creates an annotation filter based on {@link Class#isInstance(Object)}.
     *
     * @param annotationClass the annotation class.
     * @param <T>             the annotation type
     * @return an annotation filter.
     */
    public static <T extends Annotation> AnnotationFilter isInstance(Class<T> annotationClass) {
        return new PredicateToAnnotationFilter(annotationClass::isInstance);
    }

    /**
     * Accept any annotation (place holder where is nothing to filter).
     *
     * @param <T>             the annotation type
     * @return an annotation filter.
     */
    public static <T extends Annotation> AnnotationFilter acceptAll() {
        return new PredicateToAnnotationFilter((a) -> true);
    }

    public static <T extends Annotation> AnnotationFilter hasMetaAnnotation(Class<T> metaAnnotationClass) {
        return new PredicateToAnnotationFilter((a) -> hasMetaAnnotation(metaAnnotationClass, a));
    }

    private static <T extends Annotation> boolean hasMetaAnnotation(Class<T> metaAnnotationClass, Annotation annotation) {
        return getAnnotationClass(annotation)
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
