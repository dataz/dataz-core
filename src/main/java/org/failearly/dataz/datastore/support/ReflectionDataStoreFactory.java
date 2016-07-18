/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com/contact)
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
package org.failearly.dataz.datastore.support;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStore;

import java.lang.annotation.Annotation;

/**
 * ReflectionDataStoreFactory creates a {@link DataStore} by using the {@link Definition}
 * annotation elements:<br><br>
 * <ul>
 *    <li>{@link Definition#dataStore()}</li>
 *    <li>{@link Definition#factoryMethod()}</li>
 *    <li>{@link Definition#dataStoreAnnotation()}</li>
 * </ul>
 */
public class ReflectionDataStoreFactory<T extends Annotation> extends ReflectionDataStoreFactoryBase<T> {
    @Override
    public DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, T dataStoreAnnotation, Definition dataStoreDefinition) {
        return doCreateDataStore(namedDataStore, dataStoreAnnotation, dataStoreDefinition);
    }

    private DataStore doCreateDataStore(Class<? extends NamedDataStore> namedDataStore, T annotation, Definition dataStoreDefinition) {
        return createDataStoreByReflection(                       //
                    dataStoreDefinition.dataStore(),              //
                    dataStoreDefinition.factoryMethod(),          //
                    dataStoreDefinition.dataStoreAnnotation(),    //
                    namedDataStore,                               //
                    annotation                                    //
            );
    }
}
