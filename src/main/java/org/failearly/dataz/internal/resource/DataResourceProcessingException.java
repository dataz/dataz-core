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

package org.failearly.dataz.internal.resource;

import org.failearly.dataz.exception.DataSetException;

/**
 * DataResourceProcessingException will be thrown in case of template processing exceptions
 */
public class DataResourceProcessingException extends DataSetException {
    public DataResourceProcessingException(String resource, Exception ex) {
        super("Exception while processing data resource " + resource, ex);
    }
}
