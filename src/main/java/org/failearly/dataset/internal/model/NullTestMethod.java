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

package org.failearly.dataset.internal.model;

import org.failearly.dataset.SuppressDataSet;
import org.failearly.dataset.internal.resource.DataResourceHandler;

import java.util.Objects;

/**
 * NullTestMethod handles test methods which has been annotated for example with {@link SuppressDataSet}.
 */
final class NullTestMethod implements TestMethod {
    private final String name;

    NullTestMethod(String methodName) {
        name = methodName;
    }


    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getName() {
        return name;

    }

    @Override
    public void handleSetupResource(String dataStoreId, DataResourceHandler dataResourceHandler) {
        Objects.requireNonNull(dataStoreId, "DataStore ID must not be null");
        Objects.requireNonNull(dataResourceHandler, "dataSetResourceHandler must not be null");
    }

    @Override
    public void handleCleanupResource(String dataStoreId, DataResourceHandler dataResourceHandler) {
        Objects.requireNonNull(dataStoreId, "DataStore ID must not be null");
        Objects.requireNonNull(dataResourceHandler, "dataSetResourceHandler must not be null");
    }

    @Override
    public boolean isSuppressCleanup() {
        return true;
    }
}
