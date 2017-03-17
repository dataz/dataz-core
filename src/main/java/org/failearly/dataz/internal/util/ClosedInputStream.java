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

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link org.failearly.dataz.internal.util.ClosedInputStream#read} returns always -1.
 */
final class ClosedInputStream extends InputStream {

    public static final InputStream CLOSED_INPUT_STREAM=new ClosedInputStream();

    private ClosedInputStream() {
    }

    @Override
    public int read() throws IOException {
        return -1;
    }
}
