/*
 * Copyright (c) 2009.
 *
 * Date: 20.05.16
 * 
 */
package org.failearly.dataz.datastore.ng;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStore;

import java.util.function.Consumer;

/**
 * ImmutableDataStores could be resolved by {@link DataStoresNG#access()})} or {@link MutableDataStores#access()}.
 */
public interface ImmutableDataStores {

    DataStore getDataStore(Class<? extends NamedDataStore> namedDataStore);

    /**
     * Read Only Access to all data stores.
     * @param dataStoreConsumer the consumer function can use any immutable or readonly method on the datastore.
     */
    void access(Consumer<DataStore> dataStoreConsumer);
}
