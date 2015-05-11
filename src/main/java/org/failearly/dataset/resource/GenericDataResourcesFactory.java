/*
 * Copyright (c) 2009.
 *
 * Date: 10.05.15
 * 
 */
package org.failearly.dataset.resource;

import org.failearly.dataset.DataSet;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.internal.resource.ResourceType;

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
 * @see #createDataResourceFromAnnotation(Annotation, Class, String, List)
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
    protected final List<DataResource> doCreateDataResourcesFromTestClass(Class<?> testClass, T annotation, List<GeneratorCreator> generatorCreators) {
        final List<DataResource> dataResources = new LinkedList<>();
        if (userHasProvidedResourceNames(annotation)) {
            dataResources.addAll(doResolveDataResourcesFromResourceNames(annotation, testClass, generatorCreators));
        } else {
            dataResources.add(
                    createDataResourceFromAnnotation( //
                            annotation,                                                                                                     //
                            testClass,                                                                                                      //
                            createDefaultResourceNameFromTestClass(testClass, getDataStoreIdFromAnnotation(annotation), this.resourceType), //
                            generatorCreators                                                                                               //
                    )
            );
        }
        return dataResources;
    }

    @Override
    protected final List<DataResource> doCreateDataResourcesFromTestMethod(Method testMethod, T annotation, List<GeneratorCreator> generatorCreators) {
        final Class<?> testClass = testMethod.getDeclaringClass();
        final List<DataResource> dataResources = new LinkedList<>();
        if (userHasProvidedResourceNames(annotation)) {
            dataResources.addAll(doResolveDataResourcesFromResourceNames(annotation, testClass, generatorCreators));
        } else {
            dataResources.add(
                    createDataResourceFromAnnotation( //
                            annotation,                                                                                                     //
                            testClass,                                                                                                      //
                            createDefaultResourceNameFromTestMethod(testMethod, getDataStoreIdFromAnnotation(annotation), this.resourceType), //
                            generatorCreators                                                                                               //
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
                                                                           List<GeneratorCreator> generatorCreators  //
    ) {
        final String[] resourceNames = getResourceNamesFromAnnotation(annotation);
        final List<DataResource> dataResources = new ArrayList<>();
        for (String resourceName : resourceNames) {
            dataResources.add(createDataResourceFromAnnotation(annotation, testClass, resourceName, generatorCreators));
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
     * @param generatorCreators the generators
     * @return the data resource object.
     */
    protected abstract DataResource createDataResourceFromAnnotation(T annotation, Class<?> testClass, String resourceName, List<GeneratorCreator> generatorCreators);

}
