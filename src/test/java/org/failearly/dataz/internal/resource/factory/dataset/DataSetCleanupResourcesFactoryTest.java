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
 * SetupResourcesFactoryTest contains tests for {@link DataSet#cleanup()} and {@link DataSetSetupResourcesFactory}.
 */
@Subject({DataSet.class, DataSetCleanupResourcesFactory.class})
public class DataSetCleanupResourcesFactoryTest extends DataResourcesFactoryTestBase<DataSet, DataSetCleanupResourcesFactory> {

    public DataSetCleanupResourcesFactoryTest() {
        super(DataSet.class, new DataSetCleanupResourcesFactory(), AnyClass.class);
    }

    @Test
    public void what_happens_with_default_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("defaultSettings");

        // assert / then
        assertResolvedDataResources(dataResources,
            isDefaultDataResource("AnyClass-defaultSettings.cleanup")//
        );
    }

    @Test
    public void what_happens_if_using_none_default_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("noneDefaultSettings");

        // assert / then
        assertResolvedDataResources(dataResources, //
            isFullCustomizedDataResource()      //
        );
    }

    private Matcher<DataResource> isFullCustomizedDataResource() {
        return DataResourceMatcherBuilder.createWithDefaults()
            .withNamedDataStore(OtherDataStore.class)
            .withDataSetName(OTHER_DATA_SET_NAME)
            .withResource("/any-resource.cleanup")
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
        assertResolvedDataResources(dataResources,
            isPartlyCustomizedDataResource("/first-resource.cleanup"),     //
            isPartlyCustomizedDataResource("/second-resource.cleanup")     //
        );
    }


    @Test
    @SuppressWarnings("unchecked")
    public void what_happens_if_using_multiple_resources__with_multiple_DataStores() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("multipleResourcesWithMultipleDataStores");

        // assert / then
        assertResolvedDataResources(dataResources,                    //
            isDataResourceWithNamedDataStore("/first-resource.cleanup", DefaultDataStore.class),       //
            isDataResourceWithNamedDataStore("/first-resource.cleanup", OtherDataStore.class),       //
            isDataResourceWithNamedDataStore("/second-resource.cleanup", DefaultDataStore.class),       //
            isDataResourceWithNamedDataStore("/second-resource.cleanup", OtherDataStore.class)       //
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

        @DataSet(datastores = OtherDataStore.class, name = OTHER_DATA_SET_NAME, cleanup = "/any-resource.cleanup", transactional = false, failOnError = false)
        public void noneDefaultSettings() {
        }

        @DataSet(cleanup = {"/first-resource.cleanup", "/second-resource.cleanup"})
        public void multipleResources() {
        }


        @DataSet(datastores = {DefaultDataStore.class, OtherDataStore.class}, cleanup = {"/first-resource.cleanup", "/second-resource.cleanup"})
        public void multipleResourcesWithMultipleDataStores() {
        }
    }

}