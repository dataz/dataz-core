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

package org.failearly.dataz.internal.test.datastore;

import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.support.ReflectionDataStoreFactoryBase;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.test.datastore.AdhocDataStore;

/**
 * Factory for {@link AdhocDataStore}.
 * AdhocDataStoreFactory uses reflection for creating instances of {@link org.failearly.dataz.datastore.DataStore}.
 */
public final class AdhocDataStoreFactory extends ReflectionDataStoreFactoryBase<AdhocDataStore> {
    public AdhocDataStoreFactory() {
    }

    @Override
    public DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore dataStoreAnnotation) {
        return doCreateDataStore(namedDataStore, dataStoreAnnotation);
    }

    private DataStore doCreateDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
        return createDataStoreByReflection(              //
                    annotation.implementation(),         //
                    annotation.factoryName(),            //
                    AdhocDataStore.class,                //
                    namedDataStore,                      //
                    annotation                           //
            );
    }
}
