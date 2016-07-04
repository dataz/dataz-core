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

import org.failearly.dataz.DataSet;
import org.failearly.dataz.internal.resource.factory.DataResourcesFactoryTestBase;
import org.failearly.dataz.resource.DataResource;
import org.junit.Test;

import java.util.List;

import static org.failearly.dataz.test.DataResourceMatchers.isDataResource;
import static org.failearly.dataz.test.DataResourceMatchers.isDefaultDataResource;

/**
 * SetupResourcesFactoryTest contains tests for {@link DataSet#setup()} and {@link DataSetSetupResourcesFactory}.
 */
public class DataSetSetupResourcesFactoryTest extends DataResourcesFactoryTestBase<DataSet, DataSetSetupResourcesFactory> {

    public DataSetSetupResourcesFactoryTest() {
        super(DataSet.class, new DataSetSetupResourcesFactory(), AnyClass.class);
    }

    @Test
    public void default_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("defaultSettings");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                              //
                isDefaultDataResource("/org/failearly/dataz/internal/resource/factory/dataset/AnyClass-defaultSettings.setup")//
        );
    }

    @Test
    public void none_default_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("noneDefaultSettings");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                         //
                isDataResource(OTHER_DATASTORE_ID, OTHER_DATA_SET_NAME, "/any-resource.setup", false, false)       //
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void multiple_resources() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("multipleResources");

        // assert / then
        assertResolvedDataResources(dataResources,                    //
                isDefaultDataResource("/first-resource.setup"),       //
                isDefaultDataResource("/second-resource.setup")       //
        );
    }

    private static class AnyClass {
        @DataSet
        public void defaultSettings() {
        }

        @DataSet(name = OTHER_DATA_SET_NAME, setup = "/any-resource.setup", transactional = false, failOnError = false)
        public void noneDefaultSettings() {
        }

        @DataSet(setup = {"/first-resource.setup", "/second-resource.setup"})
        public void multipleResources() {
        }
    }

}