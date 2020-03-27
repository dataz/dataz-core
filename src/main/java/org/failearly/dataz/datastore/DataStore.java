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

import org.failearly.dataz.internal.common.proputils.PropertiesAccessor;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.resource.DataResource;

/**
 * {@code DataStore} is the representation for any Database driver. For any {@link NamedDataStore} and applied datastore
 * impl there will be an instance of {@code DataStore}.
 */
public interface DataStore {
    /**
     * The named datastore of this instance.
     * @return the named data store
     */
    Class<? extends NamedDataStore> getNamedDataStore();

    /**
     * @return the name of the data store (impl).
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
     * @return the loaded properties from config file and/or properties impl.
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
