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

import org.apache.commons.lang.mutable.MutableInt;
import org.failearly.dataz.internal.common.test.ExceptionVerifier;
import org.failearly.dataz.internal.common.test.annotations.Subject;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.NamedDataStore;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * DataStoreStatesTest contains tests for ... .
 */
@Subject({DataStoreStates.class, DataStoreState.class})
public class DataStoreStatesTest {

    private DataStore originDataStore;
    private final OnReleaseStub onRelease=new OnReleaseStub();

    @Before
    public void setUp() throws Exception {
        originDataStore = mock(DataStore.class);
        when(originDataStore.getNamedDataStore()).then(invocation -> MyDataStore.class);
    }

    @Test
    public void how_to_create_datastore_in_different_states() throws Exception {
        // arrange / given
        final DataStoreState initial= DataStoreStates.create(originDataStore);

        // act / when
        final DataStoreState accessed=initial.access();
        final DataStoreState reserved= initial.reserve().register(onRelease);
        reserved.release();
        final DataStoreState idle = onRelease.releasedDataStore;

        // assert / then
        assertThat("Created?", initial, is(instanceOf(DataStoreStates.Created.class)));
        assertThat("Accessed?", accessed, is(instanceOf(DataStoreStates.Accessed.class)));
        assertThat("Reserved?", reserved, is(instanceOf(DataStoreStates.Reserved.class)));
        assertThat("Idle?", idle, is(instanceOf(DataStoreStates.Idle.class)));
    }

    @Test
    public void when_will_an_origin_datastore_not_initialized_or_even_used() throws Exception {
        // act / when
        DataStoreStates.create(originDataStore);

        // assert / then
        verify(originDataStore, times(0)).initialize();
        verifyNoMoreInteractions(originDataStore);
    }

    @Test
    public void when_will_an_origin_datastore_initialized() throws Exception {
        // arrange / given
        final DataStoreState initial=DataStoreStates.create(originDataStore);

        // act / when
        initial.reserve();
        initial.reserve();

        // assert / then
        verify(originDataStore, times(2)).initialize();
        verifyNoMoreInteractions(originDataStore);
    }

    @Test
    public void how_to_guarantee_that_an_origin_datastore_is_initialized_only_once() throws Exception {
        // arrange / given
        DataStoreState useReleaseDataStore;
        final DataStoreState initial=DataStoreStates.create(originDataStore);

        // act / when
        initial.reserve().register(onRelease).release();  // initialize() only executed the first time.
        useReleaseDataStore = onRelease.releasedDataStore;
        useReleaseDataStore.reserve().register(onRelease).release();
        useReleaseDataStore = onRelease.releasedDataStore;
        useReleaseDataStore.reserve().register(onRelease).release();

        // assert / then
        verify(originDataStore, times(1)).initialize();
        verifyNoMoreInteractions(originDataStore);
    }

    @Test
    public void in_which_order_should_multiple_registered_callbacks_executed() throws Exception {
        // arrange / given
        final MutableInt result=new MutableInt(0);
        final DataStoreState initial=DataStoreStates.create(originDataStore);

        final DataStoreState reserved = initial.reserve();
        reserved.register((ds)->result.setValue(result.intValue()/5));                              // result /=5
        reserved.register((ds)->result.setValue(result.intValue()*20));                              // result *=20
        reserved.register((ds)->result.setValue(result.intValue()+7));                               // result += 7

        // act / when
        reserved.release();

        // assert / then
        assertThat("Executed in LIFO-Order?", result.intValue(), is((7*20)/5));
    }

    private void expectAlwaysPermitted(DataStoreState dataStoreState) {
        alwaysPermittedOnOnDataStore(dataStoreState);
        alwaysPermittedOnDataStoreState(dataStoreState);
    }

    private void alwaysPermittedOnOnDataStore(DataStore dataStore) {
        dataStore.getId();
        dataStore.getConfigFile();
        dataStore.getNamedDataStore();
        dataStore.hasTransactionalSupport();
    }

    private void alwaysPermittedOnDataStoreState(DataStoreState dataStoreState) {
        dataStoreState.belongsToNamedDataStore(MyDataStore.class);
    }

    @Test
    public void which_methods_are_always_permitted_in_any_state() throws Exception {
        // arrange / given
        final DataStoreState initial= DataStoreStates.create(originDataStore);
        final OnReleaseStub onReleaseStub=new OnReleaseStub();

        // act / when
        final DataStoreState accessed=initial.access();
        final DataStoreState reserved= initial.reserve().register(onReleaseStub);
        reserved.release();
        final DataStoreState idle = onReleaseStub.releasedDataStore;

        // assert / then
        expectAlwaysPermitted(initial);
        expectAlwaysPermitted(accessed);
        expectAlwaysPermitted(reserved);
        expectAlwaysPermitted(idle);
    }

    @Test
    public void which_methods_are_always_denied_in_any_state() throws Exception {
        // arrange / given
        final DataStoreState initial= DataStoreStates.create(originDataStore);
        final OnReleaseStub onReleaseStub=new OnReleaseStub();

        // act / when
        final DataStoreState accessed=initial.access();
        final DataStoreState reserved= initial.reserve().register(onReleaseStub);
        reserved.release();
        final DataStoreState idle = onReleaseStub.releasedDataStore;

        // assert / then
        expectAlwaysDenied(initial);
        expectAlwaysDenied(accessed);
        expectAlwaysDenied(reserved);
        expectAlwaysDenied(idle);
    }

    private static void expectAlwaysDenied(DataStoreState state) {
        notPermitted(state::initialize);
    }

    @Test
    public void which_methods_are_permitted__in_created_state() throws Exception {
        // arrange / given
        final DataStoreState createdState= DataStoreStates.create(originDataStore);

        // assert / then
        assertThat("Created?", createdState, is(instanceOf(DataStoreStates.Created.class)));

        // permitted
        createdState.reserve();
        createdState.access();
        createdState.dispose();


        // not permitted
        notPermitted(createdState::release);
        notPermitted(createdState::getOriginDataStore);
        notPermitted(()->createdState.register(onRelease));
        notPermitted(()->createdState.applyDataResource(null));
    }

    @Test
    public void which_methods_are_permitted__in_accessed_state() throws Exception {
        // arrange / given
        final DataStoreState accessedState= DataStoreStates.create(originDataStore).access();

        // assert / then
        assertThat("Accessed?", accessedState, is(instanceOf(DataStoreStates.Accessed.class)));

        // not permitted
        notPermitted(accessedState::reserve);
        notPermitted(accessedState::getOriginDataStore);
        notPermitted(()->accessedState.register(onRelease));
        notPermitted(()->accessedState.applyDataResource(null));
        notPermitted(accessedState::access);
        notPermitted(accessedState::release);
        notPermitted(accessedState::dispose);
    }

    @Test
    public void which_methods_are_permitted__in_reserved_state() throws Exception {
        // arrange / given
        final DataStoreState reservedState= DataStoreStates.create(originDataStore).reserve().register(onRelease);


        // assert / then
        assertThat("Reserved?", reservedState, is(instanceOf(DataStoreStates.Reserved.class)));

        // permitted
        reservedState.register(onRelease);
        reservedState.applyDataResource(null);
        reservedState.getOriginDataStore();
        reservedState.access();
        reservedState.release();

        // not permitted
        notPermitted(reservedState::reserve);
        notPermitted(reservedState::dispose);
    }

    @Test
    public void which_methods_are_permitted__in_idle_state() throws Exception {
        // arrange / given
        final OnReleaseStub onReleaseStub=new OnReleaseStub();
        DataStoreStates.create(originDataStore).reserve().register(onReleaseStub).release();
        final DataStoreState idleState=onReleaseStub.releasedDataStore;


        // assert / then
        assertThat("Idle?", idleState, is(instanceOf(DataStoreStates.Idle.class)));

        // permitted
        idleState.reserve();
        idleState.access();
        idleState.dispose();

        // not permitted
        notPermitted(()->idleState.applyDataResource(null));
        notPermitted(()->idleState.register(onRelease));
        notPermitted(idleState::getOriginDataStore);
    }

    @Test
    public void does_belongs_always_access_the_origin_datastore() throws Exception {
        // arrange / given
        final DataStoreState dataStoreState= DataStoreStates.create(originDataStore);

        // act / when

        // assert / then
        assertThat("Belongs to MyDataStore?", dataStoreState.belongsToNamedDataStore(MyDataStore.class), is(true));
        assertThat("Belongs to OtherDataStore?", dataStoreState.belongsToNamedDataStore(OtherDataStore.class), is(false));
    }

    private static void notPermitted(ExceptionVerifier.TestAction action) {
        ExceptionVerifier.on(action)
                .expect(IllegalStateException.class)
                .expect(startsWith("Illegal State: DO NOT USE method "))
                .verify();
    }


    private static class MyDataStore extends NamedDataStore {}
    private static class OtherDataStore extends NamedDataStore {}

    private static class OnReleaseStub implements DataStoreState.OnRelease {

        private DataStoreState releasedDataStore;

        public void onRelease(DataStoreState current) {
            this.releasedDataStore = current;
        }
    }
}