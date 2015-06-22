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
import org.failearly.dataset.internal.model.TestMethod;
import org.failearly.dataset.internal.template.TemplateObjects;
import org.failearly.dataset.resource.DataResource;

import java.util.List;

/**
 * DataStore is the representation for any Database driver.
 */
public interface DataStore {
    /**
     * @return the id of the data store.
     * @see AdhocDataStore#id()
     */
    String getId();

    /**
     * @return the configuration file of the data store.
     * @see AdhocDataStore#config()
     */
    String getConfig();

    /**
     * The default suffix used for searching <b>setup</b> resource files.
     *
     * @return suffix to be used for {@link org.failearly.dataset.DataSet#setup()} (if no setup resource is specified).
     * @see AdhocDataStore#setupSuffix()
     * @see org.failearly.dataset.config.DataSetProperties#getDefaultSetupSuffix()
     */
    String getSetupSuffix();

    /**
     * The default suffix used for searching <b>cleanup</b> resource files.
     *
     * @return suffix to be used for {@link org.failearly.dataset.DataSet#cleanup()} (if no setup resource is specified).
     * @see AdhocDataStore#cleanupSuffix()
     * @see org.failearly.dataset.config.DataSetProperties#getDefaultCleanupSuffix()
     */
    String getCleanupSuffix();

    /**
     * Initialize the DataStore. I.e. Establish connection(s).
     *
     * @throws org.failearly.dataset.datastore.DataStoreException in case of any exception while initialize the Datastore.
     */
    void initialize() throws DataStoreException;

    /**
     * After initialization, the DataStore needs sometimes special setup (like creating a schema).
     *
     * @param dataStoreSetups   all declared {@link org.failearly.dataset.DataStoreSetup} annotations available.
     * @param templateObjects a list of {@link org.failearly.dataset.template.TemplateObject}s, which represents
     *                          all template object annotations.
     * @throws org.failearly.dataset.datastore.DataStoreException in case of any exception while setup the Datastore.
     * @see org.failearly.dataset.DataStoreSetup
     */
    void setupDataStore(List<DataStoreSetupInstance> dataStoreSetups, TemplateObjects templateObjects) throws DataStoreException;

    /**
     * If the DataStore supports transactional behaviour, the {@link org.failearly.dataset.DataSet#transactional()} will
     * be used, otherwise not.
     *
     * @return {@code true} if the DataStore has transactional resource.
     * @see org.failearly.dataset.DataSet#transactional()
     * @see DataResource#isTransactional()
     */
    boolean hasTransactionalSupport();

    /**
     * Apply the {@link org.failearly.dataset.DataSet#setup()} resources.
     *
     * @param testMethod current test method.
     * @throws org.failearly.dataset.datastore.DataStoreException thrown in case any unexpected issue with given
     *                                                            setup resource of {@link org.failearly.dataset.DataSet}.
     * @see org.failearly.dataset.internal.model.TestMethod#handleSetupResource(String, org.failearly.dataset.internal.resource.DataResourceHandler)
     */
    void setup(TestMethod testMethod) throws DataStoreException;

    /**
     * Apply the {@link org.failearly.dataset.DataSet#cleanup()} resources.
     *
     * @param testMethod current test method.
     * @throws org.failearly.dataset.datastore.DataStoreException thrown in case any unexpected issue with given
     *                                                            cleanup resource of {@link org.failearly.dataset.DataSet}.
     * @see org.failearly.dataset.internal.model.TestMethod#handleCleanupResource(String, org.failearly.dataset.internal.resource.DataResourceHandler)
     */
    void cleanup(TestMethod testMethod) throws DataStoreException;

    /**
     * Does the cleanup of the data store, after all tests has been executed.
     *
     * @see DataStores#dispose()
     * @see DataStores#shutdown()
     */
    void cleanupDataStore() throws DataStoreException;

    /**
     * Dispose reserved resources (i.e. database connections)
     */
    void dispose();

    /**
     * Return the property with given key.
     *
     * @param key the property key.
     * @return the property value or {@code null}.
     */
    String getProperty(String key);
}
