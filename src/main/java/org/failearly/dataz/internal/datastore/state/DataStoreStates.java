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
import org.failearly.dataz.resource.DataResource;

/**
 * DataStoreStates is responsible for holding all {@link DataStoreState}s and implementing the state engine.
 *
 */
public final class DataStoreStates {

    private DataStoreStates() {
    }

    /**
     * Factory method to create the initial {@link DataStoreState} instance.
     *
     * @param dataStore  the origin datastore
     *
     * @return the initial {@link DataStoreState}
     */
    public static DataStoreState create(DataStore dataStore) {
        assert ! (dataStore instanceof DataStoreStateBase) : "Only origin datastore instance are permitted";
        return new Created(dataStore);
    }

    private static DataStoreState idle(DataStoreState previous) {
        assert previous instanceof Reserved : "Reserve -> Idle";
        return new Idle(previous);
    }

    private static DataStoreState reserve(DataStoreState previous) {
        assert previous instanceof Created || previous instanceof Idle: "{Created, Idle} -> Reserved";
        return new Reserved(previous);
    }

    private static DataStoreState access(DataStoreState previous) {
        assert     previous instanceof Created
                || previous instanceof Reserved
                || previous instanceof Idle: "{Created, Reserved, Idle} -> Accessed";
        return new Accessed(previous);
    }

    /**
     * The <i>created</i> state. Following is permitted:
     * <br><br>
     * <ul>
     *    <li>{@link #reserve()}</li>
     *    <li>{@link #access()}</li>
     *    <li>{@link #dispose()} (does nothing because nothing has been done)</li>
     * </ul>
     * <br><br>
     * Not permitted is:<br><br>
     * <ul>
     *    <li>{@link #initialize()}</li>
     *    <li>{@link #release()}</li>
     *    <li>{@link #applyDataResource(DataResource)}</li>
     * </ul>
     */
    static final class Created extends DataStoreStateBase {
        private Created(DataStore dataStore) {
            super(dataStore);
        }

        @Override
        public DataStoreState access() {
            return DataStoreStates.access(this);
        }

        @Override
        public DataStoreState reserve() {
            // initialize is protected by DataStoreStateBase.
            originDataStore().initialize();
            return DataStoreStates.reserve(this);
        }

        @Override
        public void dispose() {
            // not necessary because there was not initialization
        }
    }

    /**
     * The <i>reserved</i> state. Following is permitted:
     * <br><br>
     * <ul>
     *    <li>{@link #applyDataResource(DataResource)}</li>
     *    <li>{@link #getOriginDataStore()}</li>
     *    <li>{@link #register(OnRelease)}</li>
     *    <li>{@link #release()}</li>
     *    <li>{@link #access()}</li>
     * </ul>
     * <br><br>
     * Not permitted is:<br><br>
     * <ul>
     *    <li>{@link #initialize()}</li>
     *    <li>{@link #dispose()}</li>
     *    <li>{@link #applyDataResource(DataResource)}</li>
     * </ul>
     */
    static final class Reserved extends DataStoreStateBase {
        private final Thread current=Thread.currentThread();
        private final OnReleaseList onRelease=new OnReleaseList();

        private Reserved(DataStoreState lastState) {
            super(lastState);
        }

        @Override
        public DataStoreState register(OnRelease onRelease) {
            invariant();
            this.onRelease.add(onRelease);
            return this;
        }

        @Override
        public void applyDataResource(DataResource dataResource) {
            invariant();
            originDataStore().applyDataResource(dataResource);
        }

        @Override
        public DataStore getOriginDataStore() {
            invariant();
            return originDataStore();
        }

        @Override
        public DataStoreState access() {
            invariant();
            return DataStoreStates.access(this);
        }

        @Override
        public void release() {
            invariant();
            onRelease.onRelease(DataStoreStates.idle(this));
        }

        private void invariant() {
            final Thread thread = Thread.currentThread();
            if( ! this.current.equals(thread) ) {
                throw new IllegalStateException(
                        "Reserved for thread '" + current.getName() +
                        "', but accessed within thread '" + thread.getName() + "'"
                );
            }
        }
    }

    /**
     * The <i>idle</i> state. Following is permitted:
     * <br><br>
     * <ul>
     *    <li>{@link #reserve()}</li>
     *    <li>{@link #access()}</li>
     *    <li>{@link #dispose()}</li>
     *    <li>All method which does not change the state of DataStore</li>
     * </ul>
     * <br><br>
     * Not permitted is:<br><br>
     * <ul>
     *    <li>{@link #initialize()}</li>
     *    <li>{@link #release()}</li>
     *    <li>{@link #applyDataResource(DataResource)}</li>
     * </ul>
     */
    static final class Idle extends DataStoreStateBase {
        private Idle(DataStoreState state) {
            super(state);
        }

        @Override
        public DataStoreState reserve() {
            return DataStoreStates.reserve(this);
        }

        @Override
        public DataStoreState access() {
            return DataStoreStates.access(this);
        }


        @Override
        public void dispose() {
            doDispose();
        }
    }

    /**
     * The <i>access</i> state. Following is permitted:
     * <br><br>
     * <ul>
     *    <li>All method which does not change the state of DataStore</li>
     * </ul>
     * <br><br>
     * Not permitted is:<br><br>
     * <ul>
     *    <li>{@link #initialize()}</li>
     *    <li>{@link #reserve()}</li>
     *    <li>{@link #access()}</li>
     *    <li>{@link #dispose()}</li>
     *    <li>{@link #release()}</li>
     *    <li>{@link #applyDataResource(DataResource)}</li>
     * </ul>
     */
    static final class Accessed extends DataStoreStateBase {
        private Accessed(DataStoreState dataStore) {
            super(dataStore);
        }
    }
}
