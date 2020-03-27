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

package org.failearly.dataz.datastore.support.simplefile;

import org.failearly.dataz.exception.DataSetException;

/**
 * Thrown by DataSet in case of any exception while parsing.
 */
public class DataSetParseException extends DataSetException {
    public DataSetParseException(Exception ex) {
        super("DataSet parse exception", ex);
    }

    public DataSetParseException(String message, Exception ex) {
        super("DataSet parse exception: " + message, ex);
    }
}
