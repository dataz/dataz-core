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

import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.resource.DataResource;

/**
 * StatementProcessor applies a single statement within a {@link DataResource} on a {@link DataStore}.
 *
 * @see SimpleFileParser#processStatements(DataResource, Object, StatementProcessor)
 */
@FunctionalInterface
public interface StatementProcessor<T> {
    /**
     * Process a single statement to current {@link DataStore} represented by {@code context}.
     *
     * @param statement the simple file statement.
     * @param context   the context (of the {@link DataStore} - usually a transaction.
     *
     * @throws Exception any exception thrown, while executing the statement
     * @see SimpleFileParser#processStatements(DataResource, Object, StatementProcessor)
     */
    void process(SimpleFileStatement statement, T context) throws Exception;

    /**
     * Execute commit.
     *
     * @param context the context (of the {@link DataStore} - usually a transaction.
     *
     * @throws Exception any exception thrown, while executing the statement
     */
    default void commit(T context) throws Exception {
        // DO NOTHING
    }

}
