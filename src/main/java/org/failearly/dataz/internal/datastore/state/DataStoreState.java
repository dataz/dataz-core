/*
 * Copyright (c) 2009.
 *
 * Date: 21.05.16
 * 
 */
package org.failearly.dataz.internal.datastore.state;

import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.resource.DataResource;

/**
 * DataStoreState encapsulate {@link DataStore} instance and gives and deny permission to different methods.
 * <br><br>
 * To get an initial {@code {@link DataStoreState}} object, you should call {@link DataStoreStates#create(DataStore)}.
 * The method {@link #initialize()} must not be called, it will be called, the first time, you call {@link #reserve(OnRelease)}.
 * Calling {@link #initialize()} will always fail.
 * <br><br>
 * The implementation of all {@code DataStoreState} will your find in {@link DataStoreStates}.
 *
 * @see DataStoreStates
 * @see DataStore
 */
public interface DataStoreState extends DataStore {

    /**
     * Reserve the instance. After this call (if permitted), the {@code datastore} is in state <i>reserved</i>.
     * @return the datastore in state 'reserved'.
     * @throws IllegalStateException not permitted in given state.
     *
     * @see #register(OnRelease)
     */
    DataStoreState reserve();

    /**
     * Reserve the instance and register for release event. This is a convenient method for:
     * <br><br>
     * <code>
     *     datastore.reserve().register(onRelease);
     * </code>
     *
     * @param onRelease the release callback.
     * @return the datastore in state 'reserved'.
     * @throws IllegalStateException not permitted in given state.
     * @see #register(OnRelease)
     * @see #reserve()
     * @see #release()
     */
    DataStoreState reserve(OnRelease onRelease);

    /**
     * Register for release event. Each registered callback method will be called with an {@link DataStoreState} object
     * in state 'Idle', while a calling {@link #release()} on a {@link #reserve()}'d datastore.
     * @param onRelease the release callback.
     * @return itself
     * @throws IllegalStateException not permitted in given state.
     * @see #release()
     */
    DataStoreState register(OnRelease onRelease);

    /**
     * Release the <i>reserved</i> datastore. After this call (if permitted), the {@code datastore} is in
     * state <i>idle</i>. All registered callbacks ({@link #register(OnRelease)} will be executed with an 'idle'
     * datastore.
     *
     * @throws IllegalStateException not permitted in given state.
     */
    void release();

    /**
     * The read only state 'accessed'. Nothing is possible except methods which does not change the state of the state
     * object or the origin {@link DataStore}, especially:<br><br>
     * <ul>
     *    <li>{@link #initialize()}</li>
     *    <li>{@link #applyDataResource(DataResource)}</li>
     *    <li>{@link #dispose()}</li>
     * </ul>
     *
     * @return the datastore in state 'accessed'.
     *
     * @throws IllegalStateException not permitted in given state.
     */
    DataStoreState access();

    /**
     * <b>Must not be called</b>. Throws always an illegal state exception.
     *
     * @throws IllegalStateException always thrown in any state.
     */
    void initialize() throws IllegalStateException;

    /**
     * Check if a data store belongs to the {@code namedDataStore}.
     * @param namedDataStore a named data store class to check.
     * @return {@code true} if it belongs to {@link NamedDataStore} class, otherwise {@code false}.
     */
    boolean belongsToNamedDataStore(Class<? extends NamedDataStore> namedDataStore);

    /**
     * Access to the origin {@link DataStore} instance.
     * @return the origin datastore.
     */
    DataStore getOriginDataStore();

    /**
     * Callback which must be registered with {@link #reserve(OnRelease)} and will be executed on {@link #release()}.
     */
    @FunctionalInterface
    interface OnRelease {
        /**
         * onRelease() called, when {@link #release()} has been called on {@link DataStoreState}. The object will be in state
         * <i>idle</i>.
         * @param idle the <i>idle</i> datastore.
         */
        void onRelease(DataStoreState idle);
    }
}
