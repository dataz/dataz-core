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

/**
 * dataSet provides a simple file format which will be used by {@link org.failearly.dataz.datastore.DataStore} implementations.
 * <br><br>
 * If you are not a developing a {@code DataStore} you won't use the {@link org.failearly.dataz.datastore.support.simplefile.SimpleFileParser} or any other of these classes.
 * <br><br>
 * A simple file consists of {@link org.failearly.dataz.datastore.support.simplefile.SimpleFileStatement}s - collected by
 * {@link org.failearly.dataz.datastore.support.simplefile.SimpleFileStatements}. The statement itself won't interpreted by {@code SimpleFileParser}. This responsibility belongs
 * to the {@link org.failearly.dataz.datastore.DataStore} implementation.
 * <br><br>
 * If you are using also the template engine, then first the template engine will be applied and the result of the template engine, becomes the
 * input of {@link org.failearly.dataz.datastore.DataStore}.
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
 * 2. A comment line starts {@code ##} or {@code --}.
 * <br><br>
 * 3. A statement separator is either ...
 * <ul>
 *    <li>one or more comment lines, </li>
 *    <li>one or more empty lines or</li>
 *    <li>{@code ';'} (a semicolon) on the last line (of the current statement) or</li>
 *    <li>the to of file.</li>
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
 * @see org.failearly.dataz.datastore.support.simplefile.SimpleFileParser
 * @see org.failearly.dataz.datastore.DataStore
 */
package org.failearly.dataz.datastore.support.simplefile;