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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * IOUtils contains IO utility methods.
 */
public final class IOUtils {

    private IOUtils() {
    }

    /**
     * Wraps the input stream into an auto closeable instance.
     * @param is the input stream or {@code null}.
     * @return a new input stream.
     */
    public static InputStream autoClose(InputStream is) {
        if( is==null ) {
            return ClosedInputStream.CLOSED_INPUT_STREAM;
        }

        return new AutoCloseInputStream(is);
    }

    /**
     * Converts any {@link java.io.InputStream} to a {@link java.io.Reader}.
     * @param inputStream the input stream.
     * @return the reader.
     */
    public static Reader toReader(InputStream inputStream) {
        return new InputStreamReader(autoClose(inputStream));
    }

    /**
     * The NULL input stream is an already closed stream.
     * @return an input stream.
     */
    @SuppressWarnings("SameReturnValue")
    public static InputStream nullInputStream() {
        return ClosedInputStream.CLOSED_INPUT_STREAM;
    }
}
