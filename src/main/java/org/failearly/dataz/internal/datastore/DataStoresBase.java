/*
 * Copyright (c) 2009.
 *
 * Date: 19.05.16
 * 
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
