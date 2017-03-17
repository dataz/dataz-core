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

package org.failearly.dataz.internal.resource.factory.dataset;

import org.failearly.dataz.DataSet;
import org.failearly.dataz.internal.resource.ResourceType;

/**
 * DataSetCleanupResourcesFactory creates Cleanup DataResource from annotation {@link DataSet}.
 */
public final class DataSetCleanupResourcesFactory extends DataSetResourcesFactoryBase {
    public DataSetCleanupResourcesFactory() {
        super(ResourceType.CLEANUP);
    }

    @Override
    protected String[] getResourceNamesFromAnnotation(DataSet annotation) {
        return annotation.cleanup();
    }
}
