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
