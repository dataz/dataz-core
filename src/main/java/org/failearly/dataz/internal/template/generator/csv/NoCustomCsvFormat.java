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
