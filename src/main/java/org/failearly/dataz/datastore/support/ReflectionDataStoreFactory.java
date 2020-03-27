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
package org.failearly.dataz.datastore.support;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStoreFactory;

import java.lang.annotation.Annotation;

/**
 * ReflectionDataStoreFactory creates a {@link DataStore} by using reflection.
 *
 * @see DataStoreFactory
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
