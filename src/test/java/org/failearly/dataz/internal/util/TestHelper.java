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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * TestHelper is responsible for ...
 */
final class TestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestHelper.class);

    private TestHelper() {
    }

    static void consumeInputStream(InputStream inputStream) throws IOException {
        int val=0;
        while(-1!=val) {
            val=inputStream.read();
            LOGGER.debug("Consume input stream: Read value='{}' ({})", (char)val, val);
        }
    }
}
