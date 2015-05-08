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
import org.failearly.dataset.config.Constants;
import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.internal.model.TestMethod;
import org.failearly.dataset.resource.DataResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;

/**
 * DataStoresTest contains tests for ... .
 */
public class DataStoresTest {

    @Before
    public void setUp() throws Exception {
        DataStores.reset();
    }

    @After
    public void tearDown() throws Exception {
        DataSetProperties.reload();
    }

    @Test
    public void loadDataStore_no_definition_annotation() throws Exception {
        // arrange / given
        final DataStore dataStore = DataStores.loadDataStore(TestClass.class);

        // assert / then
        assertThat("DataStore type?", dataStore, instanceOf(NullDataStore.class));
        assertThat("DataStore ID?", dataStore.getId(), is(Constants.DATASET_DEFAULT_DATASTORE_ID));
        assertThat("DataStore config?", dataStore.getConfig(), is("/datastore.properties"));
    }

    @Test
    public void loadDataStore_one_definition_but_no_settings() throws Exception {
        // arrange / given
        final DataStore dataStore = DataStores.loadDataStore(TestClassWithOneDefinition.class);

        // assert / then
        assertThat("DataStore type?", dataStore, instanceOf(NullDataStore.class));
        assertThat("DataStore ID?", dataStore.getId(), is(Constants.DATASET_DEFAULT_DATASTORE_ID));
        assertThat("DataStore config?", dataStore.getConfig(), is("/datastore.properties"));
    }

    @Test
    public void loadDataStore_multiple_definitions() throws Exception {
        // arrange / given
        final DataStore dataStore = DataStores.loadDataStore(TestClassWithMultiDefinitions.class);

        // assert / then
        assertThat("DataStore type?", dataStore, instanceOf(DataStoreCollection.class));
        assertThat("DataStore ID?", dataStore.getId(), is("{MASTER,CUSTOM1,CUSTOM2}"));
        assertThat("DataStore config?", dataStore.getConfig(), is("{/master.properties,/custom1.properties,/custom2.properties}"));
    }

    @Test
    public void loadDataStore_ClassHierarchy() throws Exception {
        // arrange / given
        final DataStore dataStore = DataStores.loadDataStore(SubClass.class);

        // assert / then
        assertThat("DataStore type?", dataStore, instanceOf(DataStoreCollection.class));
        assertThat("DataStore ID?", dataStore.getId(), is("{MASTER,CUSTOM1,CUSTOM2}"));
        assertThat("DataStore config?", dataStore.getConfig(), is("{/master.properties,/custom1.properties,/custom2.properties}"));
    }

    @Test
    public void loadDataStore_with_explicit_datastore_type() throws Exception {
        // arrange / given
        final DataStore dataStore = DataStores.loadDataStore(TestClassWithExplicitDataStoreType.class);

        // assert / then
        assertThat("DataStore type?", dataStore, instanceOf(AnyDataStoreImpl.class));
        assertThat("DataStore ID?", dataStore.getId(), is("DB"));
        assertThat("DataStore config?", dataStore.getConfig(), is("/my-config.props"));
    }


    @Test
    public void loadDataStore_simulating_existing_datastore() throws Exception {
        // arrange / given
        DataSetProperties.setProperty(Constants.DATASET_PROPERTY_DATASTORE_TYPE_CLASS_NAME, AnyDataStoreTypeImpl.class.getName());

        // act / when
        final DataStore dataStore = DataStores.loadDataStore(TestClass.class);

        // assert / then
        assertThat("DataStore type?", dataStore, instanceOf(AnyDataStoreImpl.class));
        assertThat("DataStore ID?", dataStore.getId(), is(Constants.DATASET_DEFAULT_DATASTORE_ID));
        assertThat("DataStore config?", dataStore.getConfig(), is("/datastore.properties"));
    }


    private static class TestClass {
    }

    @DataStoreDefinition()
    private static class TestClassWithOneDefinition {
    }

    @DataStoreDefinition(id = "MASTER", config = "/master.properties")
    @DataStoreDefinition(id = "CUSTOM1", config = "/custom1.properties")
    @DataStoreDefinition(id = "CUSTOM2", config = "/custom2.properties")
    @DataStoreDefinition(id = "CUSTOM2", config = "/never-loaded.properties")
    private static class TestClassWithMultiDefinitions {
    }

    @DataStoreDefinition(id = "MASTER", config = "/master.properties")
    private static class BaseClass {
    }

    @DataStoreDefinition(id = "MASTER", config = "/never-loaded.properties")
    @DataStoreDefinition(id = "CUSTOM1", config = "/custom1.properties")
    @DataStoreDefinition(id = "CUSTOM2", config = "/custom2.properties")
    private static class SubClass extends BaseClass {
    }

    @DataStoreDefinition(id = "DB", config = "/my-config.props", type = AnyDataStoreTypeImpl.class)
    private static class TestClassWithExplicitDataStoreType {
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