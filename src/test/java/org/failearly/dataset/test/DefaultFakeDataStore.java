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

package org.failearly.dataset.test;

import org.failearly.dataset.datastore.DataStoreBase;
import org.failearly.dataset.datastore.DataStoreException;
import org.failearly.dataset.resource.DataResource;

/**
* DefaultFakeDataStore to be used with
*/
public class DefaultFakeDataStore extends DataStoreBase {
    public DefaultFakeDataStore(String dataStoreId) {
        super(dataStoreId, "no-config");
    }

    @Override
    public void initialize() throws DataStoreException {
    }

    @Override
    protected void doApplyResource(DataResource dataResource) throws DataStoreException {
        throw new UnsupportedOperationException("doApplyResource not implemented");
    }

    @Override
    public boolean hasTransactionalSupport() {
        return false;
    }

}
