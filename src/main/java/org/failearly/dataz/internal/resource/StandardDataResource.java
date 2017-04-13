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

package org.failearly.dataz.internal.resource;

import org.failearly.dataz.internal.util.IOUtils;
import org.failearly.dataz.resource.DataResourceValues;

import java.io.InputStream;

/**
 * StandardDataSetResource is responsible for opening an existing (standard) resource (no template engine).
 */
final class StandardDataResource extends DataResourceBase {

    private final Class<?> testClass;

    StandardDataResource(DataResourceValues dataResourceValues) {
        super(dataResourceValues);
        this.testClass = dataResourceValues.getTestClass();
    }

    @Override
    public InputStream open() {
        return IOUtils.autoClose(testClass.getResourceAsStream(getResource()));
    }
}
