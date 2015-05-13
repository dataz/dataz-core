/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 marko (http://fail-early.com/contact)
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
import org.failearly.dataset.test.DataResourceMatchers;
import org.failearly.dataset.test.TestUtils;
import org.junit.Test;
import org.mockito.Mockito;

import static org.failearly.dataset.test.DataResourceMatchers.isDataResource;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * TestMethodTest contains tests for {@link TestMethod#handleCleanupResource(String, DataResourceHandler)} .
 */
@SuppressWarnings("unchecked")
public class TestMethodImplCleanupHandlerTest extends TestMethodImplTestBase {


    @Test
    public void only_current_test_method_should_be_applied() throws Exception {
        final String testMethodName = "withDataSet";
        final TestMethod testMethod = createTestMethod(testMethodName, TestClassHierarchy.class);

        // act / when
        testMethod.handleCleanupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, defaultResourceHandler);

        // assert / then
        assertThat("Applied Test methods on handling cleanup resources?", appliedTestMethods, contains(testMethodName));
    }

    @Test
    public void method_with_annotation_DataSet__on_different_datastore() throws Exception {
        // arrange / given
        final TestMethod testMethod = createTestMethod("withDataSet", TestClassHierarchy.class);

        // act / when
        testMethod.handleCleanupResource(OTHER_DATASTORE_ID, defaultResourceHandler);

        // assert / then
        assertDataResources(
                DataResourceMatchers.isDataResource("OTHER-DATASTORE", "DS6", "/org/failearly/dataset/internal/model/TestClassHierarchy.cleanup")
        );
    }

    @Test
    public void method_with_annotation_DataSet() throws Exception {
        // arrange / given
        final TestMethod testMethod = createTestMethod("withDataSet", TestClassHierarchy.class);

        // act / when
        testMethod.handleCleanupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, defaultResourceHandler);

        // assert / then
        assertDataResources(  //
                isDataResource("DS2", "/org/failearly/dataset/internal/model/TestClassHierarchy-withDataSet.cleanup"), //
                isDataResource("DS1", "/DS12.cleanup"), //
                isDataResource("DS1", "/org/failearly/dataset/internal/model/DS11.cleanup"), //
                isDataResource("DS4", "/org/failearly/dataset/internal/model/TestClassHierarchy.cleanup"), //
                isDataResource("DS3", "/DS32.cleanup"), //
                isDataResource("DS3", "/org/failearly/dataset/internal/model/DS31.cleanup"), //
                isDataResource("DS5", "/org/failearly/dataset/internal/model/BaseTestClass.cleanup") //
        );
    }

    @Test
    public void method_without_annotation_DataSet() throws Exception {
        // arrange / given
        final TestMethod testMethod = createTestMethod("withoutDataSet", TestClassHierarchy.class);

        // act / when
        testMethod.handleCleanupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, defaultResourceHandler);

        // assert / then
        assertDataResources(  //
                isDataResource("DS4", "/org/failearly/dataset/internal/model/TestClassHierarchy.cleanup") //
                , isDataResource("DS3", "/DS32.cleanup") //
                , isDataResource("DS3", "/org/failearly/dataset/internal/model/DS31.cleanup") //
                , isDataResource("DS5", "/org/failearly/dataset/internal/model/BaseTestClass.cleanup") //

        );
    }

    @Test
    public void normal_junit_test_class() throws Exception {
        // arrange / given
        final String testMethodName = "anyTestMethod";
        final TestMethod testMethod = createTestMethod(testMethodName, NormalJunitTestClass.class);
        final DataResourceHandler resourceHandler = Mockito.mock(DataResourceHandler.class);

        // act / when
        testMethod.handleCleanupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, resourceHandler);

        // assert / then
        Mockito.verifyZeroInteractions(resourceHandler);
    }

    @Test
    public void suppressed_cleanup_annotation__should_never_call_handleCleanupResource() throws Exception {
        // arrange / given
        final TestMethod suppressCleanup = createTestMethod("suppressed", TestClassHierarchy.class);

        // assert / then
        assertThat("Suppressed?", suppressCleanup.isSuppressCleanup(), is(true));

        // check that handleCleanupResource must not be called, if @SuppressCleanup is active.
        TestUtils.assertException(AssertionError.class,                                             //
                "handleCleanupResource() must not be called if @SuppressCleanup is active.",        //
                ()->suppressCleanup.handleCleanupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, defaultResourceHandler)
            );
    }
}