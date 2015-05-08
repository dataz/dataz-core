/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.datastore;

import org.failearly.dataset.DataStoreDefinition;
import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DefaultDataStoreFactory uses default datastore type property for creating instances of {@link org.failearly.dataset.datastore.DataStore}.
 */
public final class DefaultDataStoreFactory implements DataStoreFactory<DataStoreDefinition> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataStoreFactory.class);

    private final DataStoreType dataStoreType;

    public DefaultDataStoreFactory() {
        this.dataStoreType = ClassUtils.createInstance(DataStoreType.class, DataSetProperties.getDefaultDataStoreTypeClassName());
    }

    @Override
    public DataStore createDataStore(DataStoreDefinition annotation, Object context) {
        if( annotation.type().equals(NullDataStoreType.class) )
            return dataStoreType.createDataStore(annotation, null);

        return createDataStoreType(annotation).createDataStore(annotation, null);
    }

    private static DataStoreType createDataStoreType(DataStoreDefinition generatorDefinition) {
        final Class<? extends DataStoreType> dataStoreType = generatorDefinition.type();
        try {
            return dataStoreType.newInstance();
        } catch (Exception ex) {
            LOGGER.error("Creating data store type " + dataStoreType + " causes exception: ", ex);
            throw new IllegalArgumentException("DataStoreDefinition has an invalid DataStoreType implementation " + dataStoreType.getName(), ex);
        }

    }
}