/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.internal.resource.factory.use;

import org.failearly.dataz.DataSet;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.Use;
import org.failearly.dataz.internal.resource.factory.DataResourcesFactoryTestBase;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.test.DataResourceMatcherBuilder;
import org.failearly.dataz.test.SimpleTemplateObject;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.List;

import static org.failearly.dataz.Use.ReusableDataSet;

/**
 * UseCleanupResourcesFactoryTest contains tests for {@link UseCleanupResourcesFactory} and it's associated impl {@link Use}.
 */
public final class UseCleanupResourcesFactoryTest extends DataResourcesFactoryTestBase<Use, UseCleanupResourcesFactory> {


    public UseCleanupResourcesFactoryTest() {
        super(Use.class, new UseCleanupResourcesFactory(), AnyClass.class);
    }

    @Test
    public void what_happens_if_reusing_reusable_dataset_with_default_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("reuseDefaultDataSet");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                      //
            useDefaultDataSet(UseDefaultDataSet.class)  //
        );
    }

    private Matcher<DataResource> useDefaultDataSet(Class<? extends ReusableDataSet> clazz) {
        return DataResourceMatcherBuilder.createWithDefaults()
            .withDefaultResource(clazz, "cleanup")
            .build();
    }

    private Matcher<DataResource> useCustomDataSet() {
        return DataResourceMatcherBuilder.createWithDefaults()
            .withNamedDataStore(OtherDataStore.class)
            .withDataSetName("use-explicit-resource")
            .withResource("/any-existing-resource.cleanup")
            .withTransactional(false)
            .withFailOnError(false)
            .build();
    }

    @Test
    public void what_happens_if_reusing_reusable_dataset_with_custom_settings() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("reuseDataSetWithCustomResource");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                      //
            useCustomDataSet() //
        );
    }

    @Test
    public void what_happens_if_reusing_multiple_reusable_datasets() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("reuseMultipleReusableDataSet");

        // assert / then
        assertResolvedDataResources(dataResources,  //
            useCustomDataSet(), //
            useDefaultDataSet(UseDefaultDataSet.class)     //
        );
    }

    @Test
    public void what_about_the_order_of_multiple_reusable_datasets() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("reuseMultipleReusableDataSet_but_other_order");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                   //
            useDefaultDataSet(UseDefaultDataSet.class),     //
            useCustomDataSet() //
        );
    }


    @Test
    public void what_about_using_Use_on_ReusableDataSet() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("reuseUse");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                     //
            useCustomDataSet(),   //
            useDefaultDataSet(UseDefaultDataSet.class)  //
        );
    }

    @Test
    public void what_if_a_ReusableDataSet_extends_another_ReusableDataSet() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("reusableDatasetClassHierarchy");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                     //
            useDefaultDataSet(ExtendingReusableBaseClass.class),  //
            useDefaultDataSet(ReusableBaseClass.class) //
        );
    }

    @Test
    public void what_if_using_template_resources() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("reuseWithTemplateObjects");

        // assert / then
        assertDataResourceContent(dataResources.get(0),
            "Uses local template objects!\n"  //
                + "Uses global template objects!\n" //
                + "$to-be-ignored.description\n"
        );
    }

    @Test
    public void what_if_the_datastores_option_will_be_used_only_on_top_level() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("overwriteDataStoreSettings");

        // assert / then
        // should overwrite the origin datastore settings.
        assertResolvedDataResources(dataResources,                                                         //
            useDefaultDataSetOverwrittenDataStores(UseDefaultDataSet.class, OtherDataStore.class),       //
            useDefaultDataSetOverwrittenDataStores(UseDefaultDataSet.class, DefaultDataStore.class)       //
        );
    }

    @Test
    public void what_if_the_datastores_option_will_be_used_twice() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("multipleOverwriteDataStoreSettings");

        // assert / then
        // should only the top level settings be used, other settings, won't be used.
        assertResolvedDataResources(dataResources,                                                         //
            useDefaultDataSetOverwrittenDataStores(UseDefaultDataSet.class, DefaultDataStore.class),       //
            useDefaultDataSetOverwrittenDataStores(UseDefaultDataSet.class, OtherDataStore.class)       //
        );
    }



    private Matcher<DataResource> useDefaultDataSetOverwrittenDataStores(Class<? extends ReusableDataSet> clazz, Class<? extends NamedDataStore> namedDataStores) {
        return DataResourceMatcherBuilder.createWithDefaults()
            .withDefaultResource(clazz, "cleanup")
            .withNamedDataStore(namedDataStores)
            .build();
    }


    /**
     * Holds the test fixtures.
     *
     * The methods holds various {@link Use} instances.
     */
    @SuppressWarnings("unused")
    private static class AnyClass {
        @Use(UseDefaultDataSet.class)
        public void reuseDefaultDataSet() {
        }

        @Use(UseCustomDataSet.class)
        public void reuseDataSetWithCustomResource() {
        }

        @Use({UseDefaultDataSet.class, UseCustomDataSet.class})
        public void reuseMultipleReusableDataSet() {
        }

        @Use({UseCustomDataSet.class, UseDefaultDataSet.class})
        public void reuseMultipleReusableDataSet_but_other_order() {
        }

        @Use(UsingUse.class)
        public void reuseUse() {
        }

        @Use(ExtendingReusableBaseClass.class)
        public void reusableDatasetClassHierarchy() {
        }

        @SimpleTemplateObject(scope = Scope.GLOBAL, name = "global", description = "Uses global template objects!")
        @SimpleTemplateObject(scope = Scope.LOCAL, name = "to-be-ignored", description = "<to be ignored>")
        @Use(ReusableTemplateDataSet.class)
        public void reuseWithTemplateObjects() {
        }

        @Use(datastores = {OtherDataStore.class, DefaultDataStore.class}, value = UseDefaultDataSet.class)
        public void overwriteDataStoreSettings() {
        }

        @Use(datastores = {DefaultDataStore.class, OtherDataStore.class}, value=UseDatastoreOption.class)
        public void multipleOverwriteDataStoreSettings() {}
    }


    @DataSet
    private interface UseDefaultDataSet extends ReusableDataSet {
    }

    @DataSet(datastores = OtherDataStore.class, name = "use-explicit-resource", cleanup = "/any-existing-resource.cleanup", failOnError = false, transactional = false)
    private interface UseCustomDataSet extends ReusableDataSet {
    }

    @Use({UseDefaultDataSet.class, UseCustomDataSet.class})
    private interface UsingUse extends ReusableDataSet {
    }


    @DataSet
    private static class ReusableBaseClass implements ReusableDataSet {
    }

    @DataSet
    private static class ExtendingReusableBaseClass extends ReusableBaseClass {
    }

    @DataSet(cleanup = "template.vm")
    @SimpleTemplateObject(name = "to", description = "Uses local template objects!")
    private interface ReusableTemplateDataSet extends ReusableDataSet {
    }

    @Use(datastores = {ShouldNeverBeenSeen.class}, value = UseDefaultDataSet.class)
    private interface UseDatastoreOption extends ReusableDataSet {
    }

    private static class ShouldNeverBeenSeen extends NamedDataStore {}
}