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

package org.failearly.dataz.exception;

/**
 * DataSetException is the base class for all dataSet specific exceptions.
 */
public class DataSetException extends RuntimeException {
    public DataSetException(String message) {
        super(message);
    }

    public DataSetException(String message, Throwable cause) {
        super(message, cause);
    }
}
