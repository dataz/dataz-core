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

import org.failearly.common.classutils.ObjectCreatorUtil;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;

import java.util.List;

import static org.failearly.dataz.resource.DataResourcesFactory.*;

/**
* DataSetupResourceAnnotationHandler creates an DataResourceFactory from meta annotation
 * {@link SetupDefinition}.
*/
public final class DataSetupResourceAnnotationHandler extends DataResourceAnnotationHandlerBase<SetupDefinition> {

    public DataSetupResourceAnnotationHandler(TemplateObjects templateObjects, List<DataResource> dataResourceList) {
        super(templateObjects, dataResourceList);
    }

    public DataSetupResourceAnnotationHandler(TemplateObjects templateObjects) {
        super(templateObjects);
    }

    @Override
    protected DataResourcesFactory createDataResourceFactory(SetupDefinition metaAnnotation) {
        return ObjectCreatorUtil.createInstance(metaAnnotation.value());
    }
}
