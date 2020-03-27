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

package org.failearly.dataz.internal.resource;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourceValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataSetResourceBase is responsible for ...
 */
abstract class DataResourceBase implements DataResource {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DataResourceValues dataResourceValues;


    DataResourceBase(DataResourceValues dataResourceValues) {
        this.dataResourceValues = dataResourceValues;
    }

    @Override
    public void generate(TemplateObjects templateObjects) {
        // nothing to generate
    }

    @Override
    public final String getDataSetName() {
        return this.dataResourceValues.getDataSetName();
    }


    @Override
    public final Class<? extends NamedDataStore> getNamedDataStore() {
        return this.dataResourceValues.getDataStore();
    }

    @Override
    public final boolean shouldAppliedOn(DataStore dataStore) {
        return getNamedDataStore().equals(dataStore.getNamedDataStore()) ;
    }

    @Override
    public final String getResource() {
        return this.dataResourceValues.getResource();
    }

    @Override
    public boolean isTransactional() {
        return this.dataResourceValues.isTransactional();
    }

    @Override
    public final boolean isFailOnError() {
        return this.dataResourceValues.isFailOnError();
    }

    @Override
    public String toString() {
        return dataResourceValues.toString();
    }
}
