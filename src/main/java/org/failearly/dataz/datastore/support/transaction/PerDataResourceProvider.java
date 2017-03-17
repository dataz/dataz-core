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

package org.failearly.dataz.datastore.support.transaction;

import org.failearly.dataz.resource.DataResource;

/**
 * PerDataResourceProvider is responsible for ...
 */
public interface PerDataResourceProvider<T> {
    T createTransactionContext() throws Exception;

    void process(T transaction, DataResource dataResource) throws Exception;

    void commit(T transaction) throws Exception;

    default void rollback(T transaction) throws Exception {}

    default void close(T transaction, ProcessingState processingState) throws Exception {}
}
