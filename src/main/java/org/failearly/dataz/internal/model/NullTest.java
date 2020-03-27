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

package org.failearly.dataz.internal.model;

import org.failearly.dataz.NoDataSet;

/**
 * NullTest handles test methods which has been annotated for example with {@link NoDataSet}.
 */
final class NullTest implements AtomicTest {
    private final String name;

    NullTest(String methodName) {
        name = methodName;
    }


    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getName() {
        return name;

    }

    @Override
    public void setup() {
    }

    @Override
    public void cleanup() {
    }

    @Override
    public boolean isSuppressCleanup() {
        return true;
    }
}
