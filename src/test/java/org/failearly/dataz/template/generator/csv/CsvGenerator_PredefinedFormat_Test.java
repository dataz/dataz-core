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

import org.failearly.dataz.common.OptEnum;
import org.junit.Test;


/**
 * What are the predefined formats?
 */
public class CsvGenerator_PredefinedFormat_Test extends CsvGeneratorTestBase {
    public CsvGenerator_PredefinedFormat_Test() {
        super(
            TestFixture.class
        );
    }


    @Test
    public void what_is_the_default_format() throws Exception {
        assertCsvGenerator( //
            "default_format", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
    }

    @Test
    public void what_are_the_predefined_formats_of_dataz() throws Exception {
        assertCsvGenerator( //
            "default_format", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
        assertCsvGenerator( //
            "comma_format", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
        assertCsvGenerator( //
            "semicolon_format", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
        assertCsvGenerator( //
            "tab_format", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
    }

    @Test
    public void what_are_the_predefined_formats_of_apache() throws Exception {
        assertCsvGenerator( //
            "apache_default_format", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );

        assertCsvGenerator( //
            "apache_tdf_format", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );

        assertCsvGenerator( //
            "apache_excel_format", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );

    }


    @SuppressWarnings("unused")
    private static class TestFixture {
        @CsvGenerator(name = DTON, file = "comma_format.csv", properties = @CsvProperties(columnEnum = @OptEnum(value = Columns.class, use = OptEnum.Use.VALUE)))
        void default_format() {}

        @CsvGenerator(name = DTON, file = "comma_format.csv", properties = @CsvProperties(predefined = PredefinedCsvFormat.COMMA, columnEnum = @OptEnum(value = Columns.class, use = OptEnum.Use.VALUE)))
        void comma_format() {}

        @CsvGenerator(name = DTON, file = "semicolon_format.csv", properties = @CsvProperties(predefined = PredefinedCsvFormat.SEMICOLON, columnEnum = @OptEnum(value = Columns.class, use = OptEnum.Use.VALUE)))
        void semicolon_format() {}

        @CsvGenerator(name = DTON, file = "tab_format.csv", properties = @CsvProperties(predefined = PredefinedCsvFormat.TAB, columnEnum = @OptEnum(value = Columns.class, use = OptEnum.Use.VALUE)))
        void tab_format() {}

        @CsvGenerator(name = DTON, file = "apache_default_format.csv", properties = @CsvProperties(predefined = PredefinedCsvFormat.APACHE_DEFAULT, columnEnum = @OptEnum(value = Columns.class, use = OptEnum.Use.VALUE)))
        void apache_default_format() {}

        @CsvGenerator(name = DTON, file = "apache_tdf_format.csv", properties = @CsvProperties(predefined = PredefinedCsvFormat.APACHE_TDF, columnEnum = @OptEnum(value = Columns.class, use = OptEnum.Use.VALUE)))
        void apache_tdf_format() {}

        @CsvGenerator(name = DTON, file = "apache_excel_format.csv", properties = @CsvProperties(predefined = PredefinedCsvFormat.APACHE_EXCEL, columnEnum = @OptEnum(value = Columns.class, use = OptEnum.Use.VALUE)))
        void apache_excel_format() {}
    }

    public enum Columns {
        A, B;
    }
}
