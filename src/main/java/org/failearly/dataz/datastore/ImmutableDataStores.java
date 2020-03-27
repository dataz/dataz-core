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
package org.failearly.dataz.datastore;

import org.failearly.dataz.NamedDataStore;

import java.util.function.Consumer;

/**
 * ImmutableDataStores could be resolved by {@link DataStores#access()})} or {@link MutableDataStores#access()}.
 */
public interface ImmutableDataStores {

    DataStore getDataStore(Class<? extends NamedDataStore> namedDataStore);

    /**
     * Read Only Access to all data stores.
     * @param dataStoreConsumer the consumer function can use any immutable or readonly method on the datastore.
     */
    void access(Consumer<DataStore> dataStoreConsumer);
}
