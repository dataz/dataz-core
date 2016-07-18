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

import org.failearly.common.proputils.PropertiesAccessor;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.resource.DataResource;

/**
 * {@code DataStore} is the representation for any Database driver. For any {@link NamedDataStore} and applied datastore
 * annotation there will be an instance of {@code DataStore}.
 */
public interface DataStore {
    /**
     * The named datastore of this instance.
     * @return the named data store
     */
    Class<? extends NamedDataStore> getNamedDataStore();

    /**
     * @return the name of the data store (annotation).
     */
    String getName();

    /**
     * @return the id of the data store.
     */
    String getId();

    /**
     * @return the configuration file of the data store.
     */
    String getConfigFile();

    /**
     * Initialize the DataStore. I.e. Establish connection(s).
     *
     * @throws org.failearly.dataz.datastore.DataStoreException in case of any exception while initialize the Datastore.
     */
    void initialize() throws DataStoreException;

    /**
     * Return the loaded properties. The properties will be resolved as first step during initialization.
     *
     * @return the loaded properties from config file and/or properties annotation.
     *
     * @see #getConfigFile()
     * @see org.failearly.dataz.common.Property
     */
    PropertiesAccessor getProperties();

    /**
     * If the DataStore supports transactional behaviour, the {@link org.failearly.dataz.DataSet#transactional()} will
     * be used, otherwise not.
     *
     * @return {@code true} if the DataStore has transactional resource.
     * @see org.failearly.dataz.DataSet#transactional()
     * @see DataResource#isTransactional()
     */
    boolean hasTransactionalSupport();

    /**
     * Apply {@code dataResource} on this datastore instance.
     * @param dataResource a {@link DataResource} instance
     */
    void applyDataResource(DataResource dataResource);

    /**
     * Dispose reserved resources (i.e. database connections)
     */
    void dispose();
}
