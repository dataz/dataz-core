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

import org.failearly.dataz.common.Tests;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.internal.datastore.DataStoresImplementation;

import java.util.*;


/**
 * DataStores replaces the old DataStore handling. It's now working on {@link NamedDataStore} class objects.
 */
@SuppressWarnings("WeakerAccess")
@Tests("org.failearly.dataz.datastore.DataStoresTest")
public final class DataStores {

    private static Instance INSTANCE = DataStoresImplementation.createDataStoresInstance();

    private DataStores() {
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
        synchronized (DataStores.class) {
            final Instance current = INSTANCE;
            current.dispose();
            INSTANCE = DataStoresImplementation.createDataStoresInstance();
        }
    }

    /**
     * Internal usage. Not for public usage.
     */
    public interface Instance {
        /**
         * @return Read only DataStore accessor
         */
        ImmutableDataStores access();

        /**
         * Reserve one instance of named data store(s)
         * @param namedDataStores  named data stores.
         * @return all requested named data stores.
         */
        MutableDataStores reserve(Set<Class<? extends NamedDataStore>> namedDataStores);

        /**
         * Dispose the datastores.
         */
        void dispose();
    }
}
