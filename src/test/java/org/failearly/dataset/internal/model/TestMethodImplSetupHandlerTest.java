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

package org.failearly.dataset.internal.model;

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.internal.resource.DataResourceHandler;
import org.junit.Test;
import org.mockito.Mockito;

import static org.failearly.dataset.test.DataResourceMatchers.dataResourceMatcher;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * TestMethodTest contains tests for {@link TestMethod#handleSetupResource(String, DataResourceHandler)}.
 */
@SuppressWarnings("unchecked")
public class TestMethodImplSetupHandlerTest extends TestMethodImplTestBase {


    @Test
    public void only_current_test_method__should_be_applied() throws Exception {
        final String testMethodName = "withDataSet";
        final TestMethod testMethod = createTestMethod(testMethodName, TestClassHierarchy.class);

        // act / when
        testMethod.handleSetupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, defaultResourceHandler);

        // assert / then
        assertThat("Applied Test methods on handling setup resources?", appliedTestMethods, contains(testMethodName));
    }

    @Test
    public void method_with_annotation_DataSet__on_test_class_hierarchy() throws Exception {
        // arrange / given
        final TestMethod testMethod = createTestMethod("withDataSet", TestClassHierarchy.class);

        // act / when
        testMethod.handleSetupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, defaultResourceHandler);

        // assert / then
        assertDataResources(                                                                                     //
                dataResourceMatcher("DS5", "/org/failearly/dataset/internal/model/BaseTestClass.setup")            //
                , dataResourceMatcher("DS3", "/org/failearly/dataset/internal/model/DS31.setup")                   //
                , dataResourceMatcher("DS3", "/DS32.setup")                                                        //
                , dataResourceMatcher("DS4", "/org/failearly/dataset/internal/model/TestClassHierarchy.setup")              //
                , dataResourceMatcher("DS1", "/org/failearly/dataset/internal/model/DS11.setup")                   //
                , dataResourceMatcher("DS1", "/DS12.setup")                                                        //
                , dataResourceMatcher("DS2", "/org/failearly/dataset/internal/model/TestClassHierarchy-withDataSet.setup")  //
        );
    }

    @Test
    public void method_without_annotation_DataSet__on_test_class_hierarchy() throws Exception {
        // arrange / given
        final TestMethod testMethod = createTestMethod("withoutDataSet", TestClassHierarchy.class);

        // act / when
        testMethod.handleSetupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, defaultResourceHandler);

        // assert / then
        assertDataResources( //
                dataResourceMatcher("DS5", "/org/failearly/dataset/internal/model/BaseTestClass.setup")  //
                , dataResourceMatcher("DS3", "/org/failearly/dataset/internal/model/DS31.setup") //
                , dataResourceMatcher("DS3", "/DS32.setup") //
                , dataResourceMatcher("DS4", "/org/failearly/dataset/internal/model/TestClassHierarchy.setup") //
        );
    }

    @Test
    public void method_with_annotation_DataSet__on_different_datastore() throws Exception {
        // arrange / given
        final TestMethod testMethod = createTestMethod("withDataSet", TestClassHierarchy.class);

        // act / when
        testMethod.handleSetupResource(OTHER_DATASTORE, defaultResourceHandler);

        // assert / then
        assertDataResources( //
                dataResourceMatcher("OTHER-DATASTORE", "DS6", "/org/failearly/dataset/internal/model/TestClassHierarchy.setup") //
        );
    }

    @Test
    public void normal_junit_test_class() throws Exception {
        // arrange / given
        final TestMethod testMethod = createTestMethod("anyTestMethod", NormalJunitTestClass.class);

        // act / when
        final DataResourceHandler resourceHandler = Mockito.mock(DataResourceHandler.class);
        testMethod.handleSetupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, resourceHandler);

        // assert / then
        Mockito.verifyZeroInteractions(resourceHandler);
    }

    @Test
    public void simpleDataSet_setup_without_annotation_DataSet() throws Exception {
        // arrange / given
        final TestMethod testMethod = createTestMethod("withoutDataSet", SimpleDataSetTestClass.class);

        // act / when
        testMethod.handleSetupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, defaultResourceHandler);

        // assert / then
        assertDataResources( //
                dataResourceMatcher("DS1", "/org/failearly/dataset/internal/model/DS11.setup") //
                , dataResourceMatcher("DS1", "/org/failearly/dataset/internal/model/DS12.setup") //
                , dataResourceMatcher("DS3", "/org/failearly/dataset/internal/model/SimpleDataSetTestClass.setup") //
        );
    }

    @Test
    public void simpleDataSet_setup_with_annotation_DataSet() throws Exception {
        // arrange / given
        final TestMethod testMethod = createTestMethod("withDataSet", SimpleDataSetTestClass.class);

        // act / when
        testMethod.handleSetupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, defaultResourceHandler);

        // assert / then
        assertDataResources( //
                dataResourceMatcher("DS1", "/org/failearly/dataset/internal/model/DS11.setup") //
                , dataResourceMatcher("DS1", "/org/failearly/dataset/internal/model/DS12.setup") //
                , dataResourceMatcher("DS3", "/org/failearly/dataset/internal/model/SimpleDataSetTestClass.setup") //
                , dataResourceMatcher("DS2", "/org/failearly/dataset/internal/model/DS2.setup") //
                , dataResourceMatcher("DS4", "/org/failearly/dataset/internal/model/SimpleDataSetTestClass-withDataSet.setup") //
        );
    }

    @Test
    public void simpleDataSet_setup_with_mixed_dataset_annotations() throws Exception {
        // arrange / given
        final TestMethod testMethod = createTestMethod("withDataSetupAndDataSet", SimpleDataSetTestClass.class);

        // act / when
        testMethod.handleSetupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, defaultResourceHandler);

        // assert / then
        assertDataResources( //
                dataResourceMatcher("DS1", "/org/failearly/dataset/internal/model/DS11.setup") //
                , dataResourceMatcher("DS1", "/org/failearly/dataset/internal/model/DS12.setup") //
                , dataResourceMatcher("DS3", "/org/failearly/dataset/internal/model/SimpleDataSetTestClass.setup") //
                , dataResourceMatcher("DSP2", "/org/failearly/dataset/internal/model/SimpleDataSetTestClass-withDataSetupAndDataSet.setup")// DataSetup(name=DSP2)
                , dataResourceMatcher("DS5", "/org/failearly/dataset/internal/model/SimpleDataSetTestClass-withDataSetupAndDataSet.setup") // DataSet(name=DS5)
        );
    }


}