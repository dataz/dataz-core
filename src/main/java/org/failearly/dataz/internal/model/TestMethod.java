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

package org.failearly.dataz.internal.model;

import org.failearly.dataz.AdhocDataStore;
import org.failearly.dataz.internal.resource.DataResourceHandler;

/**
 * TestMethod collect all necessary information from a Test {@link java.lang.reflect.Method} - a public method
 * annotated with {@link org.junit.Test} and not {@link org.junit.Ignore}.
 */
public interface TestMethod {
    /**
     * @return {@code true} if there is any valid resource to apply.
     */
    boolean isValid();

    /**
     * @return the test methods name.
     */
    String getName();

    /**
     * handle all available setup resources (in the correct order).
     * @param dataStoreId  the ID of a {@link org.failearly.dataz.datastore.DataStore}.
     * @param dataResourceHandler the setup data set resource handler
     *
     * @see org.failearly.dataz.datastore.DataStore#getId
     * @see AdhocDataStore#id
     */
    void handleSetupResource(String dataStoreId, DataResourceHandler dataResourceHandler);

    /**
     * handle all available cleanup resources (in the correct order).
     * @param dataStoreId  the ID of a {@link org.failearly.dataz.datastore.DataStore}.
     * @param dataResourceHandler the cleanup data set resource handler
     */
    void handleCleanupResource(String dataStoreId, DataResourceHandler dataResourceHandler);

    /**
     * @return {@code true} if the method or class has been annotated with {@link org.failearly.dataz.SuppressCleanup}.
     */
    boolean isSuppressCleanup();
}
