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
