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

import org.failearly.dataz.common.OptBoolean;
import org.failearly.dataz.common.OptCharacter;
import org.failearly.dataz.common.OptString;
import org.junit.Test;


/**
 * How to handle columns of a single record?
 */
public class CsvGenerator_ColumnHandling_Test extends CsvGeneratorTestBase {
    public CsvGenerator_ColumnHandling_Test() {
        super(
            TestFixture.class
        );
    }

    @Test
    public void how_to_set_custom_column_separator() throws Exception {
        assertCsvGenerator( //
            "custom_column_separator", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
    }


    @Test
    public void how_to_handle_empty_spaces() throws Exception {
        assertCsvGenerator( //
            "trim_is_enabled", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
        assertCsvGenerator( //
            "trim_is_disabled", //
            templateAccessByColumnNames("A", "B"), //
            "1: ' a ', ' b   '\n2: 'x', 'y'\n" //
        );
    }


    @Test
    public void how_to_handle_quote_character() throws Exception {
        assertCsvGenerator( //
            "quoteCharacter_is_set", //
            templateAccessByColumnNames("A", "B"), //
            "1: ' a ', 'b'\n2: 'x', 'y'\n" //
        );
        assertCsvGenerator( //
            "quoteCharacter_is_not_set", //
            templateAccessByColumnNames("A", "B"), //
            "1: '\" a \"', '\"b\"'\n2: 'x', 'y'\n" //
        );
    }

    @Test
    public void how_to_set_null_code() throws Exception {
        assertCsvGenerator( //
            "nullCode_is_set", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'NULL', 'NULL'\n2: '', ''\n3: 'x', 'y'\n" //
        );
        assertCsvGenerator( //
            "nullCode_is_not_set", //
            templateAccessByColumnNames("A", "B"), //
            "1: '(nil)', '(nil)'\n2: 'NULL', 'NULL'\n3: 'x', 'y'\n" //
        );
    }

    @Test
    public void how_to_replace_null_with_custom_value() throws Exception {
        assertCsvGenerator( //
            "nullString_is_set", //
            templateAccessByColumnNames("A", "B"), //
            "1: '--', '--'\n2: '', ''\n3: 'x', 'y'\n" //
        );
    }

    @Test
    public void how_to_handle_special_characters() throws Exception {
        assertCsvGenerator( //
            "escapeCharacter_is_set", //
            templateAccessByColumnNames("A", "B"), //
            "1: '\t', '\r'\n2: '/', '\n'\n3: ',', '/x'\n" //
        );
        assertCsvGenerator( //
            "escapeCharacter_is_not_set", //
            templateAccessByColumnNames("A", "B"), //
            "1: '/t', '/r'\n2: '//', '/n'\n3: '/', ''\n" //
        );
    }

    @Test
    public void how_to_use_custom_csv_properties() throws Exception {
        assertCsvGenerator( //
            "useCustomProperties", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
    }



    @SuppressWarnings("unused")
    private static class TestFixture {
        @CsvGenerator(name = DTON, file = "with_custom_record_separator.csv", properties = @CsvProperties(columnNames = {"A", "B"},
            columnSeparator = @OptCharacter(value = '|', use = OptCharacter.Use.VALUE)//
        ))
        void custom_column_separator() {}

        @CsvGenerator(name = DTON, file = "with_spaces.csv", properties = @CsvProperties(columnNames = {"A", "B"},//
            trim = OptBoolean.TRUE))
        void trim_is_enabled() {}

        @CsvGenerator(name = DTON, file = "with_spaces.csv", properties = @CsvProperties(columnNames = {"A", "B"}, //
            trim = OptBoolean.FALSE))
        void trim_is_disabled() {}

        @CsvGenerator(name = DTON, file = "with_quote_character.csv", properties = @CsvProperties(columnNames = {"A", "B"},//
            quoteCharacter = @OptCharacter(value = '"', use = OptCharacter.Use.VALUE)))
        void quoteCharacter_is_set() {}

        @CsvGenerator(name = DTON, file = "with_quote_character.csv", properties = @CsvProperties(columnNames = {"A", "B"}, //
            quoteCharacter = @OptCharacter(use = OptCharacter.Use.VALUE)))
        void quoteCharacter_is_not_set() {}

        @CsvGenerator(name = DTON, file = "with_empty_columns.csv", properties = @CsvProperties(columnNames = {"A", "B"}, //
            nullCode = @OptString(value = "(nil)", use = OptString.Use.VALUE)))
        void nullCode_is_set() {}

        @CsvGenerator(name = DTON, file = "with_empty_columns.csv", properties = @CsvProperties(columnNames = {"A", "B"}, //
            nullCode = @OptString(use = OptString.Use.VALUE)))
        void nullCode_is_not_set() {}

        @CsvGenerator(name = DTON, file = "with_empty_columns.csv", properties = @CsvProperties(columnNames = {"A", "B"}, //
            nullCode = @OptString(value = "(nil)", use = OptString.Use.VALUE), //
            nullString = "--" ))
        void nullString_is_set() {}

        @CsvGenerator(name = DTON, file = "with_escape_character.csv", properties = @CsvProperties(columnNames = {"A", "B"}, //
            escapeCharacter = @OptCharacter(value='/', use= OptCharacter.Use.VALUE)))
        void escapeCharacter_is_set() {}

        @CsvGenerator(name = DTON, file = "with_escape_character.csv", properties = @CsvProperties(columnNames = {"A", "B"}, //
            escapeCharacter = @OptCharacter(value='/', use= OptCharacter.Use.DEFAULT)))
        void escapeCharacter_is_not_set() {}

        @CsvGenerator(name = DTON, file = "with_semicolon_and_empty_lines.csv", properties = @CsvProperties(custom = MyCustomProperties.class))
        void useCustomProperties() {}
    }

    @SuppressWarnings("WeakerAccess")
    @CsvProperties(predefined = PredefinedCsvFormat.SEMICOLON, columnNames = {"A", "B"}, ignoreEmptyLines = OptBoolean.TRUE)
    public static class MyCustomProperties extends CustomCsvFormat {}
}
