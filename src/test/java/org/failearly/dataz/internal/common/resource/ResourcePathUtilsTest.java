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

package org.failearly.dataz.internal.common.resource;

import org.failearly.dataz.common.test.ExceptionVerifier;
import org.failearly.dataz.common.test.annotations.Subject;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * DataResourceUtilsTest contains tests for ... .
 */
@Subject(ResourcePathUtils.class)
public class ResourcePathUtilsTest {
    private static final String PARENT_DIR = "/org/failearly/dataz/internal/common";
    private static final String CURRENT_DIR = PARENT_DIR + "/resource";

    @Test
    public void resourcePath_simple_valid_one() throws Exception {
        assertThat("Relative resource path?", ResourcePathUtils.resourcePath("any-resource.dataz", MyTestClass.class),
                is(CURRENT_DIR + "/any-resource.dataz"));
        assertThat("Absolute resource path?", ResourcePathUtils.resourcePath("/any-resource.dataz", MyTestClass.class),
                is("/any-resource.dataz"));
        assertThat("Absolute resource path?", ResourcePathUtils.resourcePath("//any/dir/any-resource.dataz", MyTestClass.class),
                is("/any/dir/any-resource.dataz"));
    }

    @Test
    public void resourcePath_withDotPathElements() throws Exception {
        assertThat("Relative resource path with dot (.) path elements?", ResourcePathUtils.resourcePath("any/./dir/./any-resource.dataz", MyTestClass.class),
                is(CURRENT_DIR + "/any/dir/any-resource.dataz"));
        assertThat("Absolute resource path with dot (.) path elements?", ResourcePathUtils.resourcePath("/any/./dir/./any-resource.dataz", MyTestClass.class),
                is("/any/dir/any-resource.dataz"));
        assertThat("Relative resource path with dot dot (..) path elements?", ResourcePathUtils.resourcePath("../dir/../any-resource.dataz", MyTestClass.class),
                is(PARENT_DIR + "/any-resource.dataz"));
        assertThat("Absolute resource path with dot dot (..) path elements?", ResourcePathUtils.resourcePath("/any/../dir/../any-resource.dataz", MyTestClass.class),
                is("/any-resource.dataz"));
    }

    @Test
    public void resourcePath_invalidResource() throws Exception {
        assertInvalidResourcePathException("");
        assertInvalidResourcePathException("   ");
        assertInvalidResourcePathException("  any-resource.dataz  ");
        assertInvalidResourcePathException("any resource.dataz");
        assertInvalidResourcePathException("any\tresource.dataz");
        assertInvalidResourcePathException("any\nresource.dataz");
        assertInvalidResourcePathException("any\rresource.dataz");
        assertInvalidResourcePathException("/");
        assertInvalidResourcePathException("/only-path/");
        assertInvalidResourcePathException("/../any-resources.dataz");
    }

    private static void assertInvalidResourcePathException(String resourceName) {
        ExceptionVerifier.TestAction action=() -> ResourcePathUtils.resourcePath(resourceName, MyTestClass.class);
        ExceptionVerifier.on(action).expect(InvalidResourcePathException.class).expect("Invalid resource path '" + resourceName + "'").verify();
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class MyTestClass {
    }
}