/*
 * Copyright (c) 2009.
 *
 * Date: 25.05.16
 * 
 */
package org.failearly.dataz.datastore;

import org.failearly.dataz.NamedDataStore;

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
