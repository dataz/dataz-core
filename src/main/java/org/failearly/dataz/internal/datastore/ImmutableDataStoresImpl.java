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
package org.failearly.dataz.internal.datastore;

import org.failearly.dataz.datastore.ImmutableDataStores;
import org.failearly.dataz.internal.datastore.state.DataStoreState;

import java.util.List;

/**
 * MutableDataStores is responsible for ...
 */
final class ImmutableDataStoresImpl extends DataStoresBase implements ImmutableDataStores {
    ImmutableDataStoresImpl(List<DataStoreState> dataStores) {
        super(dataStores);
    }
}
