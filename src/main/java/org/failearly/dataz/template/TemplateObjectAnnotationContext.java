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
package org.failearly.dataz.template;

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
     * @return new annotation context.
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
     * @return new annotation context.
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
     * @return new annotation context.
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

    private ClassLoader getDeclaringClassLoader() {
        return getAnnotatedOrDeclaringClass().getClassLoader();
    }

    /**
     * Load a resource as {@link InputStream}.
     *
     * @param resource resource name
     *
     * @return the input stream or {@link Optional#empty()}
     */
    public final Optional<InputStream> loadResourceAsStream(String resource) {
        return Optional.ofNullable(getDeclaringClassLoader().getResourceAsStream(resource)).map(IOUtils::autoClose);
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
        return Optional.ofNullable(getDeclaringClassLoader().getResource(resource));
    }
}
