/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com/contact)
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
