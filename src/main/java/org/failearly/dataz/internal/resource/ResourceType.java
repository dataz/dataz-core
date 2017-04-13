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

package org.failearly.dataz.internal.resource;

import org.failearly.dataz.config.DataSetProperties;

/**
 * ResourceType is responsible for ...
 */
public enum ResourceType {
    SETUP {
        @Override
        public String resolveDataStoreSuffix() {
            return DataSetProperties.getDefaultSetupSuffix();
        }

    },
    CLEANUP {
        @Override
        public String resolveDataStoreSuffix() {
            return DataSetProperties.getDefaultCleanupSuffix();
        }

    };

    public abstract String resolveDataStoreSuffix();

}
