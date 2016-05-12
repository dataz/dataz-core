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

package org.failearly.dataset.internal.resource.factory.use;

import org.failearly.common.annotation.traverser.MetaAnnotationHandler;
import org.failearly.dataset.annotations.DataSetupResourceFactoryDefinition;
import org.failearly.dataset.internal.model.DataSetupResourceAnnotationHandler;
import org.failearly.dataset.internal.template.TemplateObjects;
import org.failearly.dataset.resource.DataResource;

import java.util.List;

/**
 * UseSetupResourcesFactory resolves and creates {@link DataResource}s from {@link org.failearly.dataset.Use} annotation.
 */
public final class UseSetupResourcesFactory extends ResourcesFactoryBase<DataSetupResourceFactoryDefinition> {
    public UseSetupResourcesFactory() {
        super(DataSetupResourceFactoryDefinition.class);
    }

    @Override
    protected MetaAnnotationHandler<DataSetupResourceFactoryDefinition> metaAnnotationHandler(List<DataResource> dataResources, TemplateObjects templateObjects) {
        return new DataSetupResourceAnnotationHandler(dataResources, templateObjects);
    }

}
