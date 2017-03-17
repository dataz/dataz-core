/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.internal.resource.factory;

import org.failearly.dataz.DataCleanup;
import org.failearly.dataz.internal.resource.ResourceType;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourceBuilder;
import org.failearly.dataz.resource.GenericDataResourcesFactory;

import java.util.List;

/**
 * DataCleanupResourcesFactory creates {@link DataResource}s from a {@link DataCleanup} annotation.
 */
public final class DataCleanupResourcesFactory extends GenericDataResourcesFactory<DataCleanup> {
    public DataCleanupResourcesFactory() {
        super(DataCleanup.class, ResourceType.CLEANUP);
    }

    @Override
    protected String[] getResourceNamesFromAnnotation(DataCleanup annotation) {
        return annotation.value();
    }

    @Override
    protected List<DataResource> createDataResourceFromAnnotation(DataCleanup annotation, Class<?> testClass, String resourceName, TemplateObjects templateObjects) {
        return DataResourceBuilder.createBuilder(testClass)         //
                .withDataSetName(annotation.name())                 //
                .withNamedDataStore(annotation.datastores())        //
                .withResourceName(resourceName)                     //
                .withFailOnError(annotation.failOnError())          //
                .withTransactional(annotation.transactional())      //
                .withTemplateObjects(templateObjects)               //
                .mandatory()                                        //
                .build();
    }
}
