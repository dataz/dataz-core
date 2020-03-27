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
package org.failearly.dataz.internal.template.generator.csv;

import org.apache.commons.csv.CSVFormat;
import org.failearly.dataz.template.generator.csv.CustomCsvFormat;

/**
 * NoCustomCsvFormat is responsible for ...
 */
public final class NoCustomCsvFormat extends CustomCsvFormat {
    @Override
    protected CSVFormat create() {
        return null;
    }
}
