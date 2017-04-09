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

import org.failearly.dataz.common.OptBoolean;
import org.failearly.dataz.common.OptEnum;
import org.junit.Test;


/**
 * How to access columns of a single record?
 * - by index
 * - by column names
 * - by file header
 */
public class CsvGenerator_ColumnAccess_Test extends CsvGeneratorTestBase {
    public CsvGenerator_ColumnAccess_Test() {
        super(
            TestFixture.class          // Test Fixture
        );
    }


    @Test
    public void how_to_access_records_by_column_index() throws Exception {
        assertCsvGenerator( //
            "csvWithoutHeader", //
            "#foreach($g in $TO)$g.recordNumber: '$g.getValue(0)', '$g.getValue(1)'\n#end", //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
    }


    @Test
    public void how_to_access_records_by_files_header_names() throws Exception {
        assertCsvGenerator( //
            "csvWithHeader", //
            templateAccessByColumnNames("Head1", "Head2"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
    }

    @Test
    public void how_to_access_records_by_files_header_names_ignoring_header_case() throws Exception {
        assertCsvGenerator( //
            "csvWithHeaderIgnoreCase", //
            templateAccessByColumnNames("hEAD1", "Head2"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
    }

    @Test
    public void how_to_access_records_by_user_defined_column_names() throws Exception {
        assertCsvGenerator( //
            "customColumnNames", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'Head1', 'Head2'\n2: 'a', 'b'\n3: 'x', 'y'\n" //
        );
    }

    @Test
    public void how_to_access_records_by_user_defined_column_names__but_file_has_a_header() throws Exception {
        assertCsvGenerator( //
            "customColumnNamesSkipHeader", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
    }

    @Test
    public void what_happens_if_using_conflicting_column_properties_columnNames__and__firstRecordAsHeader() throws Exception {
        assertCsvGenerator( //
            "conflictingHeaderProperties", //
            templateAccessByColumnNames("A", "B"), //
            // Remarks:
            // - The "columnNames" property will always supersede "firstRecordAsHeader"
            // - Without skipHeader=true, the header will be used as record.
            "1: 'Head1', 'Head2'\n2: 'a', 'b'\n3: 'x', 'y'\n" //
        );
    }

    @Test
    public void how_to_access_records_by_user_defined_enums() throws Exception {
        assertCsvGenerator( //
            "useEnumAsHeader", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'Head1', 'Head2'\n2: 'a', 'b'\n3: 'x', 'y'\n" //
        );
    }

    @SuppressWarnings("unused")
    private static class TestFixture {
        @CsvGenerator(name = DTON, file = "without_header.csv")
        void csvWithoutHeader() {
        }

        @CsvGenerator(name = DTON, file = "with_header.csv", properties = @CsvProperties(firstRecordAsHeader = OptBoolean.TRUE))
        void csvWithHeader() {
        }

        @CsvGenerator(name = DTON, file = "with_header.csv", properties = @CsvProperties(firstRecordAsHeader = OptBoolean.TRUE, ignoreColumnNamesCase = OptBoolean.TRUE))
        void csvWithHeaderIgnoreCase() {
        }

        @CsvGenerator(name = DTON, file = "with_header.csv", properties = @CsvProperties(columnNames = {"A", "B"}))
        void customColumnNames() {
        }

        @CsvGenerator(name = DTON, file = "with_header.csv", properties = @CsvProperties(columnNames = {"A", "B"}, skipHeader = OptBoolean.TRUE))
        void customColumnNamesSkipHeader() {
        }

        @CsvGenerator(name = DTON, file = "without_header.csv", properties = @CsvProperties(columnNames = {"A", "B"}, ignoreColumnNamesCase = OptBoolean.TRUE))
        void customColumnNamesIgnoreCase() {
        }

        @CsvGenerator(name = DTON, file = "with_header.csv", properties = @CsvProperties(columnNames = {"A", "B"}, firstRecordAsHeader = OptBoolean.TRUE))
        void conflictingHeaderProperties() {
        }

        @CsvGenerator(name = DTON, file = "with_header.csv", properties = @CsvProperties(columnEnum = @OptEnum(value = Columns.class, use = OptEnum.Use.VALUE)))
        void useEnumAsHeader() {
        }

    }

    public enum Columns {
        A, B;
    }
}
