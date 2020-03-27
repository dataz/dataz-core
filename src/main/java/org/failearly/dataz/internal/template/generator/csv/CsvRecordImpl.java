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
