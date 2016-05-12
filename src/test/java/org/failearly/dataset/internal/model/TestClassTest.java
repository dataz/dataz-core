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

package org.failearly.dataset.internal.model;

import com.company.project.module.MiddleDataSetBaseClass;
import org.failearly.dataset.DataSet;
import org.failearly.dataset.config.Constants;
import org.failearly.dataset.datastore.DataStores;
import org.junit.*;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * TestMethodDataSetsTestResources contains tests for TestMethods.
 */
public class TestClassTest {

    private TestClass testClass;

    @BeforeClass
    public static void setupDatastore() {
        DataStores.loadDataStore(AnyTestClass.class);
    }

    @AfterClass
    public static void resetDatastore() {
        DataStores.reset();
    }

    @Before
    public void setup() {
        this.testClass = TestClass.create(AnyTestClass.class);
    }

    @Test
    public void resolveSetupResourceNames() throws Exception {
        // act / when
        final List<String> resourceNames = resolveSetupResourceNames("testWithDataSet");

        // assert / then
        assertThat("Setup Resource Names?", resourceNames, contains(
                        "/com/company/project/DataSetBaseClass.dataset",
                        "/com/company/project/module/MiddleDataSetBaseClass.setup",
                        "/org/failearly/dataset/internal/model/AnyTestClass.dataset",
                        "/org/failearly/dataset/internal/model/testWithDataSet1.dataset",
                        "/testWithDataSet2.dataset",
                        "/org/failearly/dataset/internal/model/AnyTestClass-testWithDataSet.setup"
                )
        );
    }


    @Test
    public void resolveCleanupResourceNames() throws Exception {
        // act / when
        final List<String> resourceNames = resolveCleanupResourceNames("testWithDataSet");

        // assert / then
        assertThat("Cleanup Resource Names?", resourceNames, contains(
                        "/org/failearly/dataset/internal/model/testWithDataSet2.cleanup",
                        "/org/failearly/dataset/internal/model/AnyTestClass-testWithDataSet.cleanup",
                        "/org/failearly/dataset/internal/model/AnyTestClass.cleanup",
                        "/middle2.cleanup",
                        "/com/company/project/module/middle1.cleanup",
                        "/com/company/project/DataSetBaseClass.cleanup"
                )
        );
    }

    private List<String> resolveCleanupResourceNames(String methodName) {
        final List<String> resourceNames = new LinkedList<>();
        this.testClass.getTestMethod(methodName).handleCleanupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, (m, d, dataResource) -> resourceNames.add(dataResource.getResource()));
        return resourceNames;
    }

    private List<String> resolveSetupResourceNames(String methodName) {
        final List<String> resourceNames = new LinkedList<>();
        this.testClass.getTestMethod(methodName).handleSetupResource(Constants.DATASET_DEFAULT_DATASTORE_ID, (m, d, dataResource) -> resourceNames.add(dataResource.getResource()));
        return resourceNames;
    }

    /**
     * Just for testing purpose of the actually tests. Will be executed during test phase.
     */
    @SuppressWarnings("UnusedDeclaration")
    @DataSet(name = "AnyTestClass", setup = "AnyTestClass.dataset")
    @Ignore("Test Fixtures!")
    private static class AnyTestClass extends MiddleDataSetBaseClass {

        @SuppressWarnings("UnusedDeclaration")
        public void noTestMethod() {
        }

        @Test
        @Ignore
        public void ignoredMethod() {
        }

        @Test
        public void testWithoutDataSet() {
        }

        @Test
        @DataSet(name = "testWithDataSet", setup = {"testWithDataSet1.dataset", "/testWithDataSet2.dataset"})
        @DataSet(name = "testWithDataSet2", cleanup = "testWithDataSet2.cleanup")
        public void testWithDataSet() {
        }
    }
}
