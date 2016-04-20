/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */
package org.failearly.dataset.datastore;

import org.failearly.dataset.AdhocDataStore;

import java.lang.annotation.Annotation;

/**
 * DataStoreFactory is the abstract factory for creating {@link org.failearly.dataset.datastore.DataStore}.
 */
@SuppressWarnings("WeakerAccess")
public interface DataStoreFactory<T extends Annotation> {
    /**
     * Create an instance of {@link org.failearly.dataset.datastore.DataStore} based on a data store annotation.
     *
     * @param annotation a data store annotation with the meta annotation {@link org.failearly.dataset.datastore.DataStoreFactoryDefinition}.
     * @param context a (optional) context object used for creating.
     *
     * @return a new data store instance.
     *
     * @see DataStoreFactoryDefinition
     * @see AdhocDataStore
     */
    DataStore createDataStore(T annotation, Object context);
}
