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

package org.failearly.dataz.resource;

import org.failearly.dataz.DataCleanup;
import org.failearly.dataz.DataSet;
import org.failearly.dataz.DataSetup;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.internal.resource.DataResourceProcessingException;
import org.failearly.dataz.internal.template.TemplateObjects;

import java.io.InputStream;

/**
 * DataSetResource represents either a {@link org.failearly.dataz.DataSet#setup()} or {@link org.failearly.dataz.DataSet#cleanup()}
 * resource.
 * <p>
 * The resource could be checked for existence and opened as {@link java.io.InputStream}.
 */
public interface DataResource {
    /**
     * Returns the associated (named) datastore, the data resource should be applied on.
     *
     * @return the associated datastore.
     *
     * @see DataSet#datastores()
     * @see DataSetup#datastores()
     * @see DataCleanup#datastores()
     *
     */
    Class<? extends NamedDataStore> getNamedDataStore();

    /**
     * @return the data set name.
     * @see DataSet#name()
     * @see DataSetup#name()
     * @see DataCleanup#name()
     */
    String getDataSetName();

    /**
     * A single entry of {@link org.failearly.dataz.DataSet#setup()} or {@link org.failearly.dataz.DataSet#cleanup()}.
     *
     * @return the resource name.
     * @see DataSet#setup()
     * @see DataSet#cleanup()
     * @see DataSetup#value()
     * @see DataCleanup#value()
     */
    String getResource();

    /**
     * @return {@code true} if the resource should be handled within one transaction.
     * @see DataSet#transactional()
     * @see DataSetup#transactional()
     * @see DataCleanup#transactional()
     */
    boolean isTransactional();

    /**
     * @return {@code true} if the DataStore should fail.
     * @see DataSet#failOnError()
     * @see DataSetup#failOnError()
     * @see DataCleanup#failOnError()
     */
    boolean isFailOnError();

    /**
     * If the data resource is template based resource, here the target data resource will be generated.
     *
     * @param templateObjects all template objects available.
     * @throws DataResourceProcessingException in case of processing issues while processing the template.
     */
    void generate(TemplateObjects templateObjects) throws DataResourceProcessingException;

    /**
     * Open's the resource as stream.
     *
     * @return the input stream.
     * @throws DataSetException in case of any exception, while open the resource.
     */
    InputStream open() throws DataSetException;

    /**
     * Check if this {@code DataResource} is associated to {@code dataStore}, then {@link DataStore#applyDataResource(DataResource)}
     * could be applied.
     * @param dataStore the datastore
     * @return {@code true} if this resource is associated to the {@code dataStore}, otherwise {@code false}.
     *
     * @see DataStore#applyDataResource(DataResource)
     */
    boolean shouldAppliedOn(DataStore dataStore);
}
