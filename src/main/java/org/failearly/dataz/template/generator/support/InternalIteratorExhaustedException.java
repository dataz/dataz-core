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

package org.failearly.dataz.template.generator.support;

import org.failearly.dataz.exception.DataSetException;

/**
 * InternalIteratorExhaustedException is responsible for ...
 */
public class InternalIteratorExhaustedException extends DataSetException {
    public InternalIteratorExhaustedException(String generatorName) {
        super("Internal iterator of generator " + generatorName + " is exhausted!");
    }
}
