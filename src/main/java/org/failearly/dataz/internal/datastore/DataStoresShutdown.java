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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * DataStoresShutdown is responsible for handling the shutdown process of {@link DataStoresImplementation}.
 */
final class DataStoresShutdown {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataStoresShutdown.class);

    private final List<DataStoresImplementation> currentImplementation = new ArrayList<>();

    DataStoresShutdown() {
        LOGGER.info("Establish shutdown hook!");
        final Thread hook = new Thread(this::shutdownAll);
        hook.setName("dataZ-shutdown-hook");
        hook.setDaemon(true);
        Runtime.getRuntime().addShutdownHook(hook);
    }

    private void shutdownAll() {
        LOGGER.info("Shutdown current DataStores instance!");
        currentImplementation.forEach(DataStoresImplementation::shutdown);
        currentImplementation.clear();
    }

    void setImplementation(DataStoresImplementation dataStoresImplementation) {
        currentImplementation.clear();
        currentImplementation.add(dataStoresImplementation);
    }
}
