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

/**
 * dataSet provides a simple file format which will be used by {@link org.failearly.dataset.datastore.DataStore} implementations.
 * <br><br>
 * If you are not a developing a {@code DataStore} you won't use the {@link org.failearly.dataset.simplefile.SimpleFileParser} or any other of these classes.
 * <br><br>
 * A simple file consists of {@link org.failearly.dataset.simplefile.SimpleFileStatement}s - collected by
 * {@link org.failearly.dataset.simplefile.SimpleFileStatements}. The statement itself won't interpreted by {@code SimpleFileParser}. This responsibility belongs
 * to the {@link org.failearly.dataset.datastore.DataStore} implementation.
 * <br><br>
 * If you are using also the template engine, then first the template engine will be applied and the result of the template engine, becomes the
 * input of {@link org.failearly.dataset.datastore.DataStore}.
 * <br><br>
 * <h1>The Simple File Format</h1>
 * There are some simple rules:
 * <br>
 * 1. A statement ...
 * <ul>
 *    <li>... consists of one or more lines.</li>
 *    <li>Any preceding or following spaces/tabs will be removed from statement.</li>
 *    <li>A comment within a statement will not be recognized.</li>
 * </ul>
 * <br>
 * 2. A comment line starts {@code ##}.
 * <br><br>
 * 3. A statement separator is either ...
 * <ul>
 *    <li>one or more comment lines, </li>
 *    <li>one or more empty lines or</li>
 *    <li>{@code ';'} (a semicolon) on the last line (of the current statement) or</li>
 *    <li>the end of file.</li>
 * </ul>
 * <br>
 * 4. Any statement separator will be discarded. So if your DataStore needs a semicolon the DataStore implementation has to add it or you use 2 semicolons.
 * <br><br>
 * 5. Multi line statement become a single statement and the newline character ({@code \n}) will be removed.
 * <br><br>
 *
 * Example:<br><br>
 * <pre>
 *    ## Starting with a comment is OK.
 *    1. This is a single statement;
 *    2. This is the next statement
 *
 *    3. This is the 3rd statement
 *    ## Any comment.
 *    4. This is
 *    the first multi
 *    line statement;
 *    5. The next multi
 *    line statement
 *
 *    6. This will be handle
 *    as ... ;
 *
 *    7. two statements
 *    ## A double hash within a statement is not a comment
 *    8. A statement with ## double hash
 *
 *    ## Another comment
 *    9. Last statement
 * </pre>
 *
 * This results in following statements:<br><br>
 * <ol>
 *    <li>This is a single statement</li>
 *    <li>This is the next statement</li>
 *    <li>This is the 3rd statement</li>
 *    <li>This is the first multi line statement</li>
 *    <li>The next multi line statement</li>
 *    <li>This will be handle as ...</li>
 *    <li>two statements</li>
 *    <li>A statement with ## double hash</li>
 *    <li>Last statement</li>
 * </ol>
 *
 * @see org.failearly.dataset.simplefile.SimpleFileParser
 * @see org.failearly.dataset.datastore.DataStore
 */
package org.failearly.dataset.simplefile;