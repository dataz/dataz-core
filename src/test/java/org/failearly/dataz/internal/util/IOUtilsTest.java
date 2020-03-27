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

import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.failearly.dataz.internal.util.TestHelper.consumeInputStream;

public class IOUtilsTest {

    public static final byte[] BYTES = "XYZ".getBytes();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void withoutAutoClose() throws Exception {
        // arrange / given
        final InputStream inputStream=Mockito.spy(new ByteArrayInputStream(BYTES));

        // act / when
        consumeInputStream(inputStream);

        // assert / then
        Mockito.verify(inputStream, Mockito.times(BYTES.length+1)).read();
        Mockito.verify(inputStream, Mockito.times(0)).close();
        Mockito.verifyNoMoreInteractions(inputStream);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void autoClose() throws Exception {
        // arrange / given
        final InputStream inputStream=Mockito.spy(new ByteArrayInputStream(BYTES));
        final InputStream wrappedStream = IOUtils.autoClose(inputStream);

        // act / when
        consumeInputStream(wrappedStream);

        // assert / then
        Mockito.verify(inputStream, Mockito.times(BYTES.length+1)).read();
        Mockito.verify(inputStream, Mockito.times(1)).close();
        Mockito.verifyNoMoreInteractions(inputStream);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void onlyOneClose() throws Exception {
        // arrange / given
        final InputStream inputStream=Mockito.spy(new ByteArrayInputStream(BYTES));
        final InputStream wrappedStream = IOUtils.autoClose(inputStream);

        // act / when
        consumeInputStream(wrappedStream);
        wrappedStream.close();
        wrappedStream.close();


        // assert / then
        Mockito.verify(inputStream, Mockito.times(BYTES.length+1)).read();
        Mockito.verify(inputStream, Mockito.times(1)).close();
        Mockito.verifyNoMoreInteractions(inputStream);
    }

}