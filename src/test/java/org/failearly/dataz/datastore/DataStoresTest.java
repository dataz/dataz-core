/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.datastore;

import org.failearly.dataz.internal.common.proputils.PropertiesAccessor;
import org.failearly.dataz.common.test.ExceptionVerifier;
import org.failearly.dataz.common.test.annotations.Subject;
import org.failearly.dataz.DataSet;
import org.failearly.dataz.DataSetup;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.test.datastore.AdhocDataStore;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * DataStoresTest contains tests for ... .
 */
@Subject({DataStores.class, DataStoreBase.class})
public class DataStoresTest {

    private static final DataResource DONT_CARE = Mockito.mock(DataResource.class);

    @Before
    public void setUp() throws Exception {
        DataStores.dispose();
        TestDataStore.reset();
    }

    @Test
    public void what_is_the_datastore_lifecycle() throws Exception {
        // arrange / given

        // act / when
        final MutableDataStores dataStores = DataStores.reserve(AnyNamedDataStore.class);
        final DataStore dataStore = dataStores.getOriginDataStore(AnyNamedDataStore.class);
        dataStores.release();


        // assert / then
        assertThat(dataStore, CoreMatchers.is(instanceOf(TestDataStore.class)));
        assertThat(TestDataStore.initializedCalled(), CoreMatchers.is(1));
        assertThat(TestDataStore.disposedCalled(), CoreMatchers.is(0));
    }

    @Test
    public void what_happens_if_a_datastore_is_reserved_more_then_once() throws Exception {
        // arrange / given
        final MutableDataStores dataStores1 = DataStores.reserve(AnyNamedDataStore.class);
        dataStores1.release();

        // act / when
        final MutableDataStores dataStores2 = DataStores.reserve(AnyNamedDataStore.class);
        dataStores2.release();

        // assert / then
        assertThat("Still once initialized?", TestDataStore.initializedCalled(), CoreMatchers.is(1));
    }

    @Test
    public void what_happens_if_more_then_one_datastore_are_reserved_at_once() throws Exception {
        // arrange / given
        // act / when
        DataStores.reserve(AnyNamedDataStore.class, OtherNamedDataStore.class);

        // assert / then
        assertThat("More then once initialized?", TestDataStore.initializedCalled(), CoreMatchers.is(2));
    }

    @Test
    public void what_happens_if_released_datastores_are_disposed() throws Exception {
        // arrange / given
        final MutableDataStores dataStores = DataStores.reserve(AnyNamedDataStore.class, OtherNamedDataStore.class);
        dataStores.release();

        // act / when
        DataStores.dispose();

        // assert / then
        assertThat("Disposed?", TestDataStore.disposedCalled(), CoreMatchers.is(2));
    }

    @Test
    public void what_happens_while_disposing_one_is_not_released() throws Exception {
        // arrange / given
        DataStores.reserve(AnyNamedDataStore.class, OtherNamedDataStore.class).release();
        DataStores.reserve(OtherNamedDataStore.class); // NOT RELEASED

        // act / when
        DataStores.dispose();

        // assert / then
        assertThat("Not all should be disposed", TestDataStore.disposedCalled(), CoreMatchers.is(1));
    }

    @Test
    public void what_happens_while_disposing_all_datastores_a_last_one_will_released_after_dispose() throws Exception {
        // arrange / given
        DataStores.reserve(AnyNamedDataStore.class, OtherNamedDataStore.class).release();
        MutableDataStores notYetReleased = DataStores.reserve(AnyNamedDataStore.class);

        // and then dispose has been called
        DataStores.dispose();
        assertThat("All released datastore instances will be disposed, but not the reserved one",
                TestDataStore.disposedCalled(), CoreMatchers.is(1));

        // act / when
        notYetReleased.release();

        // assert / then
        assertThat("Even the last will be disposed", TestDataStore.disposedCalled(), CoreMatchers.is(2));
    }

    @Test
    public void what_happens_if_disposing_twice() throws Exception {
        // arrange / given
        MutableDataStores dataStores = DataStores.reserve(AnyNamedDataStore.class, OtherNamedDataStore.class);
        dataStores.release();
        DataStores.dispose();
        assertThat("Only the first will be executed?", TestDataStore.disposedCalled(), CoreMatchers.is(2));
        TestDataStore.reset();

        // act / when
        DataStores.dispose();

        // assert / then
        assertThat("The second dispose() has nothing to do?", TestDataStore.disposedCalled(), CoreMatchers.is(0));
    }

    @Test
    public void what_happens_a_named_datastore_has_multiple_DataStore_annotations_and_not_all_has_been_used() throws Exception {
        // arrange / given
        MutableDataStores first = DataStores.reserve(MultipleDataStoreAnnotations.class);
        MutableDataStores second = DataStores.reserve(MultipleDataStoreAnnotations.class);
        first.release();
        second.release();

        // act / when
        DataStores.dispose();

        // assert / then
        assertThat("Created DataStore instances?", TestDataStore.created(), CoreMatchers.is(3));
        assertThat("Initialized called for each reserved instance?", TestDataStore.initializedCalled(), CoreMatchers.is(2));
        assertThat("Only the reserved will be disposed?", TestDataStore.disposedCalled(), CoreMatchers.is(2));
    }

    @Test
    public void how_many_data_store_will_be_applied_per_named_data_store() throws Exception {
        // arrange / given
        final MutableDataStores dataStores = DataStores.reserve(MultipleDataStoreAnnotations.class);

        // act / when
        dataStores.apply((dataStore -> dataStore.applyDataResource(DONT_CARE)));

        // assert / then
        assertThat("How many applications of doApplyResource()?", TestDataStore.applyCalled(), CoreMatchers.is(1));
    }

    @Test
    public void what_happens_if_you_try_to_create_an_immutable_while_a_mutable_is_already_reserved() throws Exception {
        // arrange / given
        DataStores.reserve(AnyNamedDataStore.class);

        // act / when
        final ImmutableDataStores immutableDataStores = DataStores.access();

        // assert / then
        ExceptionVerifier.on(() -> immutableDataStores.access(dataStore -> dataStore.applyDataResource(DONT_CARE)))
                .expect(IllegalStateException.class)
                .expect("Illegal State: DO NOT USE method 'applyDataResource' in state 'Accessed'.")
                .verify();
    }

    @Test
    public void what_happens_if_you_try_to_create_an_immutable_which_on_reserved_datastore() throws Exception {
        // arrange / given
        DataStores.reserve(AnyNamedDataStore.class);

        // act / when
        final ImmutableDataStores immutableDataStores = DataStores.access();

        // assert / then
        assertThat("Access to reserved datastores?",
                immutableDataStores.getDataStore(AnyNamedDataStore.class).getNamedDataStore(),
                equalTo(AnyNamedDataStore.class)
        );
    }

    @Test
    public void what_happens_if_you_try_to_access_and_there_was_no_previous_reserve() throws Exception {
        // assert / then
        ExceptionVerifier.on(DataStores::access)
                .expect(IllegalStateException.class)
                .expect("access(): You could only access already reserved datastores.")
                .verify();
    }

    @Test
    public void what_happens_if_you_try_to_create_an_immutable_which_on_an_already_release_datastore() throws Exception {
        // arrange / given
        DataStores.reserve(AnyNamedDataStore.class).release();

        // act / when
        // assert / then
        ExceptionVerifier.on(DataStores::access)
                .expect(IllegalStateException.class)
                .expect("access(): You could only access already reserved datastores.")
                .verify();
    }

    @Test
    public void how_to_setup_a_DataStore_with_DataSetup() throws Exception {
        // act / when
        DataStores.reserve(NamedDataStoreWithDataSetup.class);

        // assert / then
        assertThat("How many applications of doApplyResource()?", TestDataStore.applyCalled(), CoreMatchers.is(3));
        assertThat("and which resources, in which order?", TestDataStore.appliedResources(),
                contains(
                        "/org/failearly/dataz/datastore/scheme.setup",
                        "/org/failearly/dataz/datastore/reference.setup",
                        "/org/failearly/dataz/datastore/other.setup"
                )
        );
    }

    @Test
    public void how_to_setup_a_DataStore_with_DataSet() throws Exception {
        // act / when
        DataStores.reserve(NamedDataStoreWithDataSet.class);

        // assert / then
        assertThat("How many applications of doApplyResource()?", TestDataStore.applyCalled(), CoreMatchers.is(3));
        assertThat("and which resources, in which order?", TestDataStore.appliedResources(),
                contains(
                        "/org/failearly/dataz/datastore/scheme.setup",
                        "/org/failearly/dataz/datastore/reference.setup",
                        "/org/failearly/dataz/datastore/other.setup"
                )
        );
    }

    @Test
    public void how_often_will_the_DataStore_setup_resource_applied_on_multiple_reservations() throws Exception {
        // arrange / given
        DataStores.reserve(NamedDataStoreWithDataSetup.class).release();

        // act / when
        DataStores.reserve(NamedDataStoreWithDataSetup.class).release();

        // assert / then
        assertThat("How many applications of doApplyResource()?", TestDataStore.applyCalled(), CoreMatchers.is(3));
        assertThat("and which resources, in which order?", TestDataStore.appliedResources(),
                contains(
                        "/org/failearly/dataz/datastore/scheme.setup",
                        "/org/failearly/dataz/datastore/reference.setup",
                        "/org/failearly/dataz/datastore/other.setup"
                )
        );
    }

    @AdhocDataStore(name = "any", implementation = TestDataStore.class)
    private static class AnyNamedDataStore extends org.failearly.dataz.NamedDataStore {
    }

    @AdhocDataStore(name = "other", implementation = TestDataStore.class)
    private static class OtherNamedDataStore extends NamedDataStore {
    }

    @AdhocDataStore(name = "1", implementation = TestDataStore.class)
    @AdhocDataStore(name = "2", implementation = TestDataStore.class)
    @AdhocDataStore(name = "3", implementation = TestDataStore.class)
    private static class MultipleDataStoreAnnotations extends NamedDataStore {
    }

    @AdhocDataStore(name = "1", implementation = TestDataStore.class)
    @AdhocDataStore(name = "2", implementation = TestDataStore.class)
    @DataSetup({"scheme.setup", "reference.setup"})
    @DataSetup({"other.setup"})
    private static class NamedDataStoreWithDataSetup extends NamedDataStore {
    }

    @AdhocDataStore(name = "any", implementation = TestDataStore.class)
    @DataSet(setup = {"scheme.setup", "reference.setup"})
    @DataSet(setup = {"other.setup"})
    private static class NamedDataStoreWithDataSet extends NamedDataStore {
    }

    private static class TestDataStore extends DataStoreBase {
        private final static AtomicInteger created = new AtomicInteger(0);
        private final static AtomicInteger initializedCalled = new AtomicInteger(0);
        private final static AtomicInteger disposedCalled = new AtomicInteger(0);

        private final static AtomicInteger applyCalled = new AtomicInteger(0);
        private final static List<String> appliedResources = new LinkedList<>();

        @SuppressWarnings("unused")
        static DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
            return new TestDataStore(namedDataStore, annotation);
        }

        private TestDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
            super(namedDataStore, annotation);
            created.incrementAndGet();
        }

        @Override
        public void initialize() throws DataStoreException {
            initializedCalled.incrementAndGet();
            super.initialize();
        }

        @Override
        protected void doEstablishConnection(PropertiesAccessor properties) throws Exception {
            // nothing to do
        }

        @Override
        protected void postDispose() {
            disposedCalled.incrementAndGet();
        }

        static void reset() {
            initializedCalled.set(0);
            disposedCalled.set(0);
            created.set(0);
            applyCalled.set(0);
            appliedResources.clear();
        }

        @Override
        protected void doApplyResource(DataResource dataResource) {
            applyCalled.incrementAndGet();
            if (dataResource != null)
                appliedResources.add(dataResource.getResource());
        }


        static int created() {
            return created.get();
        }

        static int disposedCalled() {
            return disposedCalled.get();
        }

        static int initializedCalled() {
            return initializedCalled.get();
        }

        static int applyCalled() {
            return applyCalled.get();
        }

        static List<String> appliedResources() {
            return appliedResources;
        }
    }
}