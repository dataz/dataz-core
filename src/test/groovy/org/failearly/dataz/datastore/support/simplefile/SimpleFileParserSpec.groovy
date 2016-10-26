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


package org.failearly.dataz.datastore.support.simplefile

import org.failearly.dataz.resource.DataResource
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * SimpleFileParserSpec contains specification for ... .
 */
@Subject(SimpleFileParser)
class SimpleFileParserSpec extends Specification {
    private final parser = new SimpleFileParser()
    private final AnyTransactionalContext anyContext = new AnyTransactionalContext()

    @Unroll
    def "How to parse a single statement? - #content"() {
        given: "a data resource (returning #statement)"
        def dataResource = Stub(DataResource) {
            open() >> asInputStream(content)
        }

        and: "a statement processor"
        def statementProcessor = new TestStatementProcessor()

        when: "process statements within dataResource"
        parser.processStatements(dataResource, anyContext, statementProcessor)

        then: "the processor should be called with the statement"
        statementProcessor.statements == expected

        where:
        content                                                    || expected
        "This is a single statement"                               || ["This is a single statement"]
        "   This is a single statement  "                          || ["This is a single statement"]
        "\tThis is a single statement\t"                           || ["This is a single statement"]
        "\nThis is a single statement\n"                           || ["This is a single statement"]
        "This is a single statement\nover multiple\nlines"         || ["This is a single statement over multiple lines"]
        "  This is a single statement \n  over multiple \n lines " || ["This is a single statement over multiple lines"]
    }

    @Unroll
    def "How to parse multiple statements? - #content"() {
        given: "a data resource (returning #content)"
        def dataResource = Stub(DataResource) {
            open() >> asInputStream(content)
        }

        and: "a statement processor"
        def statementProcessor = new TestStatementProcessor()

        when: "process statements within dataResource"
        parser.processStatements(dataResource, anyContext, statementProcessor)

        then: "the processor should be called with each statements"
        statementProcessor.statements == expected

        where:
        content                                         || expected
        "First Statement;\nSecond Statement"            || ["First Statement", "Second Statement"]
        "First Statement\n\nSecond Statement"           || ["First Statement", "Second Statement"]
        "First Statement\n        \nSecond Statement"   || ["First Statement", "Second Statement"]
        "First Statement\n## Comment\nSecond Statement" || ["First Statement", "Second Statement"]
        "First Statement\n-- Comment\nSecond Statement" || ["First Statement", "Second Statement"]
        "First Statement\n@@COMMIT\nSecond Statement"   || ["First Statement", "Second Statement"]
    }

    @Unroll
    def "What is a simple file comment? - #content"() {
        given: "a data resource (returning #content)"
        def dataResource = Stub(DataResource) {
            open() >> asInputStream(content)
        }

        and: "a statement processor"
        def statementProcessor = new TestStatementProcessor()

        when: "process statements within dataResource"
        parser.processStatements(dataResource, anyContext, statementProcessor)

        then: "the processor should be called with each statements"
        statementProcessor.statements == expected

        where:
        content                     || expected
        "## This is a comment"      || [/* NO statements*/]
        "     ## This is a comment" || [/* NO statements*/]
        "-- This is a comment"      || [/* NO statements*/]
        "    -- This is a comment"  || [/* NO statements*/]
        "This is ## a statement"    || ["This is ## a statement"]
        "This is -- a statement"    || ["This is -- a statement"]
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Unroll
    def "How to handle COMMIT statements? - #content"() {
        given: "a data resource (returning #content)"
        def dataResource = Stub(DataResource) {
            open() >> asInputStream(content)
        }

        and: "a statement processor"
        def statementProcessor = new TestStatementProcessor()

        when: "process statements within dataResource"
        parser.processStatements(dataResource, anyContext, statementProcessor)

        then: "expected number of commit should be #expectedCommits"
        statementProcessor.numberOfCommits == expectedCommits

        and: "the processed statements are"
        statementProcessor.statements == expectedStatements

        where:
        content                        || expectedCommits | expectedStatements
        "No commit"                    || 0 | ["No commit"]

        // Commit without argument
        "@@COMMIT"                     || 0 | []           // Nothing to commit
        "S1;\n@@COMMIT"                || 1 | ["S1"]       // One statement to commit
        "S1;\n     @@COMMIT"           || 1 | ["S1"]       // and with trailing spaces
        "S1;\n@@COMMIT\n@@COMMIT"      || 1 | ["S1"]       // second commit will be skipped
        "S1;\n@@COMMIT\nS2;\n@@COMMIT" || 2 | ["S1", "S2"] // Commit after each statement

        // Commit with argument (after 2 or 3 statements)
        "@@COMMIT 2"                   || 0 | []
        "S1;\n@@COMMIT 2"              || 0 | ["S1"]
        "S1;\nS2;\n@@COMMIT 2"         || 1 | ["S1", "S2"]
        "S1;\nS2;\n@@COMMIT 3"         || 0 | ["S1", "S2"]
        "S1;\nS2;\nS3;\n@@COMMIT 3"    || 1 | ["S1", "S2", "S3"]

        // Next statement will be executed without commit
        "S1;\nS2;\n@@COMMIT 2\nS3"     || 1 | ["S1", "S2", "S3"]

        // Invalid COMMIT parameter, will be ignored (NO Commits at all)
        "S1;\n@@COMMIT xxx"            || 0 | ["S1"]
        "S1;\n@@COMMIT 0"              || 0 | ["S1"]
        "S1;\n@@COMMIT -1642"          || 0 | ["S1"]

    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Unroll
    def "What should happen if the statement processor throws on process() an exception? - failOnError=#failOnError, exception=#exception"() {
        given: "a statement processor throwing an exception on process"
        def statementProcessor = Mock(StatementProcessor)

        and: "a data resource returning multiple statements (2)"
        def dataResource = Stub(DataResource) {
            open() >> asInputStream(content)
        }

        and: "the data resource has been marked with failOnError=#failOnError"
        dataResource.isFailOnError() >> failOnError

        when: "process statements of data resource"
        def caughtException = null;
        try {
            parser.processStatements(dataResource, anyContext, statementProcessor)
        }
        catch (Throwable ex) {
            caughtException = ex
        }

        then: "should be an exception, if #failOnError set to true"
        failOnError && caughtException == exception || !failOnError && caughtException == null

        and: "there should be #expectedProcessInvocations on process"
        expectedProcessInvocations * statementProcessor.process(_, anyContext) >> { throw exception }

        where:
        failOnError | exception                                     | content         || expectedProcessInvocations
        true        | new RuntimeException("any runtime exception") | "Stmt1;\nStmt2" || 1
        false       | new RuntimeException("any runtime exception") | "Stmt1;\nStmt2" || 2
        true        | new Exception("any exception")                | "Stmt1;\nStmt2" || 1
        false       | new Exception("any exception")                | "Stmt1;\nStmt2" || 2
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Unroll
    def "What should happen if the statement processor on commit() throws an exception? - failOnError=#failOnError, exception=#exception"() {
        given: "a statement processor throwing an exception on commit"
        def statementProcessor = Mock(StatementProcessor)

        and: "a data resource returning multiple statements (2 statemnts + 2 commits)"
        def dataResource = Stub(DataResource) {
            open() >> asInputStream("Stmt1;\n@@COMMIT\nStmt2;\n@@COMMIT")
        }

        and: "the data resource has been marked with failOnError=#failOnError"
        dataResource.isFailOnError() >> failOnError

        when: "process statements of data resource"
        def caughtException = null;
        try {
            parser.processStatements(dataResource, anyContext, statementProcessor)
        }
        catch (Throwable ex) {
            caughtException = ex
        }

        then: "should be an exception, if #failOnError set to true"
        failOnError && caughtException == exception || !failOnError && caughtException == null

        and: "there should be #expectedCommitInvocations on commit"
        expectedCommitInvocations * statementProcessor.commit(anyContext) >> { throw exception }

        where:
        failOnError | exception                                     || expectedCommitInvocations
        true        | new RuntimeException("any runtime exception") || 1
        false       | new RuntimeException("any runtime exception") || 2
        true        | new Exception("any exception")                || 1
        false       | new Exception("any exception")                || 2
    }

    private static InputStream asInputStream(def stmts) {
        return new ByteArrayInputStream(stmts.toString().getBytes());
    }

    private static class AnyTransactionalContext {}

    private static class TestStatementProcessor implements StatementProcessor<AnyTransactionalContext> {
        def statements = []
        def numberOfCommits = 0

        @Override
        void process(SimpleFileStatement statement, AnyTransactionalContext context) throws Exception {
            statements << statement.content
        }

        @Override
        void commit(AnyTransactionalContext context) throws Exception {
            numberOfCommits += 1
        }
    }
}