/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.resource;

import java.io.InputStream;
import java.util.List;

/**
 * DataSetResource represents either a {@link org.failearly.dataset.DataSet#setup()} or {@link org.failearly.dataset.DataSet#cleanup()}
 * resource.
 *
 * The resource could be checked for existence and opened as {@link java.io.InputStream}.
 */
public interface DataResource {
    /**
     * @return The data store id.
     *
     * @see org.failearly.dataset.DataSet#datastore()
     * @see org.failearly.dataset.DataStoreDefinition#id()
     */
    String getDataStoreId();

    /**
     * @return the data set name.
     *
     * @see org.failearly.dataset.DataSet#name()
     * @see org.failearly.dataset.DataStoreSetup#name()
     */
    String getDataSetName();

    /**
     * A single entry of {@link org.failearly.dataset.DataSet#setup()} or {@link org.failearly.dataset.DataSet#cleanup()}.
     *
     * @return the resource name.
     *
     * @see org.failearly.dataset.DataSet#setup()
     * @see org.failearly.dataset.DataSet#cleanup()
     */
    String getResource();

    /**
     * @return {@code true} if the resource should be handled within one transaction.
     *
     * @see org.failearly.dataset.DataSet#transactional()
     */
    boolean isTransactional();

    /**
     * Return an additional value stored under {@code key} or {@code defaultValue}.
     * @param key the key or name of the additional value
     * @param defaultValue the value to be returned if there is no value stored for given {@code key}
     * @param <T> the expected type.
     * @return the additional value or {@code defaultValue}.
     */
    public <T> T getAdditionalValue(String key, T defaultValue);

    /**
     * @return the input stream.
     */
    InputStream open();

    /**
     * @return {@code true} if the DataStore should fail.
     *
     */
    boolean isFailOnError();
}
