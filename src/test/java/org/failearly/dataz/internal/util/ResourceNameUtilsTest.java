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

package org.failearly.dataz.internal.util;

import org.failearly.dataz.config.DataSetProperties;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResourceNameUtilsTest {
    public ResourceNameUtilsTest() {
    }

    @Test
    public void getResourceNameWithoutPathAndSuffix() throws Exception {
        assertThat("With double suffix?", ResourceNameUtils.getResourceNameWithoutPathAndSuffix("/this/is/a/path/to/resource-file.txt.vm"), is("resource-file"));
        assertThat("With single suffix?", ResourceNameUtils.getResourceNameWithoutPathAndSuffix("/this/is/a/path/to/resource-file.txt"), is("resource-file"));
        assertThat("Without suffix?", ResourceNameUtils.getResourceNameWithoutPathAndSuffix("/this/is/a/path/to/resource-file"), is("resource-file"));
        assertThat("Without path and suffix?", ResourceNameUtils.getResourceNameWithoutPathAndSuffix("resource-file"), is("resource-file"));
        assertThat("Empty?", ResourceNameUtils.getResourceNameWithoutPathAndSuffix(""), is(""));
        assertThat("Null?", ResourceNameUtils.getResourceNameWithoutPathAndSuffix(null), is(""));
    }

    @Test
    public void getResourceSuffix() throws Exception {
        assertThat("With double suffix?", ResourceNameUtils.getResourceSuffix("/this/is/a/path/to/resource-file.setup.xml"), is(".setup.xml"));
        assertThat("With  suffix + template suffix?",
                ResourceNameUtils.getResourceSuffix("/this/is/a/path/to/resource-file.setup" + DataSetProperties.getTemplateSuffix()),
                is(".setup"));
        assertThat("With single suffix?", ResourceNameUtils.getResourceSuffix("/resource-file.txt"), is(".txt"));
        assertThat("Without suffix?", ResourceNameUtils.getResourceSuffix("/this/is/a/path/to/resource-file"), is(""));
        assertThat("Without path?", ResourceNameUtils.getResourceSuffix("resource-file.txt"), is(".txt"));
        assertThat("Empty?", ResourceNameUtils.getResourceSuffix(""), is(""));
        assertThat("Null?", ResourceNameUtils.getResourceSuffix(null), is(""));
    }

    @Test
    public void getResourcePath() throws Exception {
        assertThat("Absolute path?", ResourceNameUtils.getResourcePath("/this/is/a/path/to/resource-file.setup.xml"), is("/this/is/a/path/to"));
        assertThat("Absolute path?", ResourceNameUtils.getResourcePath("/resource-file.setup.xml"), is("/"));
        assertThat("Relative path?", ResourceNameUtils.getResourcePath("rel/path/to/resource-file.setup.xml"), is("rel/path/to"));
        assertThat("No path?", ResourceNameUtils.getResourcePath("resource-file.setup.xml"), is(""));
        assertThat("Empty?", ResourceNameUtils.getResourcePath(""), is(""));
        assertThat("Null?", ResourceNameUtils.getResourcePath(null), is(""));
    }
}