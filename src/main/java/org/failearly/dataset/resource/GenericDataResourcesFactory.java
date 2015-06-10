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
import org.failearly.dataset.internal.resource.ResourceType;
import org.failearly.dataset.internal.template.TemplateObjects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.failearly.dataset.resource.ResourcePathUtils.createDefaultResourceNameFromTestClass;
import static org.failearly.dataset.resource.ResourcePathUtils.createDefaultResourceNameFromTestMethod;

/**
 * GenericResourcesFactory provides a generic/default implementation. Only some methods which base on the concrete knowledge of the annotation.
 *
 * @see #createDataResourceFromAnnotation(Annotation, Class, String, TemplateObjects)
 * @see #getDataStoreIdFromAnnotation(Annotation)
 * @see #getResourceNamesFromAnnotation(Annotation)
 */
public abstract class GenericDataResourcesFactory<T extends Annotation> extends TypedDataResourcesFactory<T> {

    protected final ResourceType resourceType;

    protected GenericDataResourcesFactory(Class<T> annotationClass, ResourceType resourceType) {
        super(annotationClass);
        this.resourceType = resourceType;
    }

    @Override
    protected final List<DataResource> doCreateDataResourcesFromTestClass(Class<?> testClass, T annotation, TemplateObjects templateObjects) {
        final List<DataResource> dataResources = new LinkedList<>();
        if (userHasProvidedResourceNames(annotation)) {
            dataResources.addAll(doResolveDataResourcesFromResourceNames(annotation, testClass, templateObjects));
        } else {
            dataResources.add(
                    createDataResourceFromAnnotation( //
                            annotation,                                                                                                     //
                            testClass,                                                                                                      //
                            createDefaultResourceNameFromTestClass(testClass, getDataStoreIdFromAnnotation(annotation), this.resourceType), //
                            templateObjects                                                                                               //
                    )
            );
        }
        return dataResources;
    }

    @Override
    protected final List<DataResource> doCreateDataResourcesFromTestMethod(Method testMethod, T annotation, TemplateObjects templateObjects) {
        final Class<?> testClass = testMethod.getDeclaringClass();
        final List<DataResource> dataResources = new LinkedList<>();
        if (userHasProvidedResourceNames(annotation)) {
            dataResources.addAll(doResolveDataResourcesFromResourceNames(annotation, testClass, templateObjects));
        } else {
            dataResources.add(
                    createDataResourceFromAnnotation( //
                            annotation,                                                                                                     //
                            testClass,                                                                                                      //
                            createDefaultResourceNameFromTestMethod(testMethod, getDataStoreIdFromAnnotation(annotation), this.resourceType), //
                            templateObjects                                                                                               //
                    )
            );
        }
        return dataResources;
    }

    private boolean userHasProvidedResourceNames(T annotation) {
        return 0 <  getResourceNamesFromAnnotation(annotation).length;
    }

    private List<DataResource> doResolveDataResourcesFromResourceNames(    //
                                                                           T annotation,            //
                                                                           Class<?> testClass,      //
                                                                           TemplateObjects templateObjects  //
    ) {
        final String[] resourceNames = getResourceNamesFromAnnotation(annotation);
        final List<DataResource> dataResources = new ArrayList<>();
        for (String resourceName : resourceNames) {
            dataResources.add(createDataResourceFromAnnotation(annotation, testClass, resourceName, templateObjects));
        }

        return dataResources;
    }

    /**
     * get the value for the data store id from annotation.
     *
     * @param annotation the annotation
     * @return the datastore id
     * @see DataSet#datastore()
     */
    protected abstract String getDataStoreIdFromAnnotation(T annotation);

    /**
     * Get the resource name(s) from annotation.
     *
     * @param annotation the annotation
     * @return all configured resource names.
     */
    protected abstract String[] getResourceNamesFromAnnotation(T annotation);


    /**
     * Create a {@link DataResource} from Annotation and test class. Typical this method uses {@link DataResourceBuilder}.
     *
     * @param annotation        the annotation
     * @param testClass         the test class
     * @param resourceName      the resource name
     * @param templateObjects the generators
     * @return the data resource object.
     */
    protected abstract DataResource createDataResourceFromAnnotation(T annotation, Class<?> testClass, String resourceName, TemplateObjects templateObjects);

}
