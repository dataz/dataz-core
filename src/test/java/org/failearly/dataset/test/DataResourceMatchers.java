/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 marko (http://fail-early.com/contact)
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
package org.failearly.dataset.test;

import org.failearly.dataset.DataSet;
import org.failearly.dataset.config.Constants;
import org.failearly.dataset.resource.DataResource;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

/**
 * DataResourceMatchers contains several factory method for {@link DataResource} matchers.
 */
public final class DataResourceMatchers {
    /**
     * Create {@link Matcher} for {@link DataResource} based on ... .
     *
     * @param dataStoreId the ID of the data store (i.e. {@link DataSet#datastore()}).
     * @param dataSetName the data set name (i.e. {@link DataSet#name()}).
     * @param resource the resource name.
     *
     * @return matcher matches all of {@code dataStoreId}, {@code dataSetName} and {@code resource}.
     */
    public static Matcher<DataResource> dataResourceMatcher(String dataStoreId, String dataSetName, String resource) {
        return allOf(
                dataStoreIdMatcher(dataStoreId),
                dataSetNameMatcher(dataSetName),
                resourceMatcher(resource)
        );
    }

    /**
     * Like {@link #dataResourceMatcher(String, String, String)} but with {@code dataStoreId} value {@link Constants#DATASET_DEFAULT_DATASTORE_ID}.
     *
     * @param dataSetName the data set name (i.e. {@link DataSet#name()}).
     * @param resource the resource name.

     * @return matcher matches all of {@code dataStoreId}, {@code dataSetName} and {@code resource}.
     */
    public static Matcher<DataResource> dataResourceMatcher(String dataSetName, String resource) {
        return dataResourceMatcher(Constants.DATASET_DEFAULT_DATASTORE_ID, dataSetName, resource);
    }

    private static Matcher<DataResource> dataStoreIdMatcher(String dataStoreId) {
        return ClosureMatcher.closureMatcher(DataResource::getDataStoreId, equalTo(dataStoreId), "DataStoreId");
    }

    private static Matcher<DataResource> dataSetNameMatcher(final String dataSetName) {
        return ClosureMatcher.closureMatcher(DataResource::getDataSetName, equalTo(dataSetName), "DataSetName");
    }

    private static Matcher<DataResource> resourceMatcher(final String resource) {
        return ClosureMatcher.closureMatcher(DataResource::getResource, equalTo(resource), "Resource");
    }
}
