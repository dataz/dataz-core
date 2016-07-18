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
