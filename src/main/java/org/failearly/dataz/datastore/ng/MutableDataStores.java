/*
 * Copyright (c) 2009.
 *
 * Date: 19.05.16
 * 
 */
package org.failearly.dataz.datastore.ng;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStore;

import java.util.function.Consumer;

/**
 * MutableDataStores is responsible for ...
 */
public interface MutableDataStores {

    DataStore getOriginDataStore(Class<? extends NamedDataStore> namedDataStore);

    <T extends DataStore> T getOriginDataStore(Class<? extends NamedDataStore> namedDataStore, Class<T> targetClass);

    void apply(Consumer<DataStore> dataStoreConsumer);

    void release();

    ImmutableDataStores access();
}
