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

package org.failearly.dataz.datastore;

import org.failearly.dataz.internal.common.proputils.PropertiesAccessor;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.resource.DataResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractDataStore is a base class to protect the implementations against changes of the interface {@link DataStore}.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractDataStore implements DataStore {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public Class<? extends NamedDataStore> getNamedDataStore() {
        LOGGER.warn("getNamedDataStore() not implemented");
        return NullNamedDataStore.class;
    }

    @Override
    public String getName() {
        LOGGER.warn("getName() not implemented");
        return "unknown";
    }

    @Override
    public String getId() {
        LOGGER.warn("getId() not implemented");
        return "unknown";
    }

    @Override
    public String getConfigFile() {
        LOGGER.warn("getConfig() not implemented");
        return "unknown";
    }

    @Override
    public PropertiesAccessor getProperties() {
        LOGGER.error("getProperties() not implemented");
        throw new UnsupportedOperationException("getProperties() not implemented");
    }

    @Override
    public void initialize() throws DataStoreException {
        LOGGER.error("initialize() not implemented");
        throw new UnsupportedOperationException("initialize() not implemented");
    }

    @Override
    public boolean hasTransactionalSupport() {
        LOGGER.warn("hasTransactionalSupport() not implemented");
        return false;
    }

    @Override
    public void applyDataResource(DataResource dataResource) {
        LOGGER.error("applyDataResource() not implemented");
        throw new UnsupportedOperationException("applyDataResource() not implemented");
    }

    @Override
    public void dispose() {
        LOGGER.error("dispose() not implemented");
        throw new UnsupportedOperationException("dispose() not implemented");
    }

    static final class NullNamedDataStore
            extends NamedDataStore {
    }

}
