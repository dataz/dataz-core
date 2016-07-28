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
package org.failearly.dataz.internal.datastore.state;

import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStoreException;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.resource.DataResource;

/**
 * DataStoreStateBase is the base implementation for {@link DataStoreState}.
 *
 * DataStoreStateBase implements {@link #belongsToNamedDataStore(Class)}, but protect all important
 * method by throwing an {@link IllegalStateException}. The subclasses are the states, which gives permission
 * by overriding the protected methods.
 * <br><br>
 * <b>Caution</b>: {@link #initialize()} will protected for all states.
 *
 * @see DataStoreStates
 */
abstract class DataStoreStateBase extends DataStoreDelegate implements DataStoreState {

    DataStoreStateBase(DataStore dataStore) {
        super(dataStore);
    }

    @Override
    public final boolean belongsToNamedDataStore(Class<? extends NamedDataStore> namedDataStore) {
        return this.getNamedDataStore() == namedDataStore;
    }

    @Override
    public DataStoreState reserve() {
        return illegalState("reserve");
    }

    @Override
    public DataStore getOriginDataStore() {
        return illegalState("getOriginDataStore");
    }

    @Override
    public final DataStoreState reserve(OnRelease onRelease) {
        return reserve().register(onRelease);
    }

    public DataStoreState register(OnRelease onRelease) {
        return illegalState("register");
    }

    @Override
    public void release() {
        illegalState("release");
    }

    @Override
    public DataStoreState access() {
        return illegalState("access");
    }

    @Override
    public final void initialize() throws DataStoreException {
        illegalState("initialize");
    }

    @Override
    public void applyDataResource(DataResource dataResource) {
        illegalState("applyDataResource");
    }

    @Override
    public void dispose() {
        illegalState("dispose");
    }

    private DataStoreStateBase illegalState(String method) {
        throw new IllegalStateException("Illegal State: DO NOT USE method '" + method + "' in state '" + this.getClass().getSimpleName() + "'.");
    }
}
