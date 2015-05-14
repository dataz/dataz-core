package org.failearly.dataset.internal.resource.factory.dataset;

import org.failearly.dataset.DataSet;
import org.failearly.dataset.internal.resource.factory.DataResourcesFactoryTestBase;
import org.failearly.dataset.resource.DataResource;
import org.junit.Test;

import java.util.List;

import static org.failearly.dataset.test.DataResourceMatchers.isDataResource;
import static org.failearly.dataset.test.DataResourceMatchers.isDefaultDataResource;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * SetupResourcesFactoryTest contains tests for {@link DataSet#setup()} and {@link SetupResourcesFactory}.
 */
public class SetupResourcesFactoryTest extends DataResourcesFactoryTestBase<DataSet, SetupResourcesFactory> {

    public SetupResourcesFactoryTest() {
        super(DataSet.class, new SetupResourcesFactory(), AnyClass.class);
    }

    @Test
    public void default_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("defaultSettings");

        // assert / then
        assertThat(dataResources,                                                                                               //
                contains(                                                                                                       //
                        isDefaultDataResource("/org/failearly/dataset/internal/resource/factory/dataset/AnyClass-defaultSettings.setup")//
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
                        isDataResource(OTHER_DATASTORE_ID, OTHER_DATA_SET_NAME, "/any-resource.setup", false, false)       //
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
                        isDefaultDataResource("/first-resource.setup"),       //
                        isDefaultDataResource("/second-resource.setup")       //
                )                                                             //
        );
    }

    private static class AnyClass {
        @DataSet
        public void defaultSettings() {
        }

        @DataSet(datastore = OTHER_DATASTORE_ID, name = OTHER_DATA_SET_NAME, setup = "/any-resource.setup", transactional = false, failOnError = false)
        public void noneDefaultSettings() {
        }

        @DataSet(setup = {"/first-resource.setup", "/second-resource.setup"})
        public void multipleResources() {
        }
    }

}