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
package org.failearly.dataz.datastore.support.simplefile;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * SimpleFileStatements is responsible for ...
 */
final class SimpleFileStatements implements Iterable<SimpleFileStatement>{
    private final List<SimpleFileStatement> statements = new LinkedList<>();
    private int statementNumber=0;

    SimpleFileStatements() {
    }

    List<SimpleFileStatement> statements() {
        return Collections.unmodifiableList(statements);
    }

    public Iterator<SimpleFileStatement> iterator() {
        return statements().iterator();
    }

    void addStatement(SimpleFileStatement statement) {
        statementNumber += 1;
        statement.setStatementNumber(statementNumber);
        if (statement.isValid()) {
            this.statements.add(statement);
        }
    }
}
