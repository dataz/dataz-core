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

package org.failearly.dataz.internal.resource.factory.dataset;

import org.failearly.dataz.DataSet;
import org.failearly.dataz.internal.resource.ResourceType;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourceBuilder;
import org.failearly.dataz.resource.GenericDataResourcesFactory;

import java.util.List;

/**
 * DataSetResourcesFactoryBase is the base class for {@link DataSet} based {@link DataResource}s factory classes.
 */
abstract class DataSetResourcesFactoryBase extends GenericDataResourcesFactory<DataSet> {

    protected DataSetResourcesFactoryBase(ResourceType resourceType) {
        super(DataSet.class, resourceType);
    }

    protected final List<DataResource> createDataResourceFromAnnotation(DataSet annotation, Class<?> testClass, String resourceName, TemplateObjects templateObjects) {
        return DataResourceBuilder.createBuilder(testClass)        //
                .withResourceType(this.resourceType)           //
                .withDataSetName(annotation.name())            //
                .withNamedDataStore(annotation.datastores())            //
                .withResourceName(resourceName)                //
                .withFailOnError(annotation.failOnError())     //
                .withTransactional(annotation.transactional()) //
                .withTemplateObjects(templateObjects)      //
                .build();
    }

}
