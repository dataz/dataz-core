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

package org.failearly.dataz.template.generator.csv;

import org.failearly.common.annotations.utils.OptEnum;
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
