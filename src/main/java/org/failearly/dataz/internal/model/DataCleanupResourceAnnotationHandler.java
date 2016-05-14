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

package org.failearly.dataz.internal.model;

import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;
import org.failearly.common.test.ObjectCreator;

import java.util.List;

/**
 * DataSetupResourceAnnotationHandler creates an DataResourceFactory from meta annotation
 * {@link DataResourcesFactory.CleanupDefinition}.
 */
public final class DataCleanupResourceAnnotationHandler extends DataResourceAnnotationHandlerBase<DataResourcesFactory.CleanupDefinition> {

    public DataCleanupResourceAnnotationHandler(List<DataResource> dataResourceList, TemplateObjects templateObjects) {
        super(templateObjects, dataResourceList);
    }

    @Override
    protected DataResourcesFactory createDataResourceFactory(DataResourcesFactory.CleanupDefinition metaAnnotation) {
        return ObjectCreator.createInstance(metaAnnotation.value());
    }
}
