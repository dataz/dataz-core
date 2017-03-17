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

package org.failearly.dataz.test;

import org.failearly.common.builder.BuilderBase;

import java.lang.annotation.Annotation;

/**
 * AnnotationResolver is responsible for ...
 */
public final class AnnotationInstanceResolver<T extends Annotation> extends BuilderBase<T> {

    private final Class<T> annotationClass;
    private Class<?> testClass;
    private String methodName;

    private AnnotationInstanceResolver(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public static <T extends Annotation> AnnotationInstanceResolver<T> annotationResolver(Class<T> annotationClass) {
        return new AnnotationInstanceResolver<>(annotationClass);
    }

    public AnnotationInstanceResolver<T> fromClass(Class<?> testClass) {
        this.testClass = testClass;
        return this;
    }

    public AnnotationInstanceResolver<T> fromMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @Override
    protected T doBuild() throws RuntimeException {
        T annotation;
        try {
            if (methodName == null) {
                annotation = testClass.getAnnotation(annotationClass);
            } else {
                annotation = testClass.getMethod(methodName).getAnnotation(annotationClass);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unknown method '" + methodName + "' on " + testClass);
        }

        if (annotation == null) {
            throw new RuntimeException("Can't find annotation of " + annotationClass);
        }

        return annotation;
    }

    @Override
    protected void checkMandatoryFields() {
        checkMandatoryField(annotationClass, "annotationClass");
        checkMandatoryField(testClass, "testClass");
    }
}
