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

import org.failearly.dataset.DataSet;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.internal.resource.ResourceType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * DataResourcesFactoryBase is a typed base class for all {@link DataResourcesFactory} implementations.
 */
public abstract class DataResourcesFactoryBase<T extends Annotation> implements DataResourcesFactory {

    protected final ResourceType resourceType;
    private final Class<T> annotationClass;

    protected DataResourcesFactoryBase(Class<T> annotationClass, ResourceType resourceType) {
        this.annotationClass = annotationClass;
        this.resourceType = resourceType;
    }

    @Override
    public final List<DataResource> createDataResources(Class<?> annotatedClass, Annotation annotation, List<GeneratorCreator> generatorCreators) {
        return resolveAllDataResourcesFromTestClass(annotatedClass, annotationClass.cast(annotation), generatorCreators);
    }

    /**
     * Resolve all {@link DataResource}s from a given test class.
     * @param annotatedClass the test class
     * @param annotation the annotation
     * @param generatorCreators all known generator (creator)s.
     * @return a list with all DataResource
     */
    protected abstract List<DataResource> resolveAllDataResourcesFromTestClass(Class<?> annotatedClass, T annotation, List<GeneratorCreator> generatorCreators);

    @Override
    public final List<DataResource> createDataResources(Method annotatedMethod, Annotation annotation, List<GeneratorCreator> generatorCreators) {
        return resolveAllDataResourcesFromTestMethod(annotatedMethod, annotationClass.cast(annotation), generatorCreators);
    }

    protected abstract List<DataResource> resolveAllDataResourcesFromTestMethod(Method annotatedMethod, T annotation, List<GeneratorCreator> generatorCreators);

    /**
     * Create a {@link DataResource} from Annotation and test class.
     * @param annotation the annotation
     * @param testClass the test class
     * @param resourceName the resource name
     * @param generatorCreators the generators
     * @return the data resource object.
     */
    protected  abstract DataResource createDataResourceFromAnnotation(T annotation, Class<?> testClass, String resourceName, List<GeneratorCreator> generatorCreators);

    /**
     * Create a {@link DataResource} from Annotation and test method.
     * @param annotation the annotation
     * @param testMethod the test method
     * @param resourceName the resource name
     * @param generatorCreators the generators
     * @return the data resource object.
     */
    protected final DataResource createDataResourceFromAnnotation(T annotation, Method testMethod, String resourceName, List<GeneratorCreator> generatorCreators) {
        return createDataResourceFromAnnotation(annotation, testMethod.getDeclaringClass(), resourceName, generatorCreators);
    }

    /**
     * Resolve the (raw) resource name from given annotation.
     *
     * @param annotation the annotation
     *
     * @return all configured resource names.
     *
     * @see DataSet#cleanup()
     * @see DataSet#setup()
     */
    protected abstract String[] resolveResourceNamesFromAnnotation(T annotation);
}
