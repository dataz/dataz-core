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

package org.failearly.dataz.resource;

import org.failearly.dataz.internal.template.TemplateObjects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * TypedDataResourcesFactoryBase is a typed base class for all {@link DataResourcesFactory} implementations. It's only responsibility is type safety.
 */
public abstract class TypedDataResourcesFactory<T extends Annotation> implements DataResourcesFactory {

    private final Class<T> annotationClass;

    protected TypedDataResourcesFactory(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public final List<DataResource> createDataResources(Class<?> annotatedClass, Annotation annotation, TemplateObjects templateObjects) {
        return doCreateDataResourcesFromClass(annotatedClass, annotationClass.cast(annotation), templateObjects);
    }

    @Override
    public final List<DataResource> createDataResources(Method annotatedMethod, Annotation annotation, TemplateObjects templateObjects) {
        return doCreateDataResourcesFromMethod(annotatedMethod, annotationClass.cast(annotation), templateObjects);
    }

    /**
     * Do create {@link DataResource}s from test class and impl.
     *
     * @param clazz         the test class
     * @param annotation        the impl
     * @param templateObjects   all template objects.
     * @return a list with all DataResource
     */
    protected List<DataResource> doCreateDataResourcesFromClass(Class<?> clazz, T annotation, TemplateObjects templateObjects) {
        return doCreateDataResources(annotation, templateObjects);
    }

    /**
     * Do create {@link DataResource}s from test method and impl.
     *
     * @param method        the test method
     * @param annotation        the impl
     * @param templateObjects   all template objects.
     * @return a list with all DataResource
     */
    protected List<DataResource> doCreateDataResourcesFromMethod(Method method, T annotation, TemplateObjects templateObjects)  {
        return doCreateDataResources(annotation, templateObjects);
    }

    /**
     * Do create {@link DataResource}s from impl.
     *
     * @param annotation        the impl
     * @param templateObjects   all template objects.
     * @return a list with all DataResource
     */
    protected List<DataResource> doCreateDataResources(T annotation, TemplateObjects templateObjects) {
        throw new UnsupportedOperationException("Please implement doCreateDataResources(), doCreateDataResourcesFromClass() or doCreateDataResourcesFromMethod()");
    }

}
