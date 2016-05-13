/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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

package org.failearly.dataz.template.support.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * AnnotationHelper is responsible for ...
 */
@SuppressWarnings("unused")
final class AnnotationHelper<T extends Annotation> {
    private final Class<T> annotationClass;
    private T[] annotations;
    private Map<String, List<T>> methodAnnotationMap;
    // private Map<String, Method> methods;


    private AnnotationHelper(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    static <T extends Annotation> AnnotationHelper<T> createAnnotationHelper(Class<T> annotationClass) {
        return new AnnotationHelper<>(annotationClass);
    }

    AnnotationHelper<T> withFixtureClass(Class<?> aFixtureClass) {
        this.annotations = aFixtureClass.getAnnotationsByType(annotationClass);
        methodAnnotationMap = Stream.of(aFixtureClass.getDeclaredMethods()).collect(
                toMap(
                        Method::getName,
                        (Method method)->Arrays.asList(method.getAnnotationsByType(annotationClass))
                )
        );
        return this;
    }

    boolean hasAnnotations() {
        return this.annotations.length > 0;
    }

    List<T> getAnnotations() {
        return Arrays.asList(this.annotations);
    }

    T getAnnotation(int index) {
        if( ! (index < annotations.length) ) {
            throw new IllegalArgumentException("index >= " + annotations.length);
        }
        return annotations[index];
    }

    T getAnnotationFromMethod(String methodName, int index) {
        final List<T> ts = methodAnnotationMap.get(methodName);
        if( ts==null  ) {
            throw new IllegalArgumentException("Unknown method " + methodName);
        }
        if( ! (index < ts.size()) ) {
            throw new IllegalArgumentException("index >= " + ts.size());
        }
        return ts.get(index);
    }

    T getFirstAnnotationFromMethod(String methodName) {
        return getAnnotationFromMethod(methodName, 0);
    }

    String resolveElementValue(int index, String element) {
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
        return Arrays.stream(this.annotations)
                .map(annotation -> doResolveElementValue(annotation, element, valueClass)).collect(Collectors.toList());
    }

}
