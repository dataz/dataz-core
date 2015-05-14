/*
 * Copyright (c) 2009.
 *
 * Date: 14.05.15
 * 
 */
package org.failearly.dataset.internal.resource.factory;

import org.failearly.dataset.DataCleanup;
import org.failearly.dataset.DataCleanup;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.internal.resource.ResourceType;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.DataResourceBuilder;
import org.failearly.dataset.resource.DataResourcesFactory;
import org.failearly.dataset.resource.GenericDataResourcesFactory;

import java.util.List;

/**
 * DataCleanupResourcesFactory creates {@link DataResource}s from a {@link DataCleanup} annotation.
 */
public final class DataCleanupResourcesFactory extends GenericDataResourcesFactory<DataCleanup> {
    public DataCleanupResourcesFactory() {
        super(DataCleanup.class, ResourceType.CLEANUP);
    }

    @Override
    protected String getDataStoreIdFromAnnotation(DataCleanup annotation) {
        return annotation.datastore();
    }

    @Override
    protected String[] getResourceNamesFromAnnotation(DataCleanup annotation) {
        return annotation.value();
    }

    @Override
    protected DataResource createDataResourceFromAnnotation(DataCleanup annotation, Class<?> testClass, String resourceName, List<GeneratorCreator> generatorCreators) {
        return DataResourceBuilder.createBuilder(testClass)         //
                        .withDataSetName(annotation.name())                 //
                        .withDataStoreId(annotation.datastore())            //
                        .withResourceName(resourceName)                     //
                        .withFailOnError(annotation.failOnError())          //
                        .withTransactional(annotation.transactional())      //
                        .withGeneratorCreators(generatorCreators)           //
                        .mandatory()                                        //
                        .build();
    }
}
