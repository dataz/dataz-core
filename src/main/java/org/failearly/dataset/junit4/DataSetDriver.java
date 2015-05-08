/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
 */

package org.failearly.dataset.junit4;

import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

/**
 * DataSetDriver does the actually work.
 * <p>
 * Applies the {@link org.failearly.dataset.DataSet} on the registered {@link org.failearly.dataset.datastore.DataStore}s.
 */
public final class DataSetDriver {

    private DataSetDriver() {
    }

    /**
     * Create a {@link org.failearly.dataset.junit4.DataSetDriver} from current test instance.
     *
     * @param testInstance {@code this}
     * @return a new driver instance.
     */
    public static TestRule createDataSetDriver(Object testInstance) {
        return createDataSetDriver(testInstance, null);
    }

    /**
     * Create a {@link org.failearly.dataset.junit4.DataSetDriver} from current test instance and context.
     *
     * @param testInstance {@code this}
     * @param context      any context to be used for creating a {@link org.failearly.dataset.datastore.DataStore}.
     * @return a new driver instance.
     * @see org.failearly.dataset.datastore.DataStoreFactory#createDataStore(java.lang.annotation.Annotation, Object)
     */
    public static TestRule createDataSetDriver(Object testInstance, Object context) {
        final TestRule dataStoreHandlerRule = DataStoreLoaderRule.createDataStoreLoaderRule(testInstance, context);
        final TestRule dataSetRule = DataSetRule.createDataSetRule(testInstance);

        return RuleChain.emptyRuleChain()
                .around(dataSetRule)
                .around(dataStoreHandlerRule);

    }

    public static void reset() {
        DataStoreLoaderRule.reset();
        DataSetRule.reset();
    }
}
