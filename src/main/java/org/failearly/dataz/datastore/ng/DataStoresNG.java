/*
 * Copyright (c) 2009.
 *
 * Date: 16.05.16
 * 
 */
package org.failearly.dataz.datastore.ng;

import org.failearly.common.annotations.Tests;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.internal.datastore.DataStoresImplementation;

import java.util.*;


/**
 * DataStoresNG replaces the old DataStore handling. It's now working on {@link NamedDataStore} class objects.
 */
@SuppressWarnings("WeakerAccess")
@Tests("org.failearly.dataz.datastore.ng.DataStoresNGTest")
public final class DataStoresNG {

    private static DataStoresInstance INSTANCE = DataStoresImplementation.createDataStoresInstance();

    private DataStoresNG() {
    }

    public static ImmutableDataStores access() {
        return INSTANCE.access();
    }

    @SafeVarargs
    public static MutableDataStores reserve(Class<? extends NamedDataStore>... namedDataStores) {
        return reserve(Arrays.asList(namedDataStores));
    }

    public static MutableDataStores reserve(Collection<Class<? extends NamedDataStore>> namedDataStores) {
        return INSTANCE.reserve(new HashSet<>(namedDataStores));
    }

    public static void dispose() {
        synchronized (DataStoresNG.class) {
            final DataStoresInstance current = INSTANCE;
            current.dispose();
            INSTANCE = DataStoresImplementation.createDataStoresInstance();
        }
    }
}
