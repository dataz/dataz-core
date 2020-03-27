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
package org.failearly.dataz.test;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.internal.common.builder.BuilderBase;
import org.failearly.dataz.resource.DataResource;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;

import static org.failearly.dataz.internal.common.resource.ResourcePathUtils.defaultClassResource;
import static org.failearly.dataz.internal.common.resource.ResourcePathUtils.resourcePath;
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
