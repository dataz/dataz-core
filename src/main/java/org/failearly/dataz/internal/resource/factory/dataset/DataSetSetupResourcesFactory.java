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

package org.failearly.dataz.internal.resource.factory.dataset;

import org.failearly.dataz.DataSet;
import org.failearly.dataz.internal.resource.ResourceType;

/**
 * DataSetSetupResourceFactory creates Setup DataResource from impl {@link DataSet}.
 */
public final class DataSetSetupResourcesFactory extends DataSetResourcesFactoryBase {
    public DataSetSetupResourcesFactory() {
        super(ResourceType.SETUP);
    }

    @Override
    protected String[] getResourceNamesFromAnnotation(DataSet annotation) {
        return annotation.setup();
    }

}
