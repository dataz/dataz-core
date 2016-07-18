/*
 * Copyright (c) 2009.
 *
 * Date: 19.05.16
 * 
 */
package org.failearly.dataz.internal.datastore;

import org.failearly.dataz.datastore.ImmutableDataStores;
import org.failearly.dataz.datastore.MutableDataStores;
import org.failearly.dataz.internal.datastore.state.DataStoreState;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MutableDataStores is responsible for ...
 */
final class MutableDataStoresImpl extends DataStoresBase implements MutableDataStores {
    MutableDataStoresImpl(List<DataStoreState> dataStores) {
        super(dataStores);
    }

    @Override
    public void release() {
        dataStores.forEach(DataStoreState::release);
        dataStores.clear();
    }

    @Override
    public ImmutableDataStores access() {
        return new ImmutableDataStoresImpl(allToAccess());
    }

    private List<DataStoreState> allToAccess() {
        return dataStores.stream()
                         .map(DataStoreState::access)
                         .collect(Collectors.toList());
    }
}
