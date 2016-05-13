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

package org.failearly.dataz.resource;

import org.failearly.dataz.DataSet;
import org.failearly.dataz.internal.resource.ResourceType;
import org.failearly.dataz.internal.template.TemplateObjects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
     * Creates a default resource name from {@code testClass}.
     * <br><br>
     * Example: Given test class com.mycompany.project.module.MyTest
     * <br><br>
     * The result: MyTest.&lt;datastore-suffix&gt;}.
     * The {@literal <datastore-suffix>} will be generated from {@code dataStoreId} and {@code resourceType}.
     *
     * @param testClass    the test class
     * @param dataStoreId  the ID of an data store
     * @param resourceType the resource type
     * @return the default resource path
     */
    private static String createDefaultResourceNameFromTestClass(Class<?> testClass, String dataStoreId, ResourceType resourceType) {
        return testClass.getSimpleName() + resolveDataStoreSuffix(dataStoreId, resourceType);
    }

    /**
     * Creates a default resource name from {@code testMethod}.
     * <br><br>
     * Example: Given test method com.mycompany.project.module.MyTest#myTestMethod
     * <br><br>
     * The result: {@code MyTest-myTestMethod.&lt;datastore-suffix&gt;}.
     * The {@literal <datastore-suffix>} will be generated from {@code dataStoreId} and {@code resourceType}.
     *
     * @param testMethod   the test method
     * @param dataStoreId  the data store
     * @param resourceType the resource type
     * @return the default resource path
     */
    private static String createDefaultResourceNameFromTestMethod(Method testMethod, String dataStoreId, ResourceType resourceType) {
        return testMethod.getDeclaringClass().getSimpleName() + "-" + testMethod.getName() + resolveDataStoreSuffix(dataStoreId, resourceType);
    }

    private static String resolveDataStoreSuffix(String dataStoreId, ResourceType resourceType) {
        String suffix = resourceType.resolveDataStoreSuffix(dataStoreId);
        if (!suffix.startsWith(".")) {
            suffix = "." + suffix;
        }
        return suffix;
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
