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

package org.failearly.dataset.internal.resource.factory.dataset;

import org.failearly.dataset.DataSet;
import org.failearly.dataset.internal.resource.ResourceType;
import org.failearly.dataset.internal.template.TemplateObjects;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.DataResourceBuilder;
import org.failearly.dataset.resource.GenericDataResourcesFactory;

/**
 * DataSetResourcesFactoryBase is the base class for {@link DataSet} based {@link DataResource}s factory classes.
 */
abstract class ResourcesFactoryBase extends GenericDataResourcesFactory<DataSet> {

    protected ResourcesFactoryBase(ResourceType resourceType) {
        super(DataSet.class, resourceType);
    }

    @Override
    protected final String getDataStoreIdFromAnnotation(DataSet annotation) {
        return annotation.datastore();
    }

    protected final DataResource createDataResourceFromAnnotation(DataSet annotation, Class<?> testClass, String resourceName, TemplateObjects generatorCreators) {
        return DataResourceBuilder.createBuilder(testClass)        //
                .withResourceType(this.resourceType)           //
                .withDataSetName(annotation.name())            //
                .withDataStoreId(annotation.datastore())       //
                .withResourceName(resourceName)                //
                .withFailOnError(annotation.failOnError())     //
                .withTransactional(annotation.transactional()) //
                .withTemplateObjects(generatorCreators)      //
                .build();
    }

}
