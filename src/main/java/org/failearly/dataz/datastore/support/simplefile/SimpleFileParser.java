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

package org.failearly.dataz.datastore.support.simplefile;

import org.failearly.dataz.internal.util.IOUtils;
import org.failearly.dataz.resource.DataResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * SimpleFileParser parses the dataZ simple file format.
 * <p>
 * This could be used by the {@link org.failearly.dataz.datastore.DataStore} implementation.
 */
public final class SimpleFileParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFileParser.class);

    private static final String STMT_SEP = ";";
    private static final String HASH_COMMENT = "##";
    private static final String DASH_COMMENT = "--";

    public SimpleFileParser() {
    }

    private static String chompAndDropCommentLines(String line) {
        line = chomp(line);
        if (line.startsWith(HASH_COMMENT) || line.startsWith(DASH_COMMENT)) {
            line = "";
        }
        return line;
    }

    private static String chomp(String line) {
        return line.replaceAll("^\\s*", "").replaceAll("\\s*$", "");
    }

    /**
     * Applies each statement.
     *
     * Parse the {@code dataResource} and then applies each recognized {@link SimpleFileStatement} on {@link StatementProcessor}.
     *
     * @param dataResource       the data resource to parse
     * @param context            the context
     * @param statementProcessor the statement processor.
     * @param <T>                any context type
     *
     * @throws org.failearly.dataz.datastore.support.simplefile.DataSetParseException if {@link #parse(java.io.InputStream)} fails.
     * @throws java.lang.Exception                                                    if {@link StatementProcessor} causes exception.
     */
    public <T> void processStatements(DataResource dataResource, T context, StatementProcessor<T> statementProcessor)
        throws Exception {
        final SimpleFileStatements statements = parse(dataResource.open());
        final boolean failOnError = dataResource.isFailOnError();

        int statementNumber = 1;
        for (SimpleFileStatement statement : statements) {
            final StatementResult statementResult = processSingleStatement(statementNumber, statement, context, statementProcessor, failOnError);
            switch (statementResult) {
                case RESET_STATEMENT_COUNTER:
                    statementNumber = 1;
                    break;
                case NEXT_STATEMENT:
                    statementNumber += 1;
                    break;
            }
        }
    }

    private enum StatementResult {
        NEXT_STATEMENT,
        RESET_STATEMENT_COUNTER,
        NO_STATEMENT_EXECUTED
    }

    private <T> StatementResult processSingleStatement(
        int statementNumber,
        SimpleFileStatement statement,
        T context,
        StatementProcessor<T> statementProcessor,
        boolean failOnError
    ) throws Exception {
        if (statement instanceof CommitStatement) {
            return doProcessCommitStatement(statementNumber, (CommitStatement)statement, context, statementProcessor, failOnError);
        }
        return doProcessStatement(statement, context, statementProcessor, failOnError);
    }

    private <T> StatementResult doProcessCommitStatement(int statementNumber, CommitStatement statement, T context, StatementProcessor<T> statementProcessor, boolean failOnError) throws Exception {
        try {
            if (statement.isCommit(statementNumber)) {
                statementProcessor.commit(context);
                return StatementResult.RESET_STATEMENT_COUNTER;
            }
        } catch (Exception ex) {
            if (failOnError) {
                LOGGER.error("Caught exception while executing commit.", ex);
                LOGGER.error("Exception caught. Last statement was {}", statement);
                throw ex;
            } else {
                LOGGER.warn("(Ignored) Exception caught ({}). Last statement was {}.", ex.getMessage(), statement);
            }
        }

        return StatementResult.NO_STATEMENT_EXECUTED;
    }

    private <T> StatementResult doProcessStatement(SimpleFileStatement statement, T context, StatementProcessor<T> statementProcessor, boolean failOnError) throws Exception {
        try {
            statementProcessor.process(statement, context);
            return StatementResult.NEXT_STATEMENT;
        } catch (Exception ex) {
            if (failOnError) {
                LOGGER.error("Caught exception while processing statement.", ex);
                LOGGER.error("Exception caught. Last statement was {}", statement);
                throw ex;
            } else {
                LOGGER.warn("(Ignored) Exception caught ({}). Last statement was {}.", ex.getMessage(), statement);
            }
        }

        return StatementResult.NO_STATEMENT_EXECUTED;
    }

    /**
     * Parse and extract all {@link SimpleFileStatements}.
     *
     * @param inputStream the input stream.
     *
     * @return all statements.
     */
    SimpleFileStatements parse(InputStream inputStream) throws DataSetParseException {
        return doParse(IOUtils.toReader(inputStream));
    }

    private SimpleFileStatements doParse(Reader input) throws DataSetParseException {
        try (final LineNumberReader reader = new LineNumberReader(input)) {
            final SimpleFileStatements statements = new SimpleFileStatements();
            doParse(statements, reader);
            return statements;
        } catch (IOException e) {
            throw new DataSetParseException(e);
        }
    }

    private void doParse(SimpleFileStatements statements, LineNumberReader reader) throws IOException {
        String line;
        final StatementBuilder statementBuilder = new StatementBuilder();
        int statementNumber=0;
        while (null != (line = reader.readLine())) {
            line = chompAndDropCommentLines(line);
            if (isCommit(line)) {
                statements.addStatement(statementBuilder.build());
                statements.addStatement(new CommitStatementImpl(line, reader.getLineNumber()));
            } else if (statementBuilder.addPart(line, reader.getLineNumber())) {
                statements.addStatement(statementBuilder.build());
            }
        }

        statements.addStatement(statementBuilder.build());
    }

    private boolean isCommit(String line) {
        return line.startsWith("@@COMMIT");
    }

    private static class StatementBuilder {
        private final List<String> parts = new LinkedList<>();
        private final List<Integer> lines = new LinkedList<>();
        private int statementNo = 1;

        boolean addPart(String part, int lineNumber) {
            if (part.isEmpty()) {
                return true;
            }
            lines.add(lineNumber);
            parts.add(part);
            return part.endsWith(STMT_SEP);
        }

        SimpleFileStatement build() {
            SimpleFileStatement result = SimpleFileStatement.NULL;
            try {
                final String statement = String.join(" ", parts);
                result = new StatementImpl(
                    clean(statement),
                    lines
                );
                return result;
            } finally {
                parts.clear();
                lines.clear();
                if (result.isValid())
                    statementNo += 1;
            }
        }

        private static String clean(String statement) {
            return chomp(statement.replaceAll(STMT_SEP + "$", ""));
        }

    }

    private static class StatementImpl implements SimpleFileStatement {
        private int statementNumber;
        private final String content;
        private final int firstLine;
        private final int lastLine;

        StatementImpl(String statement, List<Integer> lines) {
            this.content = statement;
            if (!lines.isEmpty()) {
                this.firstLine = Collections.min(lines);
                this.lastLine = Collections.max(lines);
            } else {
                this.firstLine = 0;
                this.lastLine = 0;
            }
        }

        @Override
        public void setStatementNumber(int statementNumber) {
            this.statementNumber = statementNumber;
        }

        public int getStatementNumber() {
            return statementNumber;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public int getFirstLine() {
            return firstLine;
        }

        @Override
        public int getLastLine() {
            return lastLine;
        }

        @Override
        public boolean isValid() {
            return !this.content.isEmpty();
        }


        @Override
        public String toString() {
            return "Statement{" +
                "no=" + statementNumber +
                ", line(s)=" + lines() +
                ", content='" + content + '\'' +
                '}';
        }


        protected String lines() {
            return firstLine != lastLine ?
                firstLine + "-" + lastLine :
                String.valueOf(firstLine);
        }
    }

    private static class CommitStatementImpl extends StatementImpl implements CommitStatement {
        private static final int INVALID_COMMIT_ON_VALUE = 0;
        private static final int COMMIT_AFTER_EACH_STATEMENT = 1;
        private final int commitOn;

        CommitStatementImpl(String line, int lineNumber) {
            super(line, Collections.singletonList(lineNumber));
            this.commitOn = reolveCommitOnValue();
        }

        private int reolveCommitOnValue() {
            final String commitOnValue = getContent().replace("@@COMMIT", "").trim();
            if (commitOnValue.isEmpty())
                return COMMIT_AFTER_EACH_STATEMENT;

            try {
                // commitOn >= INVALID_COMMIT_ON_VALUE
                return Math.max(Integer.parseInt(commitOnValue), INVALID_COMMIT_ON_VALUE);
            } catch (NumberFormatException e) {
                LOGGER.warn("Invalid commit statement '{}'. Not a number.", getContent());
            }

            return INVALID_COMMIT_ON_VALUE;
        }

        @Override
        public boolean isCommit(int currentStatementNumber) {
            // Why using last statement number? Because this commit statement
            final int lastStatementNumber=currentStatementNumber-1;

            if( commitOn == INVALID_COMMIT_ON_VALUE) {
                LOGGER.warn("Invalid commit statement '{}'. Ignored!", getContent());
            }

            return commitOn != INVALID_COMMIT_ON_VALUE  // invalid commitOn value ==> always FALSE
                && lastStatementNumber>0   // At least one statement to be committed
                && 0 == lastStatementNumber % commitOn; // It's already time, to commit something?
        }
    }


}
