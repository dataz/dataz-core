/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.internal.util;

import org.junit.Test;

import java.io.InputStream;

import static org.failearly.dataz.internal.util.TestHelper.consumeInputStream;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link org.failearly.dataz.internal.util.ResourceUtils}.
 */
public class ResourceUtilsTest {

    @Test
    public void resourceExistsInClassPath() throws Exception {
        assertThat("None Existing resource?", ResourceUtils.resourceExistsInClassPath(ResourceUtilsTest.class, "unknown"),
                is(false));
        assertThat("Existing resource?",
                ResourceUtils.resourceExistsInClassPath(ResourceUtilsTest.class, "/org/failearly/dataz/internal/util/ResourceUtilsTest.txt"),
                is(true));
    }

    @Test
    public void openNoneExistingResource() throws Exception {
        final InputStream resource = ResourceUtils.openResource(ResourceUtilsTest.class, "unknown");
        consumeInputStream(resource);
        assertThat("None Existing resource?",
                resource,
                sameInstance(ClosedInputStream.CLOSED_INPUT_STREAM));
    }

    @Test
    public void openExistingResource() throws Exception {
        final InputStream resource = ResourceUtils.openResource(ResourceUtilsTest.class, "/org/failearly/dataz/internal/util/ResourceUtilsTest.txt");
        consumeInputStream(resource);
        assertThat("Existing resource?", resource,
                instanceOf(AutoCloseInputStream.class));
    }

}