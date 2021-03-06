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
package org.failearly.dataz.test.datastore;

import org.failearly.dataz.common.test.annotations.Subject;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStoreBase;
import org.failearly.dataz.datastore.DataStoreException;
import org.failearly.dataz.datastore.DataStores;
import org.failearly.dataz.datastore.MutableDataStores;
import org.failearly.dataz.resource.DataResource;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * AdhocDataStoreTest contains tests for AdhocDataStore
 */
@Subject(AdhocDataStore.class)
public class AdhocDataStoreTest {
    @Before
    public void setUp() throws Exception {
        DataStores.dispose();
    }

    @Test
    public void access_datastores__should_create_expected_datastore_type() throws Exception {
        // arrange / given
        final MutableDataStores dataStores = DataStores.reserve(MyDataStore.class);

        // act / when
        final TestDataStore tds=dataStores.getOriginDataStore(MyDataStore.class, TestDataStore.class);

        // assert / then
        assertThat(tds, is(instanceOf(TestDataStore.class)));
    }

    @AdhocDataStore(name ="first", implementation = TestDataStore.class)
    private static class MyDataStore extends NamedDataStore {
    }

    private static class TestDataStore extends DataStoreBase {
        protected TestDataStore(Class<? extends NamedDataStore> namedDataStore, Annotation dataStoreAnnotation) {
            super(namedDataStore, dataStoreAnnotation);
        }

        private static DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
            return new TestDataStore(namedDataStore, annotation);
        }

        @Override
        protected void doApplyResource(DataResource dataResource) throws DataStoreException {

        }
    }
}
