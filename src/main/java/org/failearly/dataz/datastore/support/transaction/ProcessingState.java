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
 * The potential processing states for {@link PerDataResourceProvider#process(Object, DataResource)}
 * and {@link PerStatementProvider#process(Object, DataResource)}.
 *
 * @see PerDataResourceProvider#close(Object, ProcessingState)
 * @see PerStatementProvider#close(Object, ProcessingState)
 */
public enum ProcessingState {
    OK,
    ERROR
}
