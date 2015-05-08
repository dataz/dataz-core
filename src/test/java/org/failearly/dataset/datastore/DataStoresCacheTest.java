/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.datastore;

import org.failearly.dataset.DataStoreDefinition;
import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.internal.model.TestMethod;
import org.failearly.dataset.resource.DataResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * DataStoresTest contains tests for ... .
 */
public class DataStoresCacheTest {

    @Before
    public void setUp() throws Exception {
        DataStores.reset();
    }

    @After
    public void tearDown() throws Exception {
        DataSetProperties.reload();
    }

    @Test
    public void load_multiple_times__but_initialize_only_once() throws Exception {
        // arrange / given
        final DataStore dataStore1 = DataStores.loadDataStore(TestClass1.class);

        // act / when
        final DataStore dataStore2 = DataStores.loadDataStore(TestClass1.class);

        // assert / then
        assertThat("Same instance?", dataStore2, sameInstance(dataStore1));
        verify(dataStore1).initialize();
        verifyNoMoreInteractions(dataStore1);
    }

    @Test
    public void dispose_only_once() throws Exception {
        // arrange / given
        final DataStore dataStore = DataStores.loadDataStore(TestClass1.class);

        // act / when
        DataStores.dispose();
        DataStores.dispose();

        // assert / then
        verify(dataStore).dispose();
    }

    @Test
    public void multiple_data_stores_with_same_id_first_datastore_wins() throws Exception {
        // arrange / given
        final DataStore dataStore1 = DataStores.loadDataStore(TestClass1.class);

        // act / when
        final DataStore dataStore2 = DataStores.loadDataStore(TestClass2.class);

        // assert / then
        assertThat("Same instance?", dataStore2, sameInstance(dataStore1));
        verify(dataStore1).initialize();
    }

    @Test
    public void multiple_data_stores_with_same_config_different_config() throws Exception {
        // arrange / given
        final DataStore dataStore1 = DataStores.loadDataStore(TestClass1.class);

        // act / when
        final DataStore dataStore2 = DataStores.loadDataStore(TestClass3.class);

        // assert / then
        assertThat("new datastore instance?", dataStore2, not(sameInstance(dataStore1)));
        verify(dataStore1).initialize();
        verify(dataStore2).initialize();
    }

    @DataStoreDefinition(id = "DB", config = "/my-config.props", type = AnyDataStoreTypeImpl.class)
    private static class TestClass1 {
    }

    @DataStoreDefinition(id = "DB", config = "/my-other-config.props")
    private static class TestClass2 {
    }

    @DataStoreDefinition(id = "OTHER-ID", config = "/my-other-config.props", type = AnyDataStoreTypeImpl.class)
    private static class TestClass3 {
    }


    public static class AnyDataStoreTypeImpl implements DataStoreType {
        @Override
        public DataStore createDataStore(DataStoreDefinition annotation, Object context) {
            return spy(new AnyDataStoreImpl(annotation.config(), annotation.id()));
        }
    }

    private static class AnyDataStoreImpl extends DataStoreBase {
        AnyDataStoreImpl(String dataStoreConfig, String dataStoreId) {
            super(dataStoreId, dataStoreConfig);
        }

        @Override
        public void initialize() throws DataStoreException {
        }

        @Override
        public void setup(TestMethod testMethod) throws DataStoreException {
        }

        @Override
        public void cleanup(TestMethod testMethod) {
        }

        @Override
        protected void doApplyResource(DataResource dataResource) throws DataStoreException {
        }

        @Override
        public void dispose() {
        }
    }

}