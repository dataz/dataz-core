/*
 * Copyright (c) 2009.
 *
 * Date: 31.05.16
 * 
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
