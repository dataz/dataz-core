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

package org.failearly.dataz.datastore;

import org.failearly.dataz.internal.common.proputils.PropertiesAccessor;
import org.failearly.dataz.common.test.ExceptionVerifier;
import org.failearly.dataz.common.test.annotations.Subject;
import org.failearly.dataz.DataCleanup;
import org.failearly.dataz.DataSet;
import org.failearly.dataz.DataSetup;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.common.Property;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.template.simple.Constant;
import org.failearly.dataz.test.datastore.AdhocDataStore;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

import static org.failearly.dataz.test.CoreTestUtils.inputStreamToString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * DataStoreBaseTest contains tests for ... .
 */
@Subject({DataStoreBase.class})
public class DataStoreBaseTest {

    private static final PropertiesAccessor EMPTY_PROPERTIES = new PropertiesAccessor(new Properties());
    private static final DataResource DATA_RESOURCE = Mockito.mock(DataResource.class);

    @BeforeClass
    public static void setUp() throws Exception {
        DataSetProperties.reload();
        DataSetProperties.setProperty(Constants.DATAZ_PROPERTY_DEFAULT_SETUP_SUFFIX, "setup.sql.vm");
        DataSetProperties.setProperty(Constants.DATAZ_PROPERTY_DEFAULT_CLEANUP_SUFFIX, "cleanup.sql.vm");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        DataSetProperties.reload();
    }

    @Test
    public void what_is_the_state_of_a_DataStore_before_initialization() throws Exception {
        // arrange / given
        // act / when
        final DataStore dataStore=createDataStore(AnyNamedDataStore.class);

        // assert / then
        assertThat("Assigned NamedDataStore?", dataStore.getNamedDataStore(), equalTo(AnyNamedDataStore.class));
        assertThat("Name?", dataStore.getName(), is("any"));
        assertThat("Id?", dataStore.getId(), is("AnyNamedDataStore[name=any]@TestDataStore"));
        assertThat("Property File?", dataStore.getConfigFile(), is("any-config.properties"));
        assertThat("No Properties?", dataStore.getProperties(), is(EMPTY_PROPERTIES));

        ExceptionVerifier.on(()->dataStore.applyDataResource(DATA_RESOURCE))
                .expect(IllegalStateException.class)
                .expect("AnyNamedDataStore[name=any]@TestDataStore not yet initialized!")
                .verify();
    }

    @Test
    public void have_all_properties_been_read_in_the_expected_order() throws Exception {
        // arrange / given
        final DataStore dataStore=createDataStore(AnyNamedDataStore.class);

        // act / when
        dataStore.initialize();

        // assert / then
        final PropertiesAccessor properties = dataStore.getProperties();
        assertThat("File properties read?", properties.getStringValue("prop.from.file"), is("this-is-a-property-from-any-config.properties"));
        assertThat("Annotation properties read?", properties.getStringValue("annotation.prop"), is("impl-property-value"));
        assertThat("Use of impl property?", properties.getStringValue("use.annotation.property"), is("Use of impl-property-value"));

    }

    @Test
    public void what_happens_while_initialization() throws Exception {
        // arrange / given
        final TestDataStore dataStore=createDataStore(AnyNamedDataStore.class);

        // act / when
        dataStore.initialize();

        // assert / then
        final PropertiesAccessor dataStoreProperties = dataStore.getProperties();
        assertThat("Properties set?", dataStoreProperties, not(EMPTY_PROPERTIES));
        verify(dataStore).doEstablishConnection(dataStoreProperties);
        verify(dataStore, times(2)).doApplyResource(notNull());
    }

    @Test
    public void which_resources_will_be_applied_during_initialization() throws Exception {
        // arrange / given
        final Class<AnyNamedDataStore> namedDataStore = AnyNamedDataStore.class;
        final TestDataStore dataStore=createDataStore(namedDataStore);

        // act / when
        dataStore.initialize();

        // assert / then
        assertAssignedNamedDataStore(dataStore, namedDataStore);
        assertDataSetNames(dataStore, "1st", "2nd");
        assertDataResources(dataStore,
                "/org/failearly/dataz/datastore/any-existing-resource.setup",
                "/org/failearly/dataz/datastore/any-existing-resource.setup.vm"
        );
        assertDataResourcesContent(dataStore, "## No content at all ##\n", "any value\n");
    }

    @Test
    public void what_happens_on_initialization_if_a_DataResource_fails() throws Exception {
        // arrange / given
        final TestDataStore dataStore=createDataStore(FailingInitialization.class);


        // act / when
        ExceptionVerifier.on(dataStore::initialize)
                .expect(DataStoreException.class)
                .verify();

        // assert / then
        assertDataSetNames(dataStore, "failing");
    }

    @Test
    public void what_happens_on_initialization_if_a_DataResource_fails_but_marked_to_be_ignored() throws Exception {
        // arrange / given
        final TestDataStore dataStore=createDataStore(FailingButIgnored.class);

        // act / when
        dataStore.initialize();

        // assert / then
        assertDataSetNames(dataStore, "failing-init", "failing-dispose");
    }

    @Test
    public void which_resources_will_be_applied_during_disposing() throws Exception {
        // arrange / given
        final Class<? extends NamedDataStore> namedDataStore = AnyNamedDataStore.class;
        final TestDataStore dataStore = prepareDataStoreForDisposing(namedDataStore);

        // act / when
        dataStore.dispose();

        // assert / then
        assertAssignedNamedDataStore(dataStore, namedDataStore);
        assertDataSetNames(dataStore, "2nd", "1st");
        assertDataResources(dataStore,
                "/org/failearly/dataz/datastore/any-existing-resource.cleanup.vm",
                "/org/failearly/dataz/datastore/any-existing-resource.cleanup"
        );
        assertDataResourcesContent(dataStore, "any value\n", "## No content at all ##\n");
    }

    @Test
    public void what_happens_on_disposing_if_a_DataResource_fails() throws Exception {
        // arrange / given
        final Class<? extends NamedDataStore> namedDataStore = FailingDisposing.class;
        final TestDataStore dataStore = prepareDataStoreForDisposing(namedDataStore);

        // act / when
        // assert / then
        ExceptionVerifier.on(dataStore::dispose)
                .expect(DataStoreException.class)
                .verify();

        assertAssignedNamedDataStore(dataStore, namedDataStore);
        assertDataSetNames(dataStore, "failing");
    }

    @Test
    public void what_happens_on_disposing_if_a_DataResource_fails__but_marked_to_be_ignored() throws Exception {
        // arrange / given
        final TestDataStore dataStore = prepareDataStoreForDisposing(FailingButIgnored.class);

        // act / when
        dataStore.dispose();

        // assert / then
        assertDataSetNames(dataStore, "failing-dispose", "failing-init");
    }

    @Test
    public void what_are_default_settings_for_initialization() throws Exception {
        // arrange / given
        final TestDataStore dataStore=createDataStore(TestingDefaultsDataStore.class);

        // act / when
        dataStore.initialize();

        // assert / then
        assertAssignedNamedDataStore(dataStore, TestingDefaultsDataStore.class);
        assertDataSetNames(dataStore, Constants.DATASET_DEFAULT_NAME);
        assertDataResources(dataStore, "/org/failearly/dataz/datastore/TestingDefaultsDataStore.setup.sql.vm");
        assertDataResourcesContent(dataStore, "any value\n");
    }

    @Test
    public void what_are_default_settings_for_disposing() throws Exception {
        // arrange / given
        final TestDataStore dataStore=prepareDataStoreForDisposing(TestingDefaultsDataStore.class);

        // act / when
        dataStore.dispose();

        // assert / then
        assertAssignedNamedDataStore(dataStore, TestingDefaultsDataStore.class);
        assertDataSetNames(dataStore, Constants.DATASET_DEFAULT_NAME);
        assertDataResources(dataStore, "/org/failearly/dataz/datastore/TestingDefaultsDataStore.cleanup.sql.vm");
        assertDataResourcesContent(dataStore, "any value\n");
    }

    private TestDataStore prepareDataStoreForDisposing(Class<? extends NamedDataStore> namedDataStore) {
        final TestDataStore dataStore=createDataStore(namedDataStore);
        dataStore.initialize();
        dataStore.reset();
        return dataStore;
    }

    private void assertDataResourcesContent(TestDataStore dataStore, String... expectedContent) {
        assertThat("Content?", dataStore.dataResourcesContent, is(Arrays.asList(expectedContent)));
    }


    private void assertDataResources(TestDataStore dataStore, String... expectedResources) {
        final List<String> resources=dataStore.dataResources.stream()  //
                .map(DataResource::getResource)                        //
                .collect(Collectors.toList());

        assertThat("All resources in expected order?", resources, is(Arrays.asList(expectedResources)));
    }

    private void assertDataSetNames(TestDataStore dataStore, String... expectedDataSetNames) {
        final List<String> resources=dataStore.dataResources.stream()  //
                .map(DataResource::getDataSetName)                        //
                .collect(Collectors.toList());

        assertThat("All datasets in expected order?", resources, is(Arrays.asList(expectedDataSetNames)));
    }

    private void assertAssignedNamedDataStore(TestDataStore dataStore, Class<? extends NamedDataStore> expectedNamedDataStore) {
        final Set<Class<? extends NamedDataStore>> namedDataStores=dataStore.dataResources.stream() //
                .map(DataResource::getNamedDataStore)                   //
                .collect(Collectors.toSet());

        assertThat("Assigned to NamedDataStore?", namedDataStores, is(Collections.singleton(expectedNamedDataStore)));
    }


    private static TestDataStore createDataStore(Class<? extends NamedDataStore> namedDataStore) {
        return spy(new TestDataStore(namedDataStore, namedDataStore.getAnnotation(AdhocDataStore.class)));
    }

    /**
     * The Test subject.
     */
    private static class TestDataStore extends DataStoreBase {

        private List<DataResource> dataResources=new LinkedList<>();
        private List<String> dataResourcesContent=new LinkedList<>();

        private TestDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore dataStoreAnnotation) {
            super(namedDataStore, dataStoreAnnotation);
        }

        void reset() {
            dataResources.clear();
            dataResourcesContent.clear();
        }

        @Override
        protected void doEstablishConnection(PropertiesAccessor properties) throws Exception {
        }

        @Override
        protected void doApplyResource(DataResource dataResource) throws DataStoreException {
            dataResources.add(dataResource);

            final String content = inputStreamToString(dataResource.open());
            dataResourcesContent.add(content);

            causeProcessingFailure(dataResource, content);
        }


        private static void causeProcessingFailure(DataResource dataResource, String content) {
            if( content.contains("FAIL") ) {
                throw new RuntimeException("Resource " + dataResource.getResource() + " should cause failure!");
            }
        }
    }


// ---------------------------------
//  Test Fixtures (Named DataStore)
// ---------------------------------
    @SuppressWarnings("DefaultAnnotationParam")
    @AdhocDataStore(
            name="any",
            config = "any-config.properties",
            properties = {@Property(k = "annotation.prop", v = "impl-property-value")}
    )
    @DataSet(name = "1st", datastores = MustBeIgnored.class, setup = "any-existing-resource.setup", cleanup = "any-existing-resource.cleanup")
    @DataSet(name = "2nd", datastores = {/*no assigned datastore*/}, setup = "any-existing-resource.setup.vm", cleanup = "any-existing-resource.cleanup.vm")
    @Constant(datasets = "2nd", name="value", value = "any value")
    private static class AnyNamedDataStore extends NamedDataStore {}

    @AdhocDataStore(name = "testing-defaults")
    @DataSetup
    @DataCleanup
    @Constant(name="value", value = "any value")
    private static class TestingDefaultsDataStore extends NamedDataStore {}


    @AdhocDataStore(name = "failing-init")
    @DataSet(name = "failing", setup = "failing-resource.setup", cleanup = "failing-resource.cleanup", failOnError = true)
    @DataSet(name = "ok",      setup = "any-existing-resource.setup")
    private static class FailingInitialization extends NamedDataStore {}

    @AdhocDataStore(name = "failing-dispose")
    @DataSet(name = "ok", setup = "dont-care.setup", cleanup = "any-existing-resource.cleanup")
    @DataSet(name = "failing",      setup = "dont-care.setup", cleanup = "failing-resource.cleanup", failOnError = true)
    private static class FailingDisposing extends NamedDataStore {}

    @AdhocDataStore(name = "failing-but-ignored")
    @DataSet(name = "failing-init",         setup = "failing-resource.setup",     cleanup = "any-existing-resource.cleanup", failOnError = false)
    @DataSet(name = "failing-dispose",      setup = "any-existing-resource.setup",cleanup = "failing-resource.cleanup",      failOnError = false)
    private static class FailingButIgnored extends NamedDataStore {}


    private static class MustBeIgnored extends NamedDataStore {}


}