/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */
package org.failearly.dataset.test;

import org.failearly.dataset.datastore.DataStore;
import org.failearly.dataset.datastore.DataStores;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * FakeDataStoreRule is responsible for ...
 */
public class FakeDataStoreRule extends ExternalResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(FakeDataStoreRule.class);
    private final Class<?> testClass;
    private final List<DataStore> dataStores = new LinkedList<>();

    private FakeDataStoreRule(Class<?> testClass) {
        this.testClass = testClass;
    }

    public static FakeDataStoreRule createFakeDataStoreRule(Class<?> testClass) {
        return new FakeDataStoreRule(testClass);
    }

    public FakeDataStoreRule addDataStore(DataStore dataStore) {
        this.dataStores.add(dataStore);
        return this;
    }

    public FakeDataStoreRule addDataStore(String dataStoreId) {
        this.dataStores.add(new DefaultFakeDataStore(dataStoreId));
        return this;
    }

    @Override
    protected void before() throws Throwable {
        for (DataStore dataStore : dataStores) {
            LOGGER.info("Add dataStore {} ({})", dataStore.getId(), dataStore.getClass().getSimpleName());
            DataStores.addDataStore(testClass, dataStore);
        }
    }

    @Override
    protected void after() {
        LOGGER.info("Reset dataStores");
        DataStores.reset();
    }

}
