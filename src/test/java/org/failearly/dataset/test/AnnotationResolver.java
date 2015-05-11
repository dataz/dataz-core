/*
 * Copyright (c) 2009.
 *
 * Date: 10.05.15
 * 
 */
package org.failearly.dataset.test;

import org.failearly.dataset.internal.util.BuilderBase;

import java.lang.annotation.Annotation;

/**
 * AnnotationResolver is responsible for ...
 */
public final class AnnotationResolver<T extends Annotation> extends BuilderBase<T> {

    private final Class<T> annotationClass;
    private Class<?> testClass;
    private String methodName;

    private AnnotationResolver(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public static <T extends Annotation> AnnotationResolver<T> annotationResolver(Class<T> annotationClass) {
        return new AnnotationResolver<T>(annotationClass);
    }

    public AnnotationResolver<T> fromClass(Class<?> testClass) {
        this.testClass = testClass;
        return this;
    }

    public AnnotationResolver<T> fromMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @Override
    protected T doBuild() throws RuntimeException {
        T annotation = null;
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
