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
 * @see #getResourceNamesFromAnnotation(Annotation)
 */
public abstract class GenericDataResourcesFactory<T extends Annotation> extends TypedDataResourcesFactory<T> {

    protected final ResourceType resourceType;

    protected GenericDataResourcesFactory(Class<T> annotationClass, ResourceType resourceType) {
        super(annotationClass);
        this.resourceType = resourceType;
    }

    @Override
    protected final List<DataResource> doCreateDataResourcesFromClass(Class<?> clazz, T annotation, TemplateObjects templateObjects) {
        final List<DataResource> dataResources = new LinkedList<>();
        if (userHasProvidedResourceNames(annotation)) {
            dataResources.addAll(doResolveDataResourcesFromResourceNames(annotation, clazz, templateObjects));
        } else {
            dataResources.add(
                    createDataResourceFromAnnotation(                                    //
                            annotation,                                                  //
                            clazz,                                                       //
                            createDefaultResourceNameFromClass(clazz, this.resourceType),//
                            templateObjects                                              //
                    )                                                                    //
            );
        }
        return dataResources;
    }

    @Override
    protected final List<DataResource> doCreateDataResourcesFromMethod(Method method, T annotation, TemplateObjects templateObjects) {
        final Class<?> declaringClass = method.getDeclaringClass();
        final List<DataResource> dataResources = new LinkedList<>();
        if (userHasProvidedResourceNames(annotation)) {
            dataResources.addAll(doResolveDataResourcesFromResourceNames(annotation, declaringClass, templateObjects));
        } else {
            dataResources.add(
                    createDataResourceFromAnnotation( //
                            annotation,                                                     //
                            declaringClass,                                                 //
                            createDefaultResourceNameFromMethod(method, this.resourceType), //
                            templateObjects                                                 //
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
                                                                           Class<?> clazz,      //
                                                                           TemplateObjects templateObjects  //
    ) {
        final String[] resourceNames = getResourceNamesFromAnnotation(annotation);
        final List<DataResource> dataResources = new ArrayList<>();
        for (String resourceName : resourceNames) {
            dataResources.add(createDataResourceFromAnnotation(annotation, clazz, resourceName, templateObjects));
        }

        return dataResources;
    }


    /**
     * Creates a default resource name from {@code clazz}.
     * <br><br>
     * Example: Given test class com.mycompany.project.module.MyTest
     * <br><br>
     * The result: MyTest.&lt;datastore-suffix&gt;}.
     * The {@literal <datastore-suffix>} will be generated from {@code dataStoreId} and {@code resourceType}.
     *
     * @param clazz    the class
     * @param resourceType the resource type
     * @return the default resource path
     */
    private static String createDefaultResourceNameFromClass(Class<?> clazz, ResourceType resourceType) {
        return clazz.getSimpleName() + resolveDataStoreSuffix(resourceType);
    }

    /**
     * Creates a default resource name from {@code method}.
     * <br><br>
     * Example: Given method com.mycompany.project.module.MyTest#myTestMethod
     * <br><br>
     * The result: {@code MyTest-myTestMethod.&lt;datastore-suffix&gt;}.
     * The {@literal <datastore-suffix>} will be generated from {@code dataStoreId} and {@code resourceType}.
     *
     * @param method   the method
     * @param resourceType the resource type
     * @return the default resource path
     */
    private static String createDefaultResourceNameFromMethod(Method method, ResourceType resourceType) {
        return method.getDeclaringClass().getSimpleName() + "-" + method.getName() + resolveDataStoreSuffix(resourceType);
    }

    private static String resolveDataStoreSuffix(ResourceType resourceType) {
        String suffix = resourceType.resolveDataStoreSuffix();
        if (!suffix.startsWith(".")) {
            suffix = "." + suffix;
        }
        return suffix;
    }


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
     * @param clazz         the test class
     * @param resourceName      the resource name
     * @param templateObjects the generators
     * @return the data resource object.
     */
    protected abstract DataResource createDataResourceFromAnnotation(T annotation, Class<?> clazz, String resourceName, TemplateObjects templateObjects);

}
