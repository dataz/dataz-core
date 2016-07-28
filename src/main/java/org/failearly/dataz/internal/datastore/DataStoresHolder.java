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

import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.internal.datastore.state.DataStoreState;
import org.failearly.dataz.internal.datastore.state.DataStoreState.OnRelease;
import org.failearly.dataz.internal.datastore.state.DataStoreStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * DataStoresHolder keeps all DataStores and is responsible for reserving, releasing and disposing of
 * {@link DataStore} instances. It also garantees that not two threads could gain the same DataStore and act on it.
 * <br><br>
 * Remarks:<br><br>
 * <ul>
 *    <li>The DataStoreHolder uses a LIFO (Stack) implementation,
 *          so if not used multithreaded only one {@link DataStore} will be used.</li>
 *    <li>{@code DataStoresHolder} is ThreadSafe</li>
 * </ul>
 *
 * @see DataStoreStates
 * @see DataStoreState
 */
final class DataStoresHolder implements OnRelease {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataStoresHolder.class);
    private final BlockingDeque<DataStoreState> dataStores;
    private final Class<? extends NamedDataStore> namedDataStore;
    private boolean dispose = false;


    /**
     * The factory method
     * @param namedDataStore the named datastore
     * @param dataStores origin datastore
     * @return  the datastore holder
     */
    static DataStoresHolder createDataStoresHolder(Class<? extends NamedDataStore> namedDataStore, List<DataStore> dataStores) {
        final List<DataStoreState> dataStoreStates=toDataStoreStates(dataStores);
        invariant(namedDataStore, dataStoreStates);
        return new DataStoresHolder(namedDataStore, dataStoreStates);
    }

    private DataStoresHolder(Class<? extends NamedDataStore> namedDataStore, List<DataStoreState> dataStoreStates) {
        this.namedDataStore = namedDataStore;
        this.dataStores = new LinkedBlockingDeque<>(dataStoreStates);
    }

    private static  List<DataStoreState> toDataStoreStates(List<DataStore> dataStores) {
        return dataStores.stream()
                    .map(DataStoreStates::create)
                    .collect(Collectors.toList());
    }

    /**
     * Reserve the next available DataStore. If none is available this will block.
     *
     * @return a reserved DataStore
     * @see DataStoreState#reserve(OnRelease)
     */
    DataStoreState reserve() {
        return pop().reserve(this);
    }


    @Override
    public void onRelease(DataStoreState dataStoreState) {
        invariant(namedDataStore, dataStoreState);
        push(dataStoreState);
        if (dispose) {
            this.dispose();
        }
    }

    void dispose() {
        this.dispose = true;
        final Collection<DataStore> allDataStores = new LinkedList<>();
        dataStores.drainTo(allDataStores);
        allDataStores.forEach(DataStore::dispose);
    }

    private DataStoreState pop() {
        try {
            final DataStoreState dataStoreState = this.dataStores.takeFirst();
            LOGGER.info("Reserve datastore {}", dataStoreState.getId());
            return dataStoreState;
        } catch (InterruptedException e) {
            LOGGER.error("{}: pop() interrupted.", namedDataStore.getSimpleName());
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Caught interrupt. May be deadlock.", e);
        }
    }

    private void push(DataStoreState dataStoreState) {
        try {
            LOGGER.info("Datastore {} released", dataStoreState.getId());
            this.dataStores.putFirst(dataStoreState);
        } catch (InterruptedException e) {
            LOGGER.error("{}: push() interrupted.", namedDataStore.getSimpleName());
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Caught interrupt. May be deadlock.", e);
        }
    }

    private static void invariant(Class<? extends NamedDataStore> namedDataStore, List<DataStoreState> dataStoreStates) {
        dataStoreStates.forEach((dataStoreState) -> invariant(namedDataStore, dataStoreState));
    }

    private static void invariant(Class<? extends NamedDataStore> namedDataStore, DataStoreState dataStoreState) {
        if (! dataStoreState.belongsToNamedDataStore(namedDataStore))
            throw new IllegalArgumentException("This datastore does not belong to current datastore holder " + namedDataStore.getName() );
    }
}
