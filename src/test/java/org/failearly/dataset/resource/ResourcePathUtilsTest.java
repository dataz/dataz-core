/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.resource;

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.internal.resource.InvalidResourcePathException;
import org.failearly.dataset.internal.resource.ResourceType;
import org.failearly.dataset.test.FakeDataStoreRule;
import org.failearly.dataset.test.TestUtils;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * DataResourceUtilsTest contains tests for ... .
 */
public class ResourcePathUtilsTest {

    private static final String DATASTORE_ID = Constants.DATASET_DEFAULT_DATASTORE_ID;

    @ClassRule
    public static final TestRule fakeDataStoreRule = FakeDataStoreRule.createFakeDataStoreRule(ResourcePathUtilsTest.class)
            .addDataStore(DATASTORE_ID);

    @Test
    public void createDefaultResourcePath() throws Exception {
        assertThat("Default resource path from class and setup?",
                ResourcePathUtils.createDefaultResourceNameFromTestClass(MyTestClass.class, DATASTORE_ID, ResourceType.SETUP),
                is("MyTestClass.setup"));
        assertThat("Default resource path from class and cleanup?",
                ResourcePathUtils.createDefaultResourceNameFromTestClass(MyTestClass.class, DATASTORE_ID, ResourceType.CLEANUP),
                is("MyTestClass.cleanup"));
        assertThat("Default resource path from method and setup?",
                ResourcePathUtils.createDefaultResourceNameFromTestMethod(testMethod(), DATASTORE_ID, ResourceType.SETUP),
                is("MyTestClass-anyTestMethod.setup"));
        assertThat("Default resource path from method and cleanup?",
                ResourcePathUtils.createDefaultResourceNameFromTestMethod(testMethod(), DATASTORE_ID, ResourceType.CLEANUP),
                is("MyTestClass-anyTestMethod.cleanup"));
    }

    @Test
    public void resourcePath_simple_valid_one() throws Exception {
        assertThat("Relative resource path?", ResourcePathUtils.resourcePath("any-resource.dataset", MyTestClass.class),
                is("/org/failearly/dataset/resource/any-resource.dataset"));
        assertThat("Absolute resource path?", ResourcePathUtils.resourcePath("/any-resource.dataset", MyTestClass.class),
                is("/any-resource.dataset"));
        assertThat("Absolute resource path?", ResourcePathUtils.resourcePath("//any/dir/any-resource.dataset", MyTestClass.class),
                is("/any/dir/any-resource.dataset"));
    }

    @Test
    public void resourcePath_withDotPathElements() throws Exception {
        assertThat("Relative resource path with dot (.) path elements?", ResourcePathUtils.resourcePath("any/./dir/./any-resource.dataset", MyTestClass.class),
                is("/org/failearly/dataset/resource/any/dir/any-resource.dataset"));
        assertThat("Absolute resource path with dot (.) path elements?", ResourcePathUtils.resourcePath("/any/./dir/./any-resource.dataset", MyTestClass.class),
                is("/any/dir/any-resource.dataset"));
        assertThat("Relative resource path with dot dot (..) path elements?", ResourcePathUtils.resourcePath("../dir/../any-resource.dataset", MyTestClass.class),
                is("/org/failearly/dataset/any-resource.dataset"));
        assertThat("Absolute resource path with dot dot (..) path elements?", ResourcePathUtils.resourcePath("/any/../dir/../any-resource.dataset", MyTestClass.class),
                is("/any-resource.dataset"));
    }

    @Test
    public void resourcePath_invalidResource() throws Exception {
        assertInvalidResourcePathException("");
        assertInvalidResourcePathException("/");
        assertInvalidResourcePathException("/only-path/");
        assertInvalidResourcePathException("/../any-resources.dataset");
    }

    private static void assertInvalidResourcePathException(String resourceName) {
        TestUtils.assertException(
                InvalidResourcePathException.class,
                "Invalid resource path '" + resourceName + "'", () -> ResourcePathUtils.resourcePath(resourceName, MyTestClass.class)
        );
    }

    private static Method testMethod() throws NoSuchMethodException {
        return MyTestClass.class.getMethod("anyTestMethod");
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class MyTestClass {
        public void anyTestMethod() {
        }
    }
}