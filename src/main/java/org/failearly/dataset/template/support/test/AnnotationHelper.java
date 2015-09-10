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

package org.failearly.dataset.template.support.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * AnnotationHelper is responsible for ...
 */
@SuppressWarnings("unused")
public final class AnnotationHelper<T extends Annotation> {
    private final Class<T> annotationClass;
    private T[] annotations;


    private AnnotationHelper(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public static <T extends Annotation> AnnotationHelper<T> createAnnotationHelper(Class<T> annotationClass) {
        return new AnnotationHelper<>(annotationClass);
    }

    public AnnotationHelper<T> withFixtureClass(Class<?> aFixtureClass) {
        this.annotations = aFixtureClass.getAnnotationsByType(annotationClass);
        return this;
    }

    public AnnotationHelper<T> withMethod(Method aTestMethod) {
        this.annotations = aTestMethod.getAnnotationsByType(annotationClass);
        return this;
    }

    public boolean hasAnnotations() {
        return this.annotations.length > 0;
    }

    public List<T> getAnnotations() {
        return Arrays.asList(this.annotations);
    }

    public T getAnnotation(int index) {
        return annotations[index];
    }

    public String resolveElementValue(int index, String element) {
        return doResolveElementValue(getAnnotation(index), element);
    }

    private String doResolveElementValue(T annotation, String element) {
        try {
            return String.valueOf(annotationClass.getMethod(element).invoke(annotation));
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception while resolving element value", e);
        }
    }

    public <R> R resolveElementValue(int index, String element, Class<R> valueClass) {
        return doResolveElementValue(getAnnotation(index), element, valueClass);
    }

    private <R> R doResolveElementValue(T annotation, String element, Class<R> valueClass) {
        try {
            return valueClass.cast(annotationClass.getMethod(element).invoke(annotation));
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception while resolving element value", e);
        }
    }

    public <R> List<R> resolveElementValues(String element, Class<R> valueClass) {
        return annotationsAsStream().map(annotation -> doResolveElementValue(annotation, element, valueClass)).collect(Collectors.toList());
    }

    private Stream<T> annotationsAsStream() {
        return Arrays.stream(this.annotations);
    }
}
