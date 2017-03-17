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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * AutoCloseInputStream closes the input stream, the moment any of  returns -1.
 */
final class AutoCloseInputStream extends FilterInputStream {


    AutoCloseInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        final int read = super.read();
        if( read==-1 ) {
            close();
        }
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        final int read = super.read(b);
        if( read==-1 ) {
            close();
        }
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        final int read = super.read(b, off, len);
        if( read==-1 ) {
            close();
        }
        return read;
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.in = ClosedInputStream.CLOSED_INPUT_STREAM;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
