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

package org.failearly.dataset.simplefile;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SimpleFileParserTest {

    private static final String DBL_DASH_COMMENT =
            "\n" +
                    "   --  Any comment line " +
                    "\n";
    private static final String DBL_HASH_COMMENT =
            "\n" +
            "   ##  Any comment line " +
            "\n";
    private static final String NO_STMTS = DBL_HASH_COMMENT + " ;   \n    \n  " + DBL_DASH_COMMENT;
    private static final String STMTS_SEPARATED_BY_SEMICOLON =
            "  Stmt1;   \n" +
            "        Stmt2;      " +
            "\n";
    private static final String STMTS_SEPARATED_BY_EMPTY_LINE =
            "  Stmt1   " +
            "\n   " +
            "\n        Stmt2      " +
            "\n";
    private static final String MULTI_LINE_STMTS_SEPARATED_BY_EMPTY_LINE =
            "  Stmt1.1 " +
            "\n Stmt1.2   " +
            "\n   " +
            "\n        Stmt2.1 " +
            "\n Stmt2.2     ";
    private static final String MULTI_LINE_STMTS_SEPARATED_BY_SEMICOLON =
            "  Stmt1.1 " +
            "\n Stmt1.2  ;   " +
            "\n        Stmt2.1 " +
             "\n Stmt2.2     ";
    private static final String MULTI_LINE_STMTS_SEPARATED_BY_COMMENT ="  Stmt1.1 \n Stmt1.2 \n ## Any comment  \n        Stmt2.1 \n Stmt2.2     \n";

    private static final String FROM_PACKAGE_DOCUMENTATION=""
+ "## Starting with a comment is OK.                   \n"
+ "1. This is a single statement;                      \n"
+ "2. This is the next statement                       \n"
+ "\n"
+ "3. This is the 3rd statement                        \n"
+ "    ## Any comment            \n"
+ "     4. This is                                     \n"
+ "the first multi                                     \n"
+ "line statement;                                     \n"
+ "5. The next multi                                   \n"
+ "line statement                                      \n"
+ "\n"
+ "6. This will be handle                              \n"
+ "as ... ;                                            \n"
+ "\n"
+ "7. two statements                                   \n"
+ "## A double hash within a statement is not a comment\n"
+ "8. A statement with ## double hash                  \n"
+ "\n"
+ "-- Another comment                                  \n"
+ "9. Last statement";


    @Test
    public void twoStatementsSeparatedBySemicolons() throws Exception {
        testSimpleFileParser(STMTS_SEPARATED_BY_SEMICOLON,
                "Statement{no=1, line(s)=1, content='Stmt1'}",
                "Statement{no=2, line(s)=2, content='Stmt2'}");
        testSimpleFileParser(DBL_HASH_COMMENT +STMTS_SEPARATED_BY_SEMICOLON,
                "Statement{no=1, line(s)=3, content='Stmt1'}",
                "Statement{no=2, line(s)=4, content='Stmt2'}");
    }

    @Test
    public void twoStatementsSeparatedByEmptyLine() throws Exception {
        testSimpleFileParser(STMTS_SEPARATED_BY_EMPTY_LINE,
                "Statement{no=1, line(s)=1, content='Stmt1'}",
                "Statement{no=2, line(s)=3, content='Stmt2'}"
        );
        testSimpleFileParser(DBL_HASH_COMMENT +STMTS_SEPARATED_BY_EMPTY_LINE+ DBL_HASH_COMMENT,
                "Statement{no=1, line(s)=3, content='Stmt1'}",
                "Statement{no=2, line(s)=5, content='Stmt2'}"
        );
    }

    @Test
    public void multiLineStatementsSeparatedByEmptyLine() throws Exception {
        testSimpleFileParser(MULTI_LINE_STMTS_SEPARATED_BY_EMPTY_LINE,
                "Statement{no=1, line(s)=1-2, content='Stmt1.1 Stmt1.2'}",
                "Statement{no=2, line(s)=4-5, content='Stmt2.1 Stmt2.2'}");
        testSimpleFileParser(DBL_HASH_COMMENT +MULTI_LINE_STMTS_SEPARATED_BY_EMPTY_LINE+ DBL_HASH_COMMENT,
                "Statement{no=1, line(s)=3-4, content='Stmt1.1 Stmt1.2'}",
                "Statement{no=2, line(s)=6-7, content='Stmt2.1 Stmt2.2'}");
    }

    @Test
    public void multiLineStatementsSeparatedBySemicolon() throws Exception {
        testSimpleFileParser(MULTI_LINE_STMTS_SEPARATED_BY_SEMICOLON,
                "Statement{no=1, line(s)=1-2, content='Stmt1.1 Stmt1.2'}",
                "Statement{no=2, line(s)=3-4, content='Stmt2.1 Stmt2.2'}");
    }

    @Test
    public void multiLineStatementsSeparatedByComment() throws Exception {
        testSimpleFileParser(MULTI_LINE_STMTS_SEPARATED_BY_COMMENT,
                "Statement{no=1, line(s)=1-2, content='Stmt1.1 Stmt1.2'}",
                "Statement{no=2, line(s)=4-5, content='Stmt2.1 Stmt2.2'}"
        );
    }

    @Test
    public void testPackageDocumentation() throws Exception {
        testSimpleFileParser(FROM_PACKAGE_DOCUMENTATION,
                "Statement{no=1, line(s)=2, content='1. This is a single statement'}",
                "Statement{no=2, line(s)=3, content='2. This is the next statement'}",
                "Statement{no=3, line(s)=5, content='3. This is the 3rd statement'}",
                "Statement{no=4, line(s)=7-9, content='4. This is the first multi line statement'}",
                "Statement{no=5, line(s)=10-11, content='5. The next multi line statement'}",
                "Statement{no=6, line(s)=13-14, content='6. This will be handle as ...'}",
                "Statement{no=7, line(s)=16, content='7. two statements'}",
                "Statement{no=8, line(s)=18, content='8. A statement with ## double hash'}",
                "Statement{no=9, line(s)=21, content='9. Last statement'}"
        );
    }

    @Test
    public void noStatements() throws Exception {
        testSimpleFileParser(NO_STMTS);
    }

    private void testSimpleFileParser(String stmts, String... expectedStatements) throws IOException {
        // arrange / given
        final InputStream inputStream=createInputStream(stmts);

        // act / when
        final SimpleFileStatements statements=new SimpleFileParser().parse(inputStream);

        // assert / then
        if( expectedStatements.length>0 )
            assertThat("Statements?", toString(statements), contains(expectedStatements));
        else
            assertThat("No Statements?", statements.statements(), hasSize(0));

        Mockito.verify(inputStream, Mockito.times(1)).close();
    }

    private static List<String> toString(SimpleFileStatements stmts) {
        return stmts.statements().stream().map(SimpleFileStatement::toString).collect(Collectors.toList());
    }



    private static InputStream createInputStream(String stmts) {
        return Mockito.spy(new ByteArrayInputStream(stmts.getBytes()));
    }


}