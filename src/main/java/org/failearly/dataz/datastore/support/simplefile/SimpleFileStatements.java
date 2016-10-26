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
