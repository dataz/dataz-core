/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.failearly.dataz.datastore;

import org.failearly.common.annotations.Tests;
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
    public static interface Instance {
        /**
         * Read only accessor to
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
