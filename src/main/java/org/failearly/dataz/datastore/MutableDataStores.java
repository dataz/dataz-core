/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.failearly.dataz.datastore;

import org.failearly.dataz.NamedDataStore;

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
