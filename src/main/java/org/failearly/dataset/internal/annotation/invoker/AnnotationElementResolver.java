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

import org.failearly.dataset.internal.annotation.AnnotationUtils;
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
     * Checks if for given annotation (type) exists an element (method) with {@link #elementName} and which
     * {@link #methodPredicate} evaluates to true.
     * @param annotation the annotation.
     * @return {@code true} if the {@link #elementName} exists.
     */
    public boolean hasElement(Annotation annotation) {
        return resolveMethod(annotation).isPresent();
    }

    /**
     * Resolve the element value of {@link #elementName}() on annotation. <br><br>
     * Caution:
     * <br>
     * <ul>
     *     <li>If you don't test for existence with {@link #hasElement(java.lang.annotation.Annotation)}, this will cause an exception.</li>
     *     <li>If you use the wrong {@link #elementType}, this will result into an {@link java.lang.ClassCastException}.</li>
     *     <li>If the {@link #methodPredicate} is to weak, this could result into an {@link java.lang.ClassCastException}.</li>
     * </ul>
     *
     * @param annotation  the annotation
     *
     * @return the value of annotation's element.
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

    private Object invokeMethod(Optional<Method> method, Annotation annotation) {
        try {
            return method.get().invoke(annotation);
        } catch (Exception ex) {
            LOGGER.error("Unexpected exception caught while invoking " + elementName + "() on annotation " + annotation, ex);
            throw new RuntimeException("Invoke " + elementName + "() failed", ex);
        }
    }
}
