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
