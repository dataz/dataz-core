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
package org.failearly.dataz.internal.resource.resolver;

import org.failearly.common.annotation.traverser.MetaAnnotationTraverser;
import org.failearly.common.annotation.traverser.TraverseDepth;
import org.failearly.common.annotation.traverser.TraverseStrategy;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static org.failearly.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;

/**
 * SetupDataResourcesResolver is responsible for ...
 */
final class SetupDataResourcesResolver implements DataResourcesResolver {
    private final MetaAnnotationTraverser<DataResourcesFactory.SetupDefinition> setupResourcesTraverser;

    SetupDataResourcesResolver(TraverseDepth traverseDepth) {
        setupResourcesTraverser = metaAnnotationTraverser(DataResourcesFactory.SetupDefinition.class)                                     //
                .withTraverseStrategy(TraverseStrategy.BOTTOM_UP)                                  //
                .withTraverseDepth(traverseDepth)                                        //
                .build();
    }

    @Override
    public List<DataResource> resolveFromMethod(Method method, TemplateObjects templateObjects) {
        final DataSetupResourceAnnotationHandler annotationHandler = new DataSetupResourceAnnotationHandler(templateObjects);
        setupResourcesTraverser.traverse(method, annotationHandler);
        return annotationHandler.getDataResourceList();
    }

    @Override
    public List<DataResource> resolveFromClass(Class<?> clazz, TemplateObjects templateObjects) {
        final List<DataResource> dataResources=new LinkedList<>();
        setupResourcesTraverser.traverse(clazz, new DataSetupResourceAnnotationHandler(templateObjects, dataResources));
        return dataResources;
    }
}
