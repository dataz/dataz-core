/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.internal.resource.factory.use;

import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotationHandler;
import org.failearly.dataz.internal.resource.resolver.DataCleanupResourceAnnotationHandler;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;

import java.util.Collections;
import java.util.List;

/**
 * UseCleanupResourcesFactory resolves and creates {@link DataResource}s from {@link org.failearly.dataz.Use} impl.
 */
public final class UseCleanupResourcesFactory extends UseResourcesFactoryBase<DataResourcesFactory.CleanupDefinition> {

    public UseCleanupResourcesFactory() {
        super(CleanupDefinition.class);
    }

    @Override
    protected MetaAnnotationHandler<CleanupDefinition> metaAnnotationHandler(List<DataResource> dataResources, TemplateObjects templateObjects) {
        return new DataCleanupResourceAnnotationHandler(templateObjects, dataResources);
    }

    @Override
    protected List<DataResource> doChangeOrder(List<DataResource> dataResources) {
        Collections.reverse(dataResources);
        return dataResources;
    }

}
