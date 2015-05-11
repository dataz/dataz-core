/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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

import org.failearly.dataset.internal.util.IOUtils;
import org.failearly.dataset.resource.DataResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * SimpleFileParser parses the dataSet simple file format.
 * <p>
 * This could be used by the {@link org.failearly.dataset.datastore.DataStore} implementation.
 */
public final class SimpleFileParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFileParser.class);

    private static final String STMT_SEP = ";";
    private static final String COMMENT = "##";

    public SimpleFileParser() {
    }

    private static String chompAndDropCommentLines(String line) {
        line = chomp(line);
        if (line.startsWith(COMMENT)) {
            line = "";
        }
        return line;
    }

    private static String chomp(String line) {
        return line.replaceAll("^\\s*", "").replaceAll("\\s*$", "");
    }

    /**
     * Parse the input stream and handle each recognized statement.
     *
     * @param <T>              any context type
     * @param dataResource     the data resource to parse
     * @param context          the context
     * @param statementHandler the statement handler.    @throws org.failearly.dataset.simplefile.DataSetParseException if {@link #parse(java.io.InputStream)} fails.
     * @throws java.lang.Exception if {@link org.failearly.dataset.simplefile.StatementHandler} causes exception.
     */
    public <T> void parseAndHandleInputStream(DataResource dataResource, T context, StatementHandler<T> statementHandler)
            throws Exception, DataSetParseException {
        final SimpleFileStatements statements = parse(dataResource.open());
        for (SimpleFileStatement statement : statements) {
            handleCurrentStatement(statement, context, statementHandler, dataResource.isFailOnError());
        }
    }

    private <T> void handleCurrentStatement(SimpleFileStatement statement, T context, StatementHandler<T> statementHandler, boolean failOnError) throws Exception {
        try {
            statementHandler.handleStatement(context, statement);
        } catch (Exception ex) {
            if (failOnError) {
                LOGGER.error("Caught exception while handling", ex);
                LOGGER.error("Exception caught. Last statement was {}", statement);
                throw ex;
            } else {
                LOGGER.warn("(Ignored) Exception caught ({}). Last statement was {}.", ex.getMessage(), statement);
            }
        }
    }

    /**
     * Parse and extract all {@link SimpleFileStatements}.
     *
     * @param inputStream the input stream.
     * @return all statements.
     */
    public SimpleFileStatements parse(InputStream inputStream) throws DataSetParseException {
        return doParse(IOUtils.toReader(inputStream));
    }

    private SimpleFileStatements doParse(Reader input) throws DataSetParseException {
        try (final LineNumberReader reader = new LineNumberReader(input)) {
            final StatementsImpl result = new StatementsImpl();
            doParse(result, reader);
            return result;
        } catch (IOException e) {
            throw new DataSetParseException(e);
        }
    }

    private void doParse(StatementsImpl statements, LineNumberReader reader) throws IOException {
        String line;
        final StatementBuilder statementBuilder = new StatementBuilder();

        while (null != (line = reader.readLine())) {
            line = chompAndDropCommentLines(line);
            if (statementBuilder.addPart(line, reader.getLineNumber())) {
                statements.addStatement(statementBuilder.build());
            }
        }

        statements.addStatement(statementBuilder.build());
    }

    private static class StatementBuilder {
        private final List<String> parts = new LinkedList<>();
        private final List<Integer> lines = new LinkedList<>();
        private int statementNo = 1;

        private static String clean(String statement) {
            return chomp(statement.replaceAll(STMT_SEP + "$", ""));
        }

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
                        statementNo,
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
    }

    private static class StatementImpl implements SimpleFileStatement {
        private final int no;
        private final String content;
        private final int firstLine;
        private final int lastLine;

        StatementImpl(int statementNo, String statement, List<Integer> lines) {
            this.no = statementNo;
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
        public int getNo() {
            return no;
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
                    "no=" + no +
                    ", line(s)=" + lines() +
                    ", content='" + content + '\'' +
                    '}';
        }


        private String lines() {
            return firstLine != lastLine ?
                    firstLine + "-" + lastLine :
                    String.valueOf(firstLine);
        }
    }

    private static class StatementsImpl implements SimpleFileStatements {
        private final List<SimpleFileStatement> statements = new LinkedList<>();

        @Override
        public List<SimpleFileStatement> statements() {
            return Collections.unmodifiableList(statements);
        }

        @Override
        public Iterator<SimpleFileStatement> iterator() {
            return statements().iterator();
        }

        void addStatement(SimpleFileStatement statement) {
            if (statement.isValid()) {
                this.statements.add(statement);
            }
        }

    }
}
