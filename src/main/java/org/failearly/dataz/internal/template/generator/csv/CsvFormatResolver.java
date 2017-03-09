/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2017 'Marko Umek' (http://fail-early.com)
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

import org.apache.commons.csv.CSVFormat;
import org.failearly.common.annotations.utils.OptBoolean;
import org.failearly.common.annotations.utils.OptCharacter;
import org.failearly.common.annotations.utils.OptEnum;
import org.failearly.common.annotations.utils.OptString;
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
