/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.datastore;

import org.failearly.dataset.AdhocDataStore;

/**
 * DataStoreType represents the type of an DataStore and is also the factory for a {@link org.failearly.dataset.datastore.DataStore}.
 *
 * Used by {@link AdhocDataStore#type()}.
 *
 * @see org.failearly.dataset.datastore.DefaultDataStoreFactory
 */
public interface DataStoreType extends DataStoreFactory<AdhocDataStore> {
}
