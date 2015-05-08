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

package org.failearly.dataset.internal.util;

import org.junit.Test;

import java.io.InputStream;

import static org.failearly.dataset.internal.util.TestHelper.consumeInputStream;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link org.failearly.dataset.internal.util.ResourceUtils}.
 */
public class ResourceUtilsTest {

    @Test
    public void resourceExistsInClassPath() throws Exception {
        assertThat("None Existing resource?", ResourceUtils.resourceExistsInClassPath(ResourceUtilsTest.class, "unknown"),
                is(false));
        assertThat("Existing resource?",
                ResourceUtils.resourceExistsInClassPath(ResourceUtilsTest.class, "/org/failearly/dataset/internal/util/ResourceUtilsTest.txt"),
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
        final InputStream resource = ResourceUtils.openResource(ResourceUtilsTest.class, "/org/failearly/dataset/internal/util/ResourceUtilsTest.txt");
        consumeInputStream(resource);
        assertThat("Existing resource?", resource,
                instanceOf(AutoCloseInputStream.class));
    }

}