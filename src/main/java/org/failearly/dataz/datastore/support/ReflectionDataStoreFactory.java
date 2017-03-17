/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
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
