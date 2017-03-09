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
