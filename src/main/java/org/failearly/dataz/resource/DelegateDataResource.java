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
package org.failearly.dataz.resource;

import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.internal.resource.DataResourceProcessingException;
import org.failearly.dataz.internal.template.TemplateObjects;

import java.io.InputStream;

/**
 * DataResourceDelegate is responsible for ...
 */
public abstract class DelegateDataResource implements DataResource {

    private final DataResource origin;

    protected DelegateDataResource(DataResource origin) {
        this.origin = origin;
    }

    @Override
    public Class<? extends NamedDataStore> getNamedDataStore() {
        return origin.getNamedDataStore();
    }

    @Override
    public String getDataSetName() {
        return this.origin.getDataSetName();
    }

    @Override
    public String getResource() {
        return this.origin.getResource();
    }

    @Override
    public boolean isTransactional() {
        return this.origin.isTransactional();
    }

    @Override
    public void generate(TemplateObjects templateObjects) throws DataResourceProcessingException {
        this.origin.generate(templateObjects);
    }

    @Override
    public InputStream open() throws DataSetException {
        return this.origin.open();
    }

    @Override
    public boolean isFailOnError() {
        return this.origin.isFailOnError();
    }

    @Override
    public boolean shouldAppliedOn(DataStore dataStore) {
        return this.origin.shouldAppliedOn(dataStore);
    }
}
