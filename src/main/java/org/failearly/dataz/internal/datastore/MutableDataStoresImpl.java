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
