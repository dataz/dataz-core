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
package org.failearly.dataz.template.generator.csv;

import org.apache.commons.csv.CSVFormat;
import org.failearly.dataz.internal.template.generator.csv.CsvFormatResolver;

/**
 * Define your Custom Format.
 *
 * @see CsvProperties#custom()
 */
public abstract class CustomCsvFormat {

    /**
     * Replace the predefined CSVFormat with a new one, if {@link CsvProperties} has been set.
     *
     * @param predefinedCsvFormat the predefined CSVFormat
     * @return the adapted CSVFormat
     */
    public final CSVFormat replaceIfAvailable(CSVFormat predefinedCsvFormat) {
        final CSVFormat customFormat=create();
        if( customFormat==null ) {
            return predefinedCsvFormat;
        }
        return customFormat;
    }

    protected CSVFormat create() {
        final CsvProperties properties = this.getClass().getAnnotation(CsvProperties.class);
        if( properties!=null ) {
            return new CsvFormatResolver(properties).resolve().getCsvFormat();
        }

        return null;
    }

}
