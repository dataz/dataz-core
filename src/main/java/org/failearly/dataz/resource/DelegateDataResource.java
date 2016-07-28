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
