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

package org.failearly.dataz.datastore.support.simplefile;

/**
 * Statement contains a single statement with additional infos for debugging purposes.
 */
@SuppressWarnings("UnusedDeclaration")
public interface SimpleFileStatement {

    SimpleFileStatement NULL=new SimpleFileStatement() {};

    /**
     * Set the statement number
     * @param statementNumber the statement number
     */
    default void setStatementNumber(int statementNumber) {}

    /**
     * @return the content of the statement.
     */
    default String getContent() { return  "<null>";}

    /**
     * @return the statement number within the file.
     */
    default int getStatementNumber() { return -1; }

    /**
     * @return The first line number of the statement.
     */
    default int getFirstLine() { return -1; }

    /**
     * @return The last line number of the statement ({@code >= first line}).
     */
    default int getLastLine() { return -1; }

    /**
     * @return {@code true} if the statement is valid.
     */
    default boolean isValid() { return false; }

}
