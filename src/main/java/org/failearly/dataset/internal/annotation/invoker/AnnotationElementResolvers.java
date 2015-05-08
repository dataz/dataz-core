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
package org.failearly.dataset.internal.annotation.invoker;

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * AnnotationElementResolvers is responsible for ...
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
