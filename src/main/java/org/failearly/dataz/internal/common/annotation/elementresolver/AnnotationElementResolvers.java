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

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * AnnotationElementResolvers creates {@link AnnotationElementResolver} instances.
 */
public final class AnnotationElementResolvers {

    /**
     * Creates an AnnotationElementResolver.
     *
     * @param elementType the element type (thus {@link java.lang.reflect.Method#getReturnType()}.
     * @param elementName the name of the element (thus {@link java.lang.reflect.Method#getName()}.
     * @param methodPredicate any additional predicate which filters the methods (the elementName) will already be used.
     *
     * @param <T> the element's type.
     * @return a new instance of AnnotationElementResolver.
     */
    public static <T> AnnotationElementResolver<T> createResolver(Class<T> elementType, String elementName, Predicate<Method> methodPredicate) {
        return new AnnotationElementResolver<>(elementType, elementName, methodPredicate);
    }

    /**
     * Convenient factory method for {@literal createResolver(elementType,elementName,m->m.getReturnType().isAssignableFrom(elementType))}.
     *
     * @param elementType the element type (thus {@link java.lang.reflect.Method#getReturnType()}.
     * @param elementName the name of the element (thus {@link java.lang.reflect.Method#getName()}.
     *
     * @param <T> the element's type.
     * @return a new instance of AnnotationElementResolver.
     */
    @SuppressWarnings("JavaDoc")
    public static <T> AnnotationElementResolver<T> createResolver(Class<T> elementType, String elementName) {
        return createResolver(elementType, elementName, m->m.getReturnType().isAssignableFrom(elementType));
    }
}
