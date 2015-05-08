/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 marko (http://fail-early.com/contact)
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

import org.failearly.dataset.DataSet;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.internal.resource.ResourceType;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.DataResourceBuilder;

import java.util.List;

/**
 * DataSetSetupResourceFactory creates Setup DataResource from annotation {@link DataSet}.
 */
public final class DataSetCleanupResourcesFactory extends DataSetResourcesFactoryBase {
    public DataSetCleanupResourcesFactory() {
        super(ResourceType.CLEANUP);
    }

    @Override
    protected String[] resolveResourceNamesFromAnnotation(DataSet annotation) {
        return annotation.cleanup();
    }

    protected DataResource createDataResourceFromAnnotation(DataSet annotation, Class<?> testClass, String resourceName, List<GeneratorCreator> generatorCreators) {
        return DataResourceBuilder.createBuilder(testClass)        //
                    .optional()                                    //
                    .withDataSetName(annotation.name())            //
                    .withDataStoreId(annotation.datastore())       //
                    .withResourceName(resourceName)                //
                    .withFailOnError(annotation.failOnError())     //
                    .withTransactional(annotation.transactional()) //
                    .withGeneratorCreators(generatorCreators)      //
                .build();
    }
}
