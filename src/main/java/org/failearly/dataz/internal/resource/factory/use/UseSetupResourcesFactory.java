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

package org.failearly.dataz.internal.resource.factory.use;

import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotationHandler;
import org.failearly.dataz.internal.resource.resolver.DataSetupResourceAnnotationHandler;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;

import java.util.List;

/**
 * UseSetupResourcesFactory resolves and creates {@link DataResource}s from {@link org.failearly.dataz.Use} impl.
 */
public final class UseSetupResourcesFactory extends UseResourcesFactoryBase<DataResourcesFactory.SetupDefinition> {
    public UseSetupResourcesFactory() {
        super(SetupDefinition.class);
    }

    @Override
    protected MetaAnnotationHandler<SetupDefinition> metaAnnotationHandler(List<DataResource> dataResources, TemplateObjects templateObjects) {
        return new DataSetupResourceAnnotationHandler(templateObjects, dataResources);
    }

}
