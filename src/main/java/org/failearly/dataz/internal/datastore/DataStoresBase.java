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
package org.failearly.dataz.internal.datastore;

import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStoreException;
import org.failearly.dataz.datastore.ImmutableDataStores;
import org.failearly.dataz.datastore.MutableDataStores;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.internal.datastore.state.DataStoreState;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * DataStoresBase provides the implementation for {@link MutableDataStores} and {@link ImmutableDataStores}.
 */
abstract class DataStoresBase {
    final List<DataStoreState> dataStores;

    DataStoresBase(List<DataStoreState> dataStores) {
        this.dataStores = dataStores;
    }

    public final DataStore getOriginDataStore(Class<? extends NamedDataStore> namedDataStore) {
        final Optional<DataStoreState> dataStore = findDataStore(namedDataStore);
        return dataStore
                .map(DataStoreState::getOriginDataStore)
                .orElseThrow(()->new DataStoreException("Missing data store " + namedDataStore.getName() + "."));
    }

    public final DataStore getDataStore(Class<? extends NamedDataStore> namedDataStore) {
        final Optional<DataStoreState> dataStore = findDataStore(namedDataStore);
        return dataStore
                .orElseThrow(()->new DataStoreException("Missing data store " + namedDataStore.getName() + "."));
    }

    public final <T extends DataStore> T getOriginDataStore(Class<? extends NamedDataStore> namedDataStore, Class<T> targetClass) {
        return targetClass.cast(this.getOriginDataStore(namedDataStore));
    }

    public final void apply(Consumer<DataStore> dataStoreConsumer) {
        dataStores.forEach(dataStoreConsumer);
    }

    public final void access(Consumer<DataStore> dataStoreConsumer) {
        dataStores.forEach(dataStoreConsumer);
    }

    private Optional<DataStoreState> findDataStore(Class<? extends NamedDataStore> namedDataStore) {
        return dataStores.stream()
                .filter(dataStore -> dataStore.belongsToNamedDataStore(namedDataStore))
                .findAny();
    }
}
