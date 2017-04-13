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
package org.failearly.dataz.template.generator.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Predefined;

/**
 * PredefinedCsvFormat some common starting points
 */
public enum PredefinedCsvFormat {
    // ------------
    // dataZ predefined CSV formats
    //
    COMMA(','),
    TAB('\t'),
    SEMICOLON(';'),
    DEFAULT(PredefinedCsvFormat.COMMA),
    // ------------
    // CsvFormats of org.apache.commons.csv.CSVFormat.Predefined
    //
    /**
     * @see Predefined#Default
     */
    APACHE_DEFAULT(Predefined.Default),
    /**
     * @see Predefined#TDF
     */
    APACHE_TDF(Predefined.TDF),
    /**
     * @see Predefined#RFC4180
     */
    APACHE_RFC4180(Predefined.RFC4180),
    /**
     * @see Predefined#MySQL
     */
    APACHE_MY_SQL(Predefined.MySQL),
    /**
     * @see Predefined#Excel
     */
    APACHE_EXCEL(Predefined.Excel),
    /**
     * @see Predefined#InformixUnload
     */
    APACHE_INFORMIX_UNLOAD(Predefined.InformixUnload),
    /**
     * @see Predefined#InformixUnloadCsv
     */
    APACHE_INFORMIX_UNLOAD_CSV(Predefined.InformixUnloadCsv);

    private final CSVFormat format;

    PredefinedCsvFormat(Predefined predefined) {
        this.format = predefined.getFormat();
    }
    PredefinedCsvFormat(char delimiter) {
        this.format = CSVFormat.newFormat(delimiter);
    }
    PredefinedCsvFormat(PredefinedCsvFormat predefinedCsvFormat) {
        this.format = predefinedCsvFormat.format;
    }


    public CSVFormat format() {
        return format;
    }
}
