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

package org.failearly.dataz.internal.resource;

import org.failearly.common.classutils.ClassLoader;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourceValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * DataSetResourceBase is responsible for ...
 */
abstract class DataResourceBase implements DataResource {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Class<? extends NamedDataStore> defaultDataStore;

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
    public final List<Class<? extends NamedDataStore>> getDataStores() {
        List<Class<? extends NamedDataStore>> dataStores = this.dataResourceValues.getDataStores();
        if (dataStores.isEmpty()) {
            dataStores = getDefaultDataStore();
        }

        return dataStores;
    }

    @Override
    public final boolean shouldAppliedOn(DataStore dataStore) {
        return getDataStores().contains(dataStore.getNamedDataStore()) ;
    }

    private List<Class<? extends NamedDataStore>> getDefaultDataStore() {
        if( defaultDataStore==null ) {
            defaultDataStore = loadDefaultDataStore();
        }
        return Collections.singletonList(defaultDataStore);
    }

    private static Class<? extends NamedDataStore> loadDefaultDataStore() {
        return ClassLoader.loadClass(NamedDataStore.class, DataSetProperties.getDefaultDataStore());
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
