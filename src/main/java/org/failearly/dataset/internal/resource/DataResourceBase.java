/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */
package org.failearly.dataset.internal.resource;

import org.failearly.dataset.internal.template.TemplateObjects;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.DataResourceValues;
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
    public final String getDataStoreId() {
        return this.dataResourceValues.getDataStoreId();
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
    public <T> T getAdditionalValue(String key, T defaultValue) {
        return dataResourceValues.getAdditionalValue(key, defaultValue);
    }

    @Override
    public String toString() {
        return dataResourceValues.toString();
    }
}
