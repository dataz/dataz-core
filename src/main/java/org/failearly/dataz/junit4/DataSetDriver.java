/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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

package org.failearly.dataz.junit4;

import org.junit.rules.TestRule;

/**
 * DataSetDriver does the actually work.
 * <p>
 * Applies the {@link org.failearly.dataz.DataSet} on the registered {@link org.failearly.dataz.datastore.DataStore}s.
 */
public final class DataSetDriver {

    private DataSetDriver() {
    }

    /**
     * Create a {@link org.failearly.dataz.junit4.DataSetDriver} from current test instance.
     *
     * @param testInstance {@code this}
     * @return a new driver instance.
     */
    public static TestRule createDataSetDriver(Object testInstance) {
        return DataSetRule.createDataSetRule(testInstance);


    }

    public static void reset() {
        DataSetRule.reset();
    }
}
