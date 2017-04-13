/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
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
