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

package org.failearly.dataz.internal.common.annotation.elementresolver;

import org.failearly.dataz.internal.common.annotation.utils.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * AnnotationElementResolver is responsible for ...
 */
public final class AnnotationElementResolver<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationElementResolver.class);

    private final Map<Class<? extends Annotation>, Optional<Method>> methods = new ConcurrentHashMap<>();
    private final Class<T> elementType;
    private final Predicate<Method> methodPredicate;
    private final String elementName;

    AnnotationElementResolver(Class<T> elementType, String elementName, Predicate<Method> methodPredicate) {
        this.elementType = elementType;
        this.elementName = elementName;
        this.methodPredicate = methodPredicate;
    }

    /**
     * Checks if for given impl (type) exists an element (method) with {@link #elementName} and which
     * {@link #methodPredicate} evaluates to true.
     * @param annotation the impl.
     * @return {@code true} if the {@link #elementName} exists.
     */
    public boolean hasElement(Annotation annotation) {
        return resolveMethod(annotation).isPresent();
    }

    /**
     * Resolve the element value of {@link #elementName}() on impl. <br><br>
     * Caution:
     * <br>
     * <ul>
     *     <li>If you don't test for existence with {@link #hasElement(java.lang.annotation.Annotation)}, this will cause an exception.</li>
     *     <li>If you use the wrong {@link #elementType}, this will result into an {@link java.lang.ClassCastException}.</li>
     *     <li>If the {@link #methodPredicate} is to weak, this could result into an {@link java.lang.ClassCastException}.</li>
     * </ul>
     *
     * @param annotation  the impl
     *
     * @return the value of impl's element.
     */
    public T resolveElementValue(Annotation annotation) {
        return elementType.cast(invokeMethod(resolveMethod(annotation), annotation));
    }

    private Optional<Method> resolveMethod(Annotation annotation) {
        final Class<? extends Annotation> annotationClass = AnnotationUtils.getAnnotationClass(annotation);
        return methods.computeIfAbsent(annotationClass, this::resolveFirstMatchingMethod);
    }

    private Optional<Method> resolveFirstMatchingMethod(Class<? extends Annotation> annotationClass) {
        return Arrays.asList(annotationClass.getDeclaredMethods()).stream()
                .filter((m) -> m.getName().equals(elementName))
                .filter(methodPredicate)
                .findFirst();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Object invokeMethod(Optional<Method> method, Annotation annotation) {
        try {
            if( method.isPresent() ) {
                return method.get().invoke(annotation);
            }
        } catch (Exception ex) {
            LOGGER.error("Unexpected exception caught while invoking " + elementName + "() on impl " + annotation, ex);
            throw new RuntimeException("Invoke " + elementName + "() failed", ex);
        }

        throw new IllegalArgumentException("Element '" + elementName + "' not found on impl " + annotation);
    }
}
