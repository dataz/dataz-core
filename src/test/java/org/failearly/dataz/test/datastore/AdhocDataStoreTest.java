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
package org.failearly.dataz.test.datastore;

import org.failearly.common.test.annotations.Subject;
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
