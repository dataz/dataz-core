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

import org.failearly.common.annotations.utils.OptBoolean;
import org.failearly.common.annotations.utils.OptCharacter;
import org.junit.Test;


/**
 * How to access columns of a single record?
 * - by index
 * - by column names
 * - by file header
 */
public class CsvGenerator_LineHandling_Test extends CsvGeneratorTestBase {
    public CsvGenerator_LineHandling_Test() {
        super(
            TestFixture.class
        );
    }


    @Test
    public void how_to_ignore_empty_lines() throws Exception {
        assertCsvGenerator( //
            "ignoreEmptyLines_is_True", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
    }

    @Test
    public void what_is_the_default_behaviour_with_empty_lines() throws Exception {
        assertCsvGenerator( //
            "ignoreEmptyLines_is_False", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
    }

    @Test
    public void how_to_use_comment_marker_files() throws Exception {
        assertCsvGenerator( //
            "commentMarker_is_hash", //
            templateAccessByColumnNames("A", "B"), //
            "1: 'a', 'b'\n2: 'x', 'y'\n" //
        );
    }

    @Test
    public void what_happens_if_there_is_no_comment_marker_set() throws Exception {
        assertCsvGenerator( //
            "noCommentMarker", //
            templateAccessByColumnNames("A", "B"), //
            "1: '# 1', ' 2'\n2: 'a', 'b'\n3: '# 3', ' 4'\n4: 'x', 'y'\n5: '# 4', ' 5'\n6: '# 3', ' 6'\n" //
        );
    }


    @SuppressWarnings("unused")
    private static class TestFixture {
        @CsvGenerator(name = DTON, file = "with_empty_lines.csv", properties = @CsvProperties(columnNames = {"A", "B"}, ignoreEmptyLines = OptBoolean.TRUE))
        void ignoreEmptyLines_is_True() {}

        @CsvGenerator(name = DTON, file = "simple.csv", properties = @CsvProperties(columnNames = {"A", "B"}, ignoreEmptyLines = OptBoolean.DEFAULT))
        void ignoreEmptyLines_is_False() {}

        @CsvGenerator(name = DTON, file = "with_comment_marker.csv", properties = @CsvProperties(columnNames = {"A", "B"},
                commentMarker = @OptCharacter(value = '#', use= OptCharacter.Use.VALUE)))
        void commentMarker_is_hash() {}

        @CsvGenerator(name = DTON, file = "with_comment_marker.csv", properties = @CsvProperties(columnNames = {"A", "B"},
            commentMarker = @OptCharacter(value = '#', use= OptCharacter.Use.DEFAULT)))
        void noCommentMarker() {}
    }
}
