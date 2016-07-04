/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.failearly.dataz.datastore;

import org.failearly.common.proputils.PropertiesAccessor;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.internal.model.AtomicTest;
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
    public final void setup(AtomicTest testMethod) throws DataStoreException {
        LOGGER.error("setup() not implemented");
        throw new UnsupportedOperationException("setup() not implemented");
    }

    @Override
    public final void cleanup(AtomicTest testMethod) {
        LOGGER.error("cleanup() not implemented");
        throw new UnsupportedOperationException("cleanup() not implemented");
    }

    @Override
    public void cleanupDataStore() {
        LOGGER.error("cleanupDataStore() not implemented");
        throw new UnsupportedOperationException("cleanupDataStore() not implemented");
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
