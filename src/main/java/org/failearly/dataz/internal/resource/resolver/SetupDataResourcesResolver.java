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
package org.failearly.dataz.internal.resource.resolver;

import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotationTraverser;
import org.failearly.dataz.internal.common.annotation.traverser.TraverseDepth;
import org.failearly.dataz.internal.common.annotation.traverser.TraverseStrategy;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static org.failearly.dataz.internal.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;

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
