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

package org.failearly.dataz.internal.resource.resolver;

import org.failearly.dataz.internal.common.classutils.ObjectCreatorUtil;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;

import java.util.List;

/**
 * DataSetupResourceAnnotationHandler creates an DataResourceFactory from meta impl
 * {@link DataResourcesFactory.CleanupDefinition}.
 */
public final class DataCleanupResourceAnnotationHandler extends DataResourceAnnotationHandlerBase<DataResourcesFactory.CleanupDefinition> {

    public DataCleanupResourceAnnotationHandler(TemplateObjects templateObjects, List<DataResource> dataResourceList) {
        super(templateObjects, dataResourceList);
    }

    public DataCleanupResourceAnnotationHandler(TemplateObjects templateObjects) {
        super(templateObjects);
    }

    @Override
    protected DataResourcesFactory createDataResourceFactory(DataResourcesFactory.CleanupDefinition metaAnnotation) {
        return ObjectCreatorUtil.createInstance(metaAnnotation.value());
    }
}
