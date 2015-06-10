/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.resource;

import org.failearly.dataset.internal.template.TemplateObjects;

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
        return doCreateDataResourcesFromTestClass(annotatedClass, annotationClass.cast(annotation), templateObjects);
    }

    @Override
    public final List<DataResource> createDataResources(Method annotatedMethod, Annotation annotation, TemplateObjects templateObjects) {
        return doCreateDataResourcesFromTestMethod(annotatedMethod, annotationClass.cast(annotation), templateObjects);
    }

    /**
     * Do create {@link DataResource}s from test class and annotation.
     *
     * @param testClass         the test class
     * @param annotation        the annotation
     * @param templateObjects   all template objects.
     * @return a list with all DataResource
     */
    protected List<DataResource> doCreateDataResourcesFromTestClass(Class<?> testClass, T annotation, TemplateObjects templateObjects) {
        return doCreateDataResources(annotation, templateObjects);
    }

    /**
     * Do create {@link DataResource}s from test method and annotation.
     *
     * @param testMethod        the test method
     * @param annotation        the annotation
     * @param templateObjects   all template objects.
     * @return a list with all DataResource
     */
    protected List<DataResource> doCreateDataResourcesFromTestMethod(Method testMethod, T annotation, TemplateObjects templateObjects)  {
        return doCreateDataResources(annotation, templateObjects);
    }

    /**
     * Do create {@link DataResource}s from annotation.
     *
     * @param annotation        the annotation
     * @param templateObjects   all template objects.
     * @return a list with all DataResource
     */
    protected List<DataResource> doCreateDataResources(T annotation, TemplateObjects templateObjects) {
        throw new UnsupportedOperationException("Please implement doCreateDataResources(), doCreateDataResourcesFromTestClass() or doCreateDataResourcesFromTestMethod()");
    }

}
