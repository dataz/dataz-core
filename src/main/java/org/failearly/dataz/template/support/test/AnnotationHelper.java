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

package org.failearly.dataz.template.support.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * AnnotationHelper is responsible for ...
 */
@SuppressWarnings("unused")
final class AnnotationHelper<T extends Annotation> {
    private final Class<T> annotationClass;
    private List<T> classAnnotations;
    private Map<String, List<T>> methodAnnotationMap;
    // private Map<String, Method> methods;


    private AnnotationHelper(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    static <T extends Annotation> AnnotationHelper<T> createAnnotationHelper(Class<T> annotationClass) {
        return new AnnotationHelper<>(annotationClass);
    }

    AnnotationHelper<T> withFixtureClass(Class<?> aFixtureClass) {
        this.classAnnotations = Arrays.asList(aFixtureClass.getAnnotationsByType(annotationClass));
        this.methodAnnotationMap = Stream.of(aFixtureClass.getDeclaredMethods()).collect(
                toMap(
                        Method::getName,
                        (Method method)->Arrays.asList(method.getAnnotationsByType(annotationClass))
                )
        );
        return this;
    }

    boolean hasAnyAnnotations() {
        return ! ( this.classAnnotations.isEmpty() && this.methodAnnotationMap.isEmpty() );
    }

    T getAnnotationFromClass(int index) {
        return classAnnotations.get(index);
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
}
