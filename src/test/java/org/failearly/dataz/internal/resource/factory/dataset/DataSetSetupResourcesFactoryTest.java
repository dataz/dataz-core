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

package org.failearly.dataz.internal.resource.factory.dataset;

import org.failearly.common.test.annotations.Subject;
import org.failearly.dataz.DataSet;
import org.failearly.dataz.internal.resource.factory.DataResourcesFactoryTestBase;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.test.DataResourceMatcherBuilder;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.List;

/**
 * SetupResourcesFactoryTest contains tests for {@link DataSet#setup()} and {@link DataSetSetupResourcesFactory}.
 */
@Subject({DataSet.class, DataSetSetupResourcesFactory.class})
public class DataSetSetupResourcesFactoryTest extends DataResourcesFactoryTestBase<DataSet, DataSetSetupResourcesFactory> {

    public DataSetSetupResourcesFactoryTest() {
        super(DataSet.class, new DataSetSetupResourcesFactory(), AnyClass.class);
    }

    @Test
    public void what_happens_with_default_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("defaultSettings");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                              //
            isDefaultDataResource("AnyClass-defaultSettings.setup")//
        );
    }

    @Test
    public void what_happens_if_using_none_default_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("noneDefaultSettings");

        // assert / then
        assertResolvedDataResources(dataResources, isFullCustomizedDataResource());
    }

    private Matcher<DataResource> isFullCustomizedDataResource() {
        return DataResourceMatcherBuilder.createWithDefaults()
            .withNamedDataStore(OtherDataStore.class)
            .withDataSetName(OTHER_DATA_SET_NAME)
            .withResource("/any-resource.setup")
            .withTransactional(false)
            .withFailOnError(false)
            .build();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void what_happens_if_using_multiple_resources() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("multipleResources");

        // assert / then
        assertResolvedDataResources(dataResources,                        //
            isPartlyCustomizedDataResource("/first-resource.setup"),       //
            isPartlyCustomizedDataResource("/second-resource.setup")       //
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void what_happens_if_using_multiple_resources__with_multiple_DataStores() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("multipleResourcesWithMultipleDataStores");

        // assert / then
        assertResolvedDataResources(dataResources,                    //
            isDataResourceWithNamedDataStore("/first-resource.setup", DefaultDataStore.class),       //
            isDataResourceWithNamedDataStore("/first-resource.setup", OtherDataStore.class),       //
            isDataResourceWithNamedDataStore("/second-resource.setup", DefaultDataStore.class),       //
            isDataResourceWithNamedDataStore("/second-resource.setup", OtherDataStore.class)       //
        );
    }

    /**
     * Holds the test fixtures.
     *
     * The methods holds various {@link DataSet} instances.
     */
    @SuppressWarnings("unused")
    private static class AnyClass {
        @DataSet
        public void defaultSettings() {
        }

        @DataSet(datastores = OtherDataStore.class, name = OTHER_DATA_SET_NAME, setup = "/any-resource.setup", transactional = false, failOnError = false)
        public void noneDefaultSettings() {
        }

        @DataSet(setup = {"/first-resource.setup", "/second-resource.setup"})
        public void multipleResources() {
        }

        @DataSet(datastores = {DefaultDataStore.class, OtherDataStore.class}, setup = {"/first-resource.setup", "/second-resource.setup"})
        public void multipleResourcesWithMultipleDataStores() {
        }
    }

}