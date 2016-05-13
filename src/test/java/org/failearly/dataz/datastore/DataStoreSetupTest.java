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

package org.failearly.dataz.datastore;

import org.failearly.dataz.AdhocDataStore;
import org.failearly.dataz.DataStoreSetup;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.template.generator.ListGenerator;
import org.failearly.dataz.template.generator.RandomRangeGenerator;
import org.failearly.dataz.test.CoreTestUtils;
import org.failearly.dataz.test.DataResourceMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;

/**
 * DataStoreSetupTest contains tests for {@link org.failearly.dataz.DataStoreSetup} on {@link org.failearly.dataz.datastore.DataStores}.
 */
@SuppressWarnings("unchecked")
public class DataStoreSetupTest {

    @Before
    @After
    public void setUp() throws Exception {
        DataStores.reset();
    }

    @Test
    public void loadDataStore_apply_each_data_store_setup_resource_once() throws Exception {
        // arrange / given
        DataStores.loadDataStore(MyTestClass.class);

        // act / when
        DataStores.loadDataStore(DataStoreSetupWithOmmitedName.class); // Ignored (because, same data store).

        // assert / then
        assertDataStoreSetupResources("master",
                DataResourceMatchers.isDataResource("master", "master", "/master-schema.sql") //
                , DataResourceMatchers.isDataResource("master", "master", "/org/failearly/dataz/datastore/master-common.sql") //

        );
        assertDataStoreSetupResources("slave",
                DataResourceMatchers.isDataResource("slave", "slave", "/slave-schema.sql") //
        );
    }

    @Test
    public void loadDataStore_apply_each_data_store_cleanup_resource_once() throws Exception {
        // arrange / given
        DataStores.loadDataStore(MyTestClass.class);
        DataStores.loadDataStore(DataStoreSetupWithOmmitedName.class);
        final TestDataStore master = prepareDataStoreForDisposeTest("master");
        final TestDataStore slave = prepareDataStoreForDisposeTest("slave");

        // act / when
        DataStores.dispose();
        DataStores.dispose();

        // assert / then
        assertDataStoreSetupResources(master, //
                DataResourceMatchers.isDataResource("master", "master", "/org/failearly/dataz/datastore/cleanup-master-common.sql") //
                , DataResourceMatchers.isDataResource("master", "master", "/drop-master-schema.sql") //
        );
        assertDataStoreSetupResources(slave /* slave has no cleanup resources */);
    }

    @Test
    public void loadDataStore_with_omitted_name__should_use_data_store_id() throws Exception {
        // act / when
        DataStores.loadDataStore(DataStoreSetupWithOmmitedName.class);

        // assert / then
        assertDataStoreSetupResources("master",
                DataResourceMatchers.isDataResource("master", "master", "/other-master-schema.sql")
        );
        assertDataStoreSetupResources("slave",
                DataResourceMatchers.isDataResource("slave", "slave", "/other-slave-schema.sql")

        );
    }

    @Test
    public void loadDataStore_on_class_hierachy__check_setup_resources() throws Exception {
        // act / when
        DataStores.loadDataStore(DerivedTestClass.class);

        // assert / then
        assertDataStoreSetupResources("master",
                DataResourceMatchers.isDataResource("master", "master", "/master-schema.sql") //
                , DataResourceMatchers.isDataResource("master", "master", "/org/failearly/dataz/datastore/master-common.sql") //
                , DataResourceMatchers.isDataResource("master", "master", "/master-additional.sql") //

        );
    }

    @Test
    public void loadDataStore_on_class_hierachy__check_cleanup_resources() throws Exception {
        // arrange / given
        DataStores.loadDataStore(DerivedTestClass.class);
        final TestDataStore master = prepareDataStoreForDisposeTest("master");

        // act / when
        DataStores.dispose();

        // assert / then
        assertDataStoreSetupResources(master,
                DataResourceMatchers.isDataResource("master", "master", "/master-cleanup-additional.sql") //
                , DataResourceMatchers.isDataResource("master", "master", "/org/failearly/dataz/datastore/cleanup-master-common.sql") //
                , DataResourceMatchers.isDataResource("master", "master", "/drop-master-schema.sql") //
        );
    }

    @Test
    public void loadDataStore_check_setup_content() throws Exception {
        // act / when
        DataStores.loadDataStore(ClassWithExistingResources.class);

        // assert / then
        assertDataStoreSetupResourceContent("master",
                "create master schema\n"
        );
    }

    @Test
    public void loadDataStore_check_cleanup_content() throws Exception {
        // arrange / given
        DataStores.loadDataStore(ClassWithExistingResources.class);
        final TestDataStore master = prepareDataStoreForDisposeTest("master");

        // act / when
        DataStores.dispose();

        // assert / then
        assertDataStoreSetupResourceContent(master,
                "Drop master schema\n"
        );
    }

    @Test
    public void loadDataStore_check_setup_content_template() throws Exception {
        // act / when
        DataStores.loadDataStore(ClassWithExistingTemplateResources.class);

        // assert / then
        assertDataStoreSetupResourceContent("master",
                "-- Template master-common-setup.sql.vm\n    INSERT INTO USERS (ID,NAME) VALUES (1569548985,'Frank K.');\n    INSERT INTO USERS (ID,NAME) VALUES (215764588,'Marko U.');\n"
        );
    }

    @Test
    public void loadDataStore_check_cleanup_content_template() throws Exception {
        // arrange / given
        DataStores.loadDataStore(ClassWithExistingTemplateResources.class);
        final TestDataStore master = prepareDataStoreForDisposeTest("master");

        // act / when
        DataStores.dispose();

        // assert / then
        assertDataStoreSetupResourceContent(master,
                "-- Template master-common-cleanup.sql.vm\n    DELETE FROM USERS WHERE NAME = 'Frank K.';\n    DELETE FROM USERS WHERE NAME = 'Marko U.';\n"
        );
    }

    private static void assertDataStoreSetupResources(String dataStoreId, Matcher<DataResource>... expectedResources) {
        final TestDataStore dataStore = (TestDataStore) DataStores.getDataStore(dataStoreId);
        assertThat("DataStore '" + dataStoreId + "' exists?", dataStore, Matchers.notNullValue());
        assertDataStoreSetupResources(dataStore, expectedResources);
    }

    private static void assertDataStoreSetupResourceContent(String dataStoreId, String... expectedContent) {
        final TestDataStore dataStore = (TestDataStore) DataStores.getDataStore(dataStoreId);
        assertThat("DataStore '" + dataStoreId + "' exists?", dataStore, Matchers.notNullValue());
        assertDataStoreSetupResourceContent(dataStore, expectedContent);
    }

    private static void assertDataStoreSetupResourceContent(TestDataStore dataStore, String... expectedContent) {
        assertThat("Resource content(s) of " + dataStore.getId() + "?", dataStore.dataSetResourceContent(), Matchers.contains(expectedContent));
    }

    private static void assertDataStoreSetupResources(TestDataStore dataStore, Matcher<DataResource>... expectedResources) {
        if (expectedResources.length > 0)
            assertThat("Resources of " + dataStore.getId() + "?", dataStore.getDataResources(), Matchers.contains(expectedResources));
        assertThat("#Resources of " + dataStore.getId() + "?", dataStore.getDataResources(), Matchers.hasSize(expectedResources.length));
    }


    private static TestDataStore prepareDataStoreForDisposeTest(String dataStoreId) {
        final TestDataStore dataStore = (TestDataStore) DataStores.getDataStore(dataStoreId);
        dataStore.reset();
        return dataStore;
    }

    @AdhocDataStore(id = "master", type = TestDataStoreType.class)
    @AdhocDataStore(id = "slave", type = TestDataStoreType.class)
    @DataStoreSetup(name = "master", datastore = "master", setup = {"/master-schema.sql", "master-common.sql"}, cleanup = {"/drop-master-schema.sql", "cleanup-master-common.sql"})
    @DataStoreSetup(name = "slave", datastore = "slave", setup = "/slave-schema.sql")
    private static class MyTestClass {
    }

    @AdhocDataStore(id = "master", type = TestDataStoreType.class)
    @AdhocDataStore(id = "slave", type = TestDataStoreType.class)
    @DataStoreSetup(datastore = "master", setup = {"/other-master-schema.sql"})
    @DataStoreSetup(datastore = "slave", setup = "/other-slave-schema.sql")
    private static class DataStoreSetupWithOmmitedName {
    }

    @AdhocDataStore(id = "master", type = TestDataStoreType.class)
    @DataStoreSetup(name = "master", datastore = "master", setup = {"/master-schema.sql", "master-common.sql"}, cleanup = {"/drop-master-schema.sql", "cleanup-master-common.sql"})
    private static class TestBaseClass {
    }

    @DataStoreSetup(name = "master", datastore = "master", setup = {"/master-additional.sql"}, cleanup = {"/master-cleanup-additional.sql"})
    private static class DerivedTestClass extends TestBaseClass {
    }

    @AdhocDataStore(id = "master", type = TestDataStoreType.class)
    @DataStoreSetup(name = "master", datastore = "master", setup = {"/master-schema.sql"}, cleanup = {"/drop-master-schema.sql"})
    private static class ClassWithExistingResources {
    }

    @AdhocDataStore(id = "master", type = TestDataStoreType.class)
    @DataStoreSetup(name = "master", datastore = "master", setup = {"/master-common-setup.sql.vm"}, cleanup = {"/master-common-cleanup.sql.vm"})
    @ListGenerator(dataset = "master", name = "users", values = {"Frank K.", "Marko U."})
    @RandomRangeGenerator(dataset = "master", name = "ids", seed = 1)
    private static class ClassWithExistingTemplateResources {
    }


    public static class TestDataStoreType implements DataStoreType {
        @Override
        public DataStore createDataStore(AdhocDataStore annotation, Object context) {
            return new TestDataStore(annotation.id(), annotation.config());
        }
    }

    // Needs to public because of Mockito.
    public static class TestDataStore extends DataStoreBase {
        private final List<DataResource> dataResources = new LinkedList<>();

        TestDataStore(String dataStoreId, String dataStoreConfigFile) {
            super(dataStoreId, dataStoreConfigFile);
        }

        List<DataResource> getDataResources() {
            return dataResources;
        }

        @Override
        public void initialize() throws DataStoreException {
        }

        @Override
        public void dispose() {
        }

        @Override
        protected void doApplyResource(DataResource dataResource) {
            dataResources.add(dataResource);
        }


        void reset() {
            dataResources.clear();
        }

        List<String> dataSetResourceContent() {
            return dataResources.stream()
                    .map(DataResource::open)
                    .map(CoreTestUtils::inputStreamToString)
                    .collect(Collectors.toList());
        }
    }
}
