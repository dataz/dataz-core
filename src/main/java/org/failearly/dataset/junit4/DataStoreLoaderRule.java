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
package org.failearly.dataset.junit4;

import org.failearly.dataset.datastore.DataStores;
import org.failearly.dataset.internal.junit.TestRuleBase;
import org.failearly.dataset.internal.junit.TestRuleSupport;
import org.junit.rules.TestRule;

/**
 * DataStoreHandlerRule is responsible for loading {@link org.failearly.dataset.datastore.DataStore}s for the test instance.
 */
public final class DataStoreLoaderRule extends TestRuleBase<Object> {
    private static final TestRuleSupport<DataStoreLoaderRule> support = new TestRuleSupport<>(DataStoreLoaderRule.class);

    private DataStoreLoaderRule() {
    }

    public static TestRule createDataStoreLoaderRule(Object testInstance, Object context) {
        return createDataStoreLoaderRule(testInstance.getClass(), context);
    }

    public static TestRule createDataStoreLoaderRule(Class<?> testClass, Object context) {
        return support.createTestRule(testClass, context);
    }

    @Override
    protected void initialize(Class<?> testClass, Object context) {
        DataStores.loadDataStore(testClass, context);
    }


    @Override
    protected void dropTestClass(Class<?> testClass) {
        support.dropInstance(testClass);
    }

    public static void reset() {
        support.cleanInstances();
    }
}
