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

package org.failearly.dataset.test;

import org.failearly.dataset.DataSet;
import org.failearly.dataset.config.Constants;
import org.failearly.dataset.resource.DataResource;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

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
     * @param transactional the DataResource should be transactional or not (all or nothing)
     * @param failOnError should fail on errors or not
     *
     * @return matcher matches all of {@code dataStoreId}, {@code dataSetName}, {@code resource}, {@code transactional} and {@code failOnError}.
     */
    public static Matcher<DataResource> isDataResource(String dataStoreId, String dataSetName, String resource, boolean transactional, boolean failOnError) {
        return allOf(
                hasDataStoreId(dataStoreId),
                hasDataSetName(dataSetName),
                hasResourceValue(resource),
                hasTransactionalValue(transactional),
                hasFailOnErrorValue(failOnError)
        );
    }

    /**
     * Create {@link Matcher} for {@link DataResource} based on ... .
     *
     * @param dataStoreId the ID of the data store (i.e. {@link DataSet#datastore()}).
     * @param dataSetName the data set name (i.e. {@link DataSet#name()}).
     * @param resource the resource name.
     *
     * @return matcher matches all of {@code dataStoreId}, {@code dataSetName} and {@code resource}.
     */
    public static Matcher<DataResource> isDataResource(String dataStoreId, String dataSetName, String resource) {
        return isDataResource(dataStoreId, dataSetName, resource, true, true);
    }

    /**
     * Create {@link Matcher} for default settings {@link DataResource} based on ... .
     *
     * @param resource the resource name.
     * @return matcher matches on the default settings for {@code dataStoreId}, {@code dataSetName}, {@code failOnError} and {@code transactional}.
     */
    public static Matcher<DataResource> isDefaultDataResource(String resource) {
        return isDataResource(Constants.DATASET_DEFAULT_NAME, resource);
    }

    /**
     * Like {@link #isDataResource(String, String, String)} but with {@code dataStoreId} value {@link Constants#DATASET_DEFAULT_DATASTORE_ID}.
     *
     * @param dataSetName the data set name (i.e. {@link DataSet#name()}).
     * @param resource the resource name.

     * @return matcher matches all of {@code dataStoreId}, {@code dataSetName} and {@code resource}.
     */
    public static Matcher<DataResource> isDataResource(String dataSetName, String resource) {
        return isDataResource(Constants.DATASET_DEFAULT_DATASTORE_ID, dataSetName, resource);
    }

    public static Matcher<DataResource> hasDataStoreId(String dataStoreId) {
        return ClosureMatcher.closureMatcher(DataResource::getDataStoreId, equalTo(dataStoreId), "DataStoreId");
    }

    public static Matcher<DataResource> hasDataSetName(final String dataSetName) {
        return ClosureMatcher.closureMatcher(DataResource::getDataSetName, equalTo(dataSetName), "DataSetName");
    }

    public static Matcher<DataResource> hasResourceValue(final String resource) {
        return ClosureMatcher.closureMatcher(DataResource::getResource, equalTo(resource), "Resource");
    }

    public static Matcher<DataResource> hasTransactionalValue(final boolean transactional) {
        return ClosureMatcher.closureMatcher(DataResource::isTransactional, is(transactional), "Transactional");
    }

    public static Matcher<DataResource> hasFailOnErrorValue(final boolean failOnError) {
        return ClosureMatcher.closureMatcher(DataResource::isFailOnError, is(failOnError), "FailOnError");
    }
}
