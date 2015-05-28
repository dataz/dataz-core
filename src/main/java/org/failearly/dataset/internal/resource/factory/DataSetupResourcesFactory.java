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
package org.failearly.dataset.internal.resource.factory;

import org.failearly.dataset.DataSetup;
import org.failearly.dataset.annotations.DataSetupResourceFactoryDefinition;
import org.failearly.dataset.internal.resource.ResourceType;
import org.failearly.dataset.internal.template.TemplateObjects;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.DataResourceBuilder;
import org.failearly.dataset.resource.GenericDataResourcesFactory;

/**
 * DataSetupResourcesFactory creates {@link DataResource}s from a {@link DataSetup} annotation.
 *
 * @see DataSetup
 * @see DataSetupResourceFactoryDefinition
 */
public final class DataSetupResourcesFactory extends GenericDataResourcesFactory<DataSetup> {

    public DataSetupResourcesFactory() {
        super(DataSetup.class, ResourceType.SETUP);
    }

    @Override
    protected DataResource createDataResourceFromAnnotation(DataSetup annotation, Class<?> testClass, String resourceName, TemplateObjects generatorCreators) {
        return DataResourceBuilder.createBuilder(testClass)         //
                .withDataSetName(annotation.name())                 //
                .withDataStoreId(annotation.datastore())            //
                .withResourceName(resourceName)                     //
                .withFailOnError(annotation.failOnError())          //
                .withTransactional(annotation.transactional())      //
                .withTemplateObjects(generatorCreators)             //
                .mandatory()                                        //
                .build();
    }

    @Override
    protected String getDataStoreIdFromAnnotation(DataSetup annotation) {
        return annotation.datastore();
    }

    @Override
    protected String[] getResourceNamesFromAnnotation(DataSetup annotation) {
        return annotation.value();
    }


}
