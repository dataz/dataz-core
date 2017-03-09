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
package org.failearly.dataz.internal.template.generator.csv;

import org.apache.commons.csv.CSVRecord;
import org.failearly.dataz.template.generator.csv.CsvRecord;

/**
 * CsvRecord represents a single record of the CSV file.
 */
@SuppressWarnings("unused")
public final class CsvRecordImpl implements CsvRecord {

    private final CSVRecord record;
    private final boolean ignoreHeaderCase;
    private final String nullString;

    CsvRecordImpl(CSVRecord record, boolean ignoreHeaderCase, final String nullString) {
        this.record = record;
        this.ignoreHeaderCase = ignoreHeaderCase;
        this.nullString = nullString;
    }


    @Override
    public long getRecordNumber() {
        return record.getRecordNumber();
    }


    @Override
    public String getValue(int index) {
        return replaceNull(this.record.get(index));
    }

    @Override
    public String getValue(String columnName) {
        if( this.ignoreHeaderCase ) {
            columnName = columnName.toLowerCase();
        }
        return replaceNull(this.record.get(columnName));
    }

    private String replaceNull(String value) {
        if(value==null) {
            return nullString;
        }
        return value;
    }
}
