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

import org.failearly.dataz.common.Tests;
import org.failearly.dataz.DataSetup;
import org.failearly.dataz.internal.resource.ResourceType;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourceBuilder;
import org.failearly.dataz.resource.GenericDataResourcesFactory;

import java.util.List;

/**
 * DataSetupResourcesFactory creates {@link DataResource}s from a {@link DataSetup} impl.
 *
 * @see DataSetup
 * @see SetupDefinition
 */
@Tests("org.failearly.dataz.internal.resource.factory.DataSetupResourcesFactoryTest")
public final class DataSetupResourcesFactory extends GenericDataResourcesFactory<DataSetup> {

    public DataSetupResourcesFactory() {
        super(DataSetup.class, ResourceType.SETUP);
    }

    @Override
    protected List<DataResource> createDataResourceFromAnnotation(DataSetup annotation, Class<?> clazz, String resourceName, TemplateObjects templateObjects) {
        return DataResourceBuilder.createBuilder(clazz)             //
                .withDataSetName(annotation.name())                 //
                .withNamedDataStore(annotation.datastores())        //
                .withResourceName(resourceName)                     //
                .withFailOnError(annotation.failOnError())          //
                .withTransactional(annotation.transactional())      //
                .withTemplateObjects(templateObjects)               //
                .mandatory()                                        //
                .build();
    }

    @Override
    protected String[] getResourceNamesFromAnnotation(DataSetup annotation) {
        return annotation.value();
    }

}
