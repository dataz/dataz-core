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

package org.failearly.dataz.datastore;

/**
 * DataStoreInitializationException should be thrown in case of initialisation issues.
 *
 * @see DataStore#initialize()
 */
public class DataStoreInitializationException extends DataStoreException {
    public DataStoreInitializationException(String message, Exception ex) {
        super(message, ex);
    }

    public DataStoreInitializationException(String message) {
        super(message);
    }
}
