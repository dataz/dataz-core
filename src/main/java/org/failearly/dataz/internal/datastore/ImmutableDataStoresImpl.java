/*
 * Copyright (c) 2009.
 *
 * Date: 19.05.16
 * 
 */
package org.failearly.dataz.internal.datastore;

import org.failearly.dataz.datastore.ng.ImmutableDataStores;
import org.failearly.dataz.internal.datastore.state.DataStoreState;

import java.util.List;

/**
 * MutableDataStores is responsible for ...
 */
final class ImmutableDataStoresImpl extends DataStoresBase implements ImmutableDataStores {
    ImmutableDataStoresImpl(List<DataStoreState> dataStores) {
        super(dataStores);
    }
}
