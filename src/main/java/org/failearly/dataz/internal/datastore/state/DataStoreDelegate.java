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
package org.failearly.dataz.internal.datastore.state;

import org.failearly.common.proputils.PropertiesAccessor;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStoreException;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.resource.DataResource;

import java.util.Objects;

/**
 * DataStoreDelegate is responsible for ...
 */
abstract class DataStoreDelegate implements DataStore {
    private DataStore origin;

    DataStoreDelegate(DataStore dataStore) {
        Objects.requireNonNull(dataStore, "DataStore must not be null.");
        if (dataStore instanceof DataStoreDelegate) {
            this.origin = ((DataStoreDelegate) dataStore).originDataStore();
        }
        else {
            this.origin = dataStore;
        }
        Objects.requireNonNull(this.origin, "DataStore must not be null.");
    }

    protected final DataStore originDataStore() {
        return origin;
    }

    @Override
    public final Class<? extends NamedDataStore> getNamedDataStore() {
        return origin.getNamedDataStore();
    }

    @Override
    public String getId() {
        return origin.getId();
    }

    @Override
    public String getName() {
        return origin.getName();
    }

    @Override
    public String getConfigFile() {
        return origin.getConfigFile();
    }

    @Override
    public void initialize() throws DataStoreException {
        origin.initialize();
    }

    @Override
    public PropertiesAccessor getProperties() {
        return origin.getProperties();
    }

    @Override
    public boolean hasTransactionalSupport() {
        return origin.hasTransactionalSupport();
    }

    @Override
    public void applyDataResource(DataResource dataResource) {
        origin.applyDataResource(dataResource);
    }

    @Override
    public void dispose() {
        doDispose();
    }

    protected final void doDispose() {
        if (origin == null) {
            throw new IllegalStateException("Called dispose twice!");
        }
        origin.dispose();
        origin = null;
    }
}
