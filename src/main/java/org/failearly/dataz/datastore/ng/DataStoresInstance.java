/*
 * Copyright (c) 2009.
 *
 * Date: 23.05.16
 * 
 */
package org.failearly.dataz.datastore.ng;

import org.failearly.dataz.NamedDataStore;

import java.util.Set;

/**
 * DataStoresInstance is responsible for ...
 */
public interface DataStoresInstance {
    ImmutableDataStores access();

    MutableDataStores reserve(Set<Class<? extends NamedDataStore>> namedDataStores);

    void dispose();
}
