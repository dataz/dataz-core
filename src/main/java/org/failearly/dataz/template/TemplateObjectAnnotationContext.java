/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */
package org.failearly.dataz.template;

import org.failearly.dataz.internal.common.resource.ResourcePathUtils;
import org.failearly.dataz.internal.util.IOUtils;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Optional;

/**
 * TemplateObjectAnnotationContext will be used by {@link TemplateObject} for resolving resources of the _Template Object Annotation_.
 *
 * Use either {@link #createAnnotationContext(Class)} or {@link #createAnnotationContext(Method)} for creating the
 * context objects.
 */
public abstract class TemplateObjectAnnotationContext {

    private static final TemplateObjectAnnotationContext TEMPLATE_OBJECT_ANNOTATION_CONTEXT = new TemplateObjectAnnotationContext() {
    };

    private TemplateObjectAnnotationContext() {}

    /**
     * Factory method for {@link TemplateObjectAnnotationContext}.
     *
     * @param annotatedMethod using a method object
     *
     * @return new impl context.
     */
    public static TemplateObjectAnnotationContext createAnnotationContext(final Method annotatedMethod) {
        return new TemplateObjectAnnotationContext() {
            @Override
            public Optional<Method> getAnnotatedMethod() {
                return Optional.of(annotatedMethod);
            }
        };
    }

    /**
     * Factory method for {@link TemplateObjectAnnotationContext}.
     *
     * @param annotatedClazz using a class object
     *
     * @return new impl context.
     */
    public static TemplateObjectAnnotationContext createAnnotationContext(final Class<?> annotatedClazz) {
        return new TemplateObjectAnnotationContext() {
            @Override
            public Optional<Class<?>> getAnnotatedClass() {
                return Optional.of(annotatedClazz);
            }
        };
    }

    /**
     * Factory method for {@link TemplateObjectAnnotationContext}.
     *
     * @param annotatedElement using a annotated element
     *
     * @return new impl context.
     */
    public static TemplateObjectAnnotationContext createByAnnotatedElement(final AnnotatedElement annotatedElement) {
        if( annotatedElement instanceof Class<?>)
            return TemplateObjectAnnotationContext.createAnnotationContext((Class<?>)annotatedElement);
        if( annotatedElement instanceof Method)
            return TemplateObjectAnnotationContext.createAnnotationContext((Method) annotatedElement);

        return TEMPLATE_OBJECT_ANNOTATION_CONTEXT;
    }



    /**
     * The TOA has been applied on a class object, then this method should be implemented.
     *
     * @return The annotated class or {@link Optional#empty()}
     */
    public Optional<Class<?>> getAnnotatedClass() {
        return Optional.empty();
    }

    /**
     * The TOA has been applied on a method object, then this method should be implemented.
     *
     * @return The annotated method or {@link Optional#empty()}
     */
    public Optional<Method> getAnnotatedMethod() {
        return Optional.empty();
    }

    /**
     * Returns the class object which is closest to the actually annotated element.
     *
     * This could be either
     * - The annotated class itself or
     * - The declaring class of the method object
     *
     * @return a class object.
     *
     * @see #getAnnotatedClass()
     * @see #getAnnotatedMethod()
     */
    public final Class<?> getAnnotatedOrDeclaringClass() {
        return getAnnotatedClass()
                    .orElseGet(() -> //
                        getAnnotatedMethod() //
                        .map(Method::getDeclaringClass) //
                        .orElseThrow(()->new IllegalStateException("Neither annotated class or method are implemented")) //
                    );
    }

    /**
     * Load a resource as {@link InputStream}.
     *
     * @param resource resource name
     *
     * @return the input stream or {@link Optional#empty()}
     */
    public final Optional<InputStream> loadResourceAsStream(String resource) {
        final String fullQualifiedResourcePath = fullQualifiedResourcePath(resource);
        return Optional.ofNullable(getAnnotatedOrDeclaringClass().getResourceAsStream(fullQualifiedResourcePath))
            .map(IOUtils::autoClose);
    }

    /**
     * Load a resource as {@link Reader}.
     *
     * @param resource resource name
     *
     * @return the reader or {@link Optional#empty()}
     */
    public final Optional<Reader> loadResourceAsReader(String resource) {
        return loadResourceAsStream(resource).map(IOUtils::toReader);
    }

    /**
     * Load a resource as {@link URL}.
     *
     * @param resource resource name
     *
     * @return the URL of the resource or {@link Optional#empty()}
     */
    public final Optional<URL> loadResourceAsUrl(String resource) {
        final String fullQualifiedResourcePath = fullQualifiedResourcePath(resource);
        return Optional.ofNullable(getAnnotatedOrDeclaringClass().getResource(fullQualifiedResourcePath));
    }

    private String fullQualifiedResourcePath(String resource) {
        return ResourcePathUtils.resourcePath(resource, getAnnotatedOrDeclaringClass());
    }
}
