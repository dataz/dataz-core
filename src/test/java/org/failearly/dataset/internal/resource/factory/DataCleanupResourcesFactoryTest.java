/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */

package org.failearly.dataset.internal.resource.factory;

import org.failearly.dataset.DataCleanup;
import org.failearly.dataset.resource.DataResource;
import org.junit.Test;

import java.util.List;

import static org.failearly.dataset.test.DataResourceMatchers.isDataResource;
import static org.failearly.dataset.test.DataResourceMatchers.isDefaultDataResource;

/**
 * DataCleanupResourcesFactoryTest contains tests for {@link DataCleanup} and {@link DataCleanupResourcesFactory}.
 */
public class DataCleanupResourcesFactoryTest extends DataResourcesFactoryTestBase<DataCleanup, DataCleanupResourcesFactory> {

    public DataCleanupResourcesFactoryTest() {
        super(DataCleanup.class, new DataCleanupResourcesFactory(), AnyClass.class);
    }

    @Test
    public void default_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("defaultSettings");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                        //
                isDefaultDataResource("/org/failearly/dataset/internal/resource/factory/AnyClass-defaultSettings.cleanup")//
        );
    }

    @Test
    public void none_default_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("noneDefaultSettings");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                   //
                isDataResource(OTHER_DATASTORE_ID, OTHER_DATA_SET_NAME, "/any-resource.cleanup", false, false)       //
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void multiple_resources() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("multipleResources");

        // assert / then
        assertResolvedDataResources(dataResources,
                isDefaultDataResource("/first-resource.cleanup"),     //
                isDefaultDataResource("/second-resource.cleanup")     //
        );
    }

    private static class AnyClass {
        @DataCleanup
        public void defaultSettings() {
        }

        @DataCleanup(datastore = OTHER_DATASTORE_ID, name = OTHER_DATA_SET_NAME, value = "/any-resource.cleanup", transactional = false, failOnError = false)
        public void noneDefaultSettings() {
        }

        @DataCleanup({"/first-resource.cleanup", "/second-resource.cleanup"})
        public void multipleResources() {
        }
    }

}