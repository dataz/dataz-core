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

import org.failearly.dataset.DataSet;
import org.failearly.dataset.DataSetup;
import org.failearly.dataset.SuppressCleanup;
import org.failearly.dataset.SuppressDataSet;
import org.failearly.dataset.config.Constants;
import org.failearly.dataset.internal.resource.DataResourceHandler;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.test.FakeDataStoreRule;
import org.hamcrest.Matcher;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.failearly.common.test.utils.ReflectionUtils.resolveMethodFromClass;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * TestMethodImplBaseTest provides helper methods for testing {@link TestMethodImpl}.
 */
public abstract class TestMethodImplTestBase {
    protected static final String OTHER_DATASTORE_ID = "OTHER-DATASTORE";
    protected final Set<String> appliedTestMethods = new HashSet<>();
    protected final DataResourceHandler defaultResourceHandler = createDefaultResourceHandler();

    private final List<DataResource> dataResources = new LinkedList<>();

    @ClassRule
    public static final TestRule fakeDataStoreRule = FakeDataStoreRule.createFakeDataStoreRule(TestMethodImplSetupHandlerTest.class)
            .addDataStore(Constants.DATASET_DEFAULT_DATASTORE_ID)
            .addDataStore(OTHER_DATASTORE_ID);

    protected static TestMethod createTestMethod(String methodName, Class<?> testClass) throws NoSuchMethodException {
        return TestMethodImpl.createTestMethod(resolveMethodFromClass(methodName, testClass), testClass);
    }

    private DataResourceHandler createDefaultResourceHandler() {
        return (methodName, dataSet, dataResource) -> {
            appliedTestMethods.add(methodName);
            dataResources.add(dataResource);
        };
    }


    @SafeVarargs
    protected final void assertDataResources(Matcher<DataResource>... resourceMatchers) {
        assertThat(dataResources, contains(resourceMatchers));
    }


    //
// Test classes
//
    @SuppressWarnings("UnusedDeclaration")
    @DataSet(name = "DS1", setup = {"DS11.setup", "DS12.setup"}, cleanup = {"DS11.cleanup", "D12.cleanup"})
    @DataSet(name = "DS3")
    protected static class SimpleDataSetTestClass {
        @Test
        public void withoutDataSet() {
        }

        @Test
        @DataSet(name = "DS2", setup = "DS2.setup", cleanup = "DS2.cleanup")
        @DataSet(name = "DS4")
        public void withDataSet() {
        }

        @DataSetup(name = "DSP2")
        @DataSet(name = "DS5")
        @Test
        public void withDataSetupAndDataSet() {
        }
    }

    @DataSet(name = "DS5")
    private static class BaseTestClass {
    }

    @SuppressWarnings("UnusedDeclaration")
    @DataSet(name = "DS3", setup = {"DS31.setup", "/DS32.setup"}, cleanup = {"DS31.cleanup", "/DS32.cleanup"})
    @DataSet(name = "DS4")
    @DataSet(datastore = OTHER_DATASTORE_ID, name = "DS6")
    protected static class TestClassHierarchy extends BaseTestClass {
        @DataSet(name = "DS1",
                setup = {"DS11.setup", "/DS12.setup"},
                cleanup = {"DS11.cleanup", "/DS12.cleanup"}
        )
        @DataSet(name = "DS2")
        @Test
        public void withDataSet() {
        }

        @Test
        public void withoutDataSet() {
        }

        @Test
        @SuppressDataSet
        public void noDataSet() {
        }

        @Test
        @SuppressCleanup
        public void suppressed() {
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    protected static class NormalJunitTestClass {
        @Test
        public void anyTestMethod() {
        }
    }
}
