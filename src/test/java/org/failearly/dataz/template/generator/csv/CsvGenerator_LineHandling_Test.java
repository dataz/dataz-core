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
import org.failearly.dataz.common.OptCharacter;
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
