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
package org.failearly.dataz.internal.datastore;

import org.failearly.dataz.datastore.ImmutableDataStores;
import org.failearly.dataz.datastore.MutableDataStores;
import org.failearly.dataz.internal.datastore.state.DataStoreState;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MutableDataStores is responsible for ...
 */
final class MutableDataStoresImpl extends DataStoresBase implements MutableDataStores {
    MutableDataStoresImpl(List<DataStoreState> dataStores) {
        super(dataStores);
    }

    @Override
    public void release() {
        dataStores.forEach(DataStoreState::release);
        dataStores.clear();
    }

    @Override
    public ImmutableDataStores access() {
        return new ImmutableDataStoresImpl(allToAccess());
    }

    private List<DataStoreState> allToAccess() {
        return dataStores.stream()
                         .map(DataStoreState::access)
                         .collect(Collectors.toList());
    }
}
