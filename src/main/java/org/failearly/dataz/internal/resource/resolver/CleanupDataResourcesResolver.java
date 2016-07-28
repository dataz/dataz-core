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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.failearly.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;
import static org.failearly.dataz.resource.DataResourcesFactory.CleanupDefinition;

/**
 * SetupDataResourcesResolver is responsible for ...
 */
final class CleanupDataResourcesResolver implements DataResourcesResolver {
    private final MetaAnnotationTraverser<CleanupDefinition> cleanupResourcesTraverser;

    CleanupDataResourcesResolver(TraverseDepth traverseDepth) {
        cleanupResourcesTraverser = metaAnnotationTraverser(CleanupDefinition.class) //
                .withTraverseStrategy(TraverseStrategy.BOTTOM_UP)                                         //
                .withTraverseDepth(traverseDepth)                                                         //
                .build();
    }

    @Override
    public List<DataResource> resolveFromMethod(Method method, TemplateObjects templateObjects) {
        final DataCleanupResourceAnnotationHandler annotationHandler = new DataCleanupResourceAnnotationHandler(templateObjects);
        cleanupResourcesTraverser.traverse(method, annotationHandler);
        final List<DataResource> dataResources=annotationHandler.getDataResourceList();
        Collections.reverse(dataResources);
        return dataResources;
    }

    @Override
    public List<DataResource> resolveFromClass(Class<?> clazz, TemplateObjects templateObjects) {
        final List<DataResource> dataResources=new LinkedList<>();
        cleanupResourcesTraverser.traverse(clazz, new DataCleanupResourceAnnotationHandler(templateObjects, dataResources));
        Collections.reverse(dataResources);
        return dataResources;
    }
}
