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
import org.failearly.dataz.common.OptBoolean;
import org.failearly.dataz.common.OptCharacter;
import org.failearly.dataz.common.OptEnum;
import org.failearly.dataz.common.OptString;
import org.failearly.dataz.template.generator.csv.CsvProperties;
import org.failearly.dataz.template.generator.csv.CustomCsvFormat;

import java.util.function.Consumer;

/**
 * CsvFormatResolver is responsible for ...
 */
public final class CsvFormatResolver {
    private final CsvProperties properties;
    private CSVFormat csvFormat;

    public CsvFormatResolver(CsvProperties properties) {
        this.properties = properties;
        this.csvFormat = properties.predefined().format();
    }

    public CsvFormatResolver resolve() {
        applyCustomFormat();
        handleHeaderOptions();
        handleRecordOptions();
        handleColumnOptions();
        return this;
    }

    public CSVFormat getCsvFormat() {
        return this.csvFormat;
    }

    private void applyCustomFormat() {
        final Class<? extends CustomCsvFormat> customFormatClass = this.properties.custom();
        try {
            final CustomCsvFormat customCsvFormat = customFormatClass.newInstance();
            this.csvFormat = customCsvFormat.replaceIfAvailable(this.csvFormat);
        } catch (Exception e) {
            throw new RuntimeException("Cant't create an instance of class " + customFormatClass.getName(), e);
        }
    }


    private void handleColumnOptions() {
        applyOptBoolean(properties.trim(), (val) -> this.csvFormat = this.csvFormat.withTrim(val));
        applyOptCharacter(properties.quoteCharacter(), (val) -> this.csvFormat = this.csvFormat.withQuote(val));
        applyOptCharacter(properties.escapeCharacter(), (val) -> this.csvFormat = this.csvFormat.withEscape(val));
        applyOptString(properties.nullCode(), (val) -> this.csvFormat = this.csvFormat.withNullString(val));
        applyOptCharacter(properties.columnSeparator(), (val)->this.csvFormat = this.csvFormat.withDelimiter(val));
    }

    private void handleRecordOptions() {
        applyOptBoolean(properties.ignoreEmptyLines(), (val) -> this.csvFormat = this.csvFormat.withIgnoreEmptyLines(val));
        applyOptCharacter(properties.commentMarker(), (val) -> this.csvFormat = this.csvFormat.withCommentMarker(val));
    }

    private void handleHeaderOptions() {
        if( ! applyOptEnum(properties.columnEnum(), (val)->this.csvFormat = this.csvFormat.withHeader(val)) ) {
            handleColumnNames();
        }
        applyOptBoolean(properties.ignoreColumnNamesCase(), (val) -> csvFormat = csvFormat.withIgnoreHeaderCase(val));
        applyOptBoolean(properties.skipHeader(), (val) -> csvFormat = csvFormat.withSkipHeaderRecord(val));
    }

    private void handleColumnNames() {
        final String[] columnNames = properties.columnNames();
        if (columnNames.length > 0) {
            csvFormat = csvFormat.withHeader(columnNames);
        } else {
            applyOptBoolean(properties.firstRecordAsHeader(), this::setFirstRecordAsHeader);
        }
    }

    private CSVFormat setFirstRecordAsHeader(boolean val) {
        if (val) {
             csvFormat = csvFormat.withFirstRecordAsHeader();
        }
        return csvFormat;
    }

    private static void applyOptBoolean(OptBoolean optBoolean, Consumer<Boolean> csvFormatFunction) {
        optBoolean.apply(csvFormatFunction);
    }
    
    private static void applyOptCharacter(OptCharacter optCharacter, Consumer<Character> csvFormatFunction) {
        optCharacter.use().applyAnnotationOn(optCharacter, csvFormatFunction);
    }

    private static void applyOptString(OptString optString, Consumer<String> csvFormatFunction) {
        optString.use().applyAnnotationOn(optString, csvFormatFunction);
    }

    private static boolean applyOptEnum(OptEnum optEnum, Consumer<Class<? extends Enum<?>>> csvFormatFunction) {
        optEnum.use().applyAnnotationOn(optEnum, csvFormatFunction);
        return optEnum.use() == OptEnum.Use.VALUE;
    }
}
