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

package org.failearly.dataset.internal.util;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.failearly.dataset.internal.util.TestHelper.consumeInputStream;

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