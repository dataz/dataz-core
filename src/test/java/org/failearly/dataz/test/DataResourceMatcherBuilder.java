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
package org.failearly.dataz.test;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.internal.util.BuilderBase;
import org.failearly.dataz.resource.DataResource;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;

import static org.failearly.common.resource.ResourcePathUtils.defaultClassResource;
import static org.failearly.common.resource.ResourcePathUtils.resourcePath;
import static org.hamcrest.Matchers.*;

/**
 * DataResourceMatcherBuilder builds {@link Matcher} for {@link DataResource}.
 */
public final class DataResourceMatcherBuilder extends BuilderBase<Matcher<DataResource>> {
    private Matcher<DataResource> dataStoresMatcher = belongsToDataStore(defaultDataStore());

    private Matcher<DataResource> dataSetNameMatcher = hasDataSetName(Constants.DATASET_DEFAULT_NAME);
    private Matcher<DataResource> transactionalMatcher = hasTransactionalValue(Constants.DATASET_DEFAULT_TRANSACTIONAL_VALUE);
    private Matcher<DataResource> failOnErrorMatcher = hasFailOnErrorValue(Constants.DATASET_DEFAULT_FAIL_ON_ERROR_VALUE);
    private Matcher<DataResource> resourcesMatcher = null;

    public static DataResourceMatcherBuilder createWithDefaults() {
        return new DataResourceMatcherBuilder();
    }

    public DataResourceMatcherBuilder withDataSetName(String dataSetName) {
        dataSetNameMatcher = hasDataSetName(dataSetName);
        return this;
    }

    public final DataResourceMatcherBuilder withNamedDataStore(Class<? extends NamedDataStore> namedDataStore) {
        dataStoresMatcher = belongsToDataStore(namedDataStore);
        return this;
    }

    @Deprecated
    DataResourceMatcherBuilder withNamedDataStore(List<Class<? extends NamedDataStore>> namedDataStores) {
        return this;
    }

    public DataResourceMatcherBuilder withTransactional(boolean transactional) {
        transactionalMatcher = hasTransactionalValue(transactional);
        return this;
    }

    public DataResourceMatcherBuilder withFailOnError(boolean failOnError) {
        failOnErrorMatcher = hasFailOnErrorValue(failOnError);
        return this;
    }

    public DataResourceMatcherBuilder withResource(String resource) {
        resourcesMatcher = hasResourceValue(resource);
        return this;
    }

    public DataResourceMatcherBuilder withResource(Class<?> clazz, String resource) {
        return this.withResource(resourcePath(resource, clazz));
    }

    public DataResourceMatcherBuilder withDefaultResource(Class<?> clazz, String suffix) {
        return this.withResource(defaultClassResource(clazz, suffix));
    }

    @Override
    protected void checkMandatoryFields() {
        super.checkMandatoryField(resourcesMatcher, "resource matcher");
    }

    @Override
    protected Matcher<DataResource> doBuild() {
        return allOf(dataStoresMatcher, dataSetNameMatcher, resourcesMatcher, transactionalMatcher, failOnErrorMatcher);
    }

    @SafeVarargs
    private static List<Class<? extends NamedDataStore>> datastores(Class<? extends NamedDataStore>... dataStores) {
        return Arrays.asList(dataStores);
    }

    private static Class<? extends NamedDataStore> defaultDataStore() {
        return DataSetProperties.loadDefaultNamedDataStore();
    }

    private static Matcher<DataResource> belongsToDataStore(Class<? extends NamedDataStore> dataStore) {
        return ClosureMatcher.closureMatcher(DataResource::getNamedDataStore, equalTo(dataStore), "Datastore");
    }

    private static Matcher<DataResource> hasDataSetName(final String dataSetName) {
        return ClosureMatcher.closureMatcher(DataResource::getDataSetName, equalTo(dataSetName), "DataSetName");
    }

    private static Matcher<DataResource> hasResourceValue(final String resource) {
        return ClosureMatcher.closureMatcher(DataResource::getResource, equalTo(resource), "Resource");
    }

    private static Matcher<DataResource> hasTransactionalValue(final boolean transactional) {
        return ClosureMatcher.closureMatcher(DataResource::isTransactional, is(transactional), "Transactional");
    }

    private static Matcher<DataResource> hasFailOnErrorValue(final boolean failOnError) {
        return ClosureMatcher.closureMatcher(DataResource::isFailOnError, is(failOnError), "FailOnError");
    }

}
