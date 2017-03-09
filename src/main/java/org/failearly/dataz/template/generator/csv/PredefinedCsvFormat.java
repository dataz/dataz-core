/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
