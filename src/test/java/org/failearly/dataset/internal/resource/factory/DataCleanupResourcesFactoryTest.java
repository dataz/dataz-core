package org.failearly.dataset.internal.resource.factory;

import org.failearly.dataset.DataCleanup;
import org.failearly.dataset.resource.DataResource;
import org.junit.Test;

import java.util.List;

import static org.failearly.dataset.test.DataResourceMatchers.isDataResource;
import static org.failearly.dataset.test.DataResourceMatchers.isDefaultDataResource;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

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
        assertThat(dataResources,                                                                                               //
                contains(                                                                                                       //
                        isDefaultDataResource("/org/failearly/dataset/internal/resource/factory/AnyClass-defaultSettings.cleanup")//
                )                                                                                                               //
        );
    }

    @Test
    public void none_default_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("noneDefaultSettings");

        // assert / then
        assertThat(dataResources,                                                                                          //
                contains(                                                                                                  //
                        isDataResource(OTHER_DATASTORE_ID, OTHER_DATA_SET_NAME, "/any-resource.cleanup", false, false)       //
                )                                                                                                          //
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void multiple_resources() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("multipleResources");

        // assert / then
        assertThat(dataResources,                                             //
                contains(                                                     //
                        isDefaultDataResource("/first-resource.cleanup"),     //
                        isDefaultDataResource("/second-resource.cleanup")     //
                )                                                             //
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