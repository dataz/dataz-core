/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */
package org.failearly.dataset.datastore;

import org.failearly.dataset.internal.model.TestMethod;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DataStoreCollection collects {@link org.failearly.dataset.datastore.DataStore} and delegates
 * to the actually implementations.
 */
final class DataStoreCollection extends AbstractDataStore {

    private final List<DataStore> dataStores = new LinkedList<>();
    private final Set<String> dataStoreIds = new HashSet<>();

    @Override
    public String getId() {
        return toString(DataStore::getId);
    }

    @Override
    public String getConfigFile() {
        return toString(DataStore::getConfigFile);
    }

    private String toString(Function<DataStore, String> mapFunction) {
        return "{" + String.join(",", dataStores.stream().map(mapFunction).collect(Collectors.toList())) + "}";
    }

    @Override
    public void setup(TestMethod testMethod) throws DataStoreException {
        dataStores.forEach((ds) -> ds.setup(testMethod));
    }

    @Override
    public void cleanup(TestMethod testMethod) {
        dataStores.forEach((ds) -> ds.cleanup(testMethod));
    }

    boolean isEmpty() {
        return dataStores.isEmpty();
    }

    DataStore getDataStore() {
        assert !dataStores.isEmpty() : "DataStoreCollection: At least one DataStore must be available";
        if (dataStores.size() > 1)
            return this;
        return dataStores.get(0);
    }


    void addDataStore(DataStore dataStore) {
        final String id = dataStore.getId();
        if (dataStoreIds.add(id)) {
            LOGGER.info("DataStore with id='{}' added!", id);
            dataStores.add(dataStore);
        } else {
            LOGGER.warn("DataStore with id='{}' duplicated -> ignored!", id);
        }
    }


    DataStore lookupDataStore(String id) {
        for (DataStore dataStore : dataStores) {
            if (id.equals(dataStore.getId()))
                return dataStore;
        }
        return null;
    }
}
