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

package org.failearly.dataz.internal.datastore;

import org.failearly.dataz.common.test.annotations.Subject;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.AbstractDataStore;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStoreException;
import org.failearly.dataz.internal.datastore.state.DataStoreState;
import org.failearly.dataz.test.datastore.AdhocDataStore;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.failearly.dataz.common.test.threadsafety.ThreadSafetyVerifier.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * DataStoresHolderTest tests the thread safety of {@link DataStoresHolder}.
 */
@Subject(DataStoresHolder.class)
public class DataStoresHolderTest {

    private static final int NO_DELAY = 0;

    private static String blockToVerify(DataStoresHolder dataStoresHolder, int delay) throws InterruptedException {
        final DataStoreState dss = dataStoresHolder.reserve();

        // Simulate the usage of a reserved datastore (execution of the test)
        Thread.sleep(delay);

        final String id = dss.getId();
        dss.release();

        return id;
    }

    @Test
    public void what_happens__if_used_in_single_thread() throws Exception {
        // arrange / given
        final DataStoresHolder dataStoresHolder = createDataStoreHolder(MultipleDataStoreAnnotations.class);
        final Set<String> ids=new HashSet<>();

        // act / when
        ids.add(blockToVerify(dataStoresHolder, NO_DELAY));
        ids.add(blockToVerify(dataStoresHolder, NO_DELAY));
        ids.add(blockToVerify(dataStoresHolder, NO_DELAY));
        ids.add(blockToVerify(dataStoresHolder, NO_DELAY));


        // assert / then
        final Set<String> expectedDataStoreIds = expectedDataStores("1st@MultipleDataStoreAnnotations");
        assertThat("Only the first will be used?", ids, is(expectedDataStoreIds));
    }

    @Test
    public void what_happens__if_only_one_DataStore_is_available__but_multiple_threads_uses_them() throws Throwable {
        // arrange / given
        final DataStoresHolder dataStoresHolder = createDataStoreHolder(AnyNamedDataStore.class);
        final Set<String> expectedDataStoreIds = expectedDataStores("any@AnyNamedDataStore");
        final int anyNumberOfThreads = 5;

        // act / when
        // assert / then
        given(String.class, () -> dataStoresHolder)
                .repeat(10)
                .threads(anyNumberOfThreads)
                .when((dsh) -> DataStoresHolderTest.blockToVerify(dsh, 10))
                .thenAssertResultSet(ids -> assertThat(ids, is(expectedDataStoreIds)))
                .verify();
    }

    @Test
    public void what_happens__if_multiple_DataStores_are_available__but_less_threads_then_datastore_instances_are_available() throws Throwable {
        // arrange / given
        final DataStoresHolder dataStoresHolder = createDataStoreHolder(MultipleDataStoreAnnotations.class);
        final Set<String> expectedDataStoreIds = expectedDataStores("1st@MultipleDataStoreAnnotations", "2nd@MultipleDataStoreAnnotations");
        final int numberOfThreads = expectedDataStoreIds.size();

        // act / when
        // assert / then
        given(String.class, () -> dataStoresHolder)
                .repeat(10)
                .threads(numberOfThreads)
                .when((dsh) -> DataStoresHolderTest.blockToVerify(dsh, 10))
                .thenAssertResultSet(ids -> assertThat(ids, is(expectedDataStoreIds)))
                .verify();
    }



    @Test
    public void what_happens__if_multiple_DataStores_are_available__but_more_threads_then_datastore_instances_are_available() throws Throwable {
        // arrange / given
        final DataStoresHolder dataStoresHolder = createDataStoreHolder(MultipleDataStoreAnnotations.class);
        final Set<String> expectedDataStoreIds = expectedDataStores(
                "1st@MultipleDataStoreAnnotations",
                "2nd@MultipleDataStoreAnnotations",
                "3rd@MultipleDataStoreAnnotations"
        );
        final int numberOfThreads = expectedDataStoreIds.size() * 2 + 1;

        // act / when
        // assert / then
        given(String.class, () -> dataStoresHolder)
                .repeat(10)
                .threads(numberOfThreads)
                .when((dsh) -> DataStoresHolderTest.blockToVerify(dsh, 5))
                .thenAssertResultSet(ids -> assertThat(ids, is(expectedDataStoreIds)))
                .verify();
    }

    @Test
    @Ignore("Only test on your local machine")
    public void what_happens_under_stress() throws Throwable {
        // arrange / given
        final Random random = new Random(1L);
        final DataStoresHolder dataStoresHolder = createDataStoreHolder(MultipleDataStoreAnnotations.class);
        final Set<String> expectedDataStoreIds = expectedDataStores(
                "1st@MultipleDataStoreAnnotations",
                "2nd@MultipleDataStoreAnnotations",
                "3rd@MultipleDataStoreAnnotations"
        );
        final int numberOfThreads = expectedDataStoreIds.size() * 5 + 1;

        // act / when
        // assert / then
        given(String.class, () -> dataStoresHolder)
                .repeat(10_000)
                .threads(numberOfThreads)
                .when((dsh) -> DataStoresHolderTest.blockToVerify(dsh, random.nextInt(100)))
                .thenAssertResultSet(ids -> assertThat(ids, is(expectedDataStoreIds)))
                .verify();
    }

    private static Set<String> expectedDataStores(String... ids) {
        return toIdSet(Arrays.asList(ids));
    }

    private static Set<String> toIdSet(List<String> ids) {
        return new HashSet<>(ids);
    }

    private static DataStoresHolder createDataStoreHolder(Class<? extends NamedDataStore> namedDataStore) {
        return DataStoresHolder.createDataStoresHolder(namedDataStore, createDataStores(namedDataStore));
    }


    private static List<DataStore> createDataStores(Class<? extends NamedDataStore> namedDataStore) {
        return Arrays.stream(namedDataStore.getDeclaredAnnotationsByType(AdhocDataStore.class))
                .map((ann) -> TestDataStore.createDataStore(namedDataStore, ann))
                .collect(Collectors.toList());
    }

    @AdhocDataStore(name = "any", implementation = TestDataStore.class)
    private static class AnyNamedDataStore extends NamedDataStore {
    }

    @AdhocDataStore(name = "1st", implementation = TestDataStore.class)
    @AdhocDataStore(name = "2nd", implementation = TestDataStore.class)
    @AdhocDataStore(name = "3rd", implementation = TestDataStore.class)
    private static class MultipleDataStoreAnnotations extends NamedDataStore {
    }

    public static class TestDataStore extends AbstractDataStore {
        private final Class<? extends NamedDataStore> namedDataStore;
        private final AdhocDataStore annotation;

        @SuppressWarnings("unused")
        static DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
            return new TestDataStore(namedDataStore, annotation);
        }

        private TestDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
            this.namedDataStore = namedDataStore;
            this.annotation = annotation;
        }

        @Override
        public Class<? extends NamedDataStore> getNamedDataStore() {
            return namedDataStore;
        }

        @Override
        public String getId() {
            return this.annotation.name() + "@" + this.namedDataStore.getSimpleName();
        }

        @Override
        public void initialize() throws DataStoreException {
        }
    }

}