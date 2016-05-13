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

import org.failearly.dataz.internal.model.TestMethod;
import org.failearly.dataz.resource.DataResource;

/**
 * NullDataStore is a placeholder for dataSet setup issue.
 */
final class NullDataStore extends DataStoreBase {

    public NullDataStore(String dataStoreId, String dataStoreConfigFile) {
        super(dataStoreId, dataStoreConfigFile);
    }

    @Override
    public void initialize() throws DataStoreException {
    }

    @Override
    public void setup(TestMethod testMethod) throws DataStoreException {
        LOGGER.error("NullDataStore: setup({})", testMethod);
        throw new MissingDataStoreException();
    }

    @Override
    public void cleanup(TestMethod testMethod) {
        LOGGER.warn("NullDataStore: cleanup({})", testMethod);
    }

    @Override
    protected void doApplyResource(DataResource dataResource) throws DataStoreException {
        throw new MissingDataStoreException();
    }

    public static class MissingDataStoreException extends DataStoreException {
        public MissingDataStoreException() {
            super("Please add valid DataStore implementation to the current classpath.");
        }
    }
}
