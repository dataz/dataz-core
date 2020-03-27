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


import org.failearly.dataz.exception.DataSetException;

/**
 * DataStoreException is a generic exception for all Datastore related exceptions.
 */
public class DataStoreException extends DataSetException {
    public DataStoreException(String message, Exception ex) {
        super(message, ex);
    }

    public DataStoreException(String message) {
        super(message);
    }
}
