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
import org.failearly.dataz.datastore.DataStoreFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * ReflectionDataStoreFactoryBase is responsible for ...
 */
public abstract class ReflectionDataStoreFactoryBase<T extends Annotation> implements DataStoreFactory<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionDataStoreFactory.class);

    protected static DataStore createDataStoreByReflection(
            Class<? extends DataStore> dataStoreClass,
            String factoryName,
            Class<? extends Annotation> dataStoreAnnotationClass,
            Class<? extends NamedDataStore> namedDataStore,
            Annotation dataStoreAnnotation) {
        try {
            final Method factoryMethod=dataStoreClass.getDeclaredMethod(factoryName, Class.class, dataStoreAnnotationClass);
            factoryMethod.setAccessible(true);
            return (DataStore) factoryMethod.invoke(null, namedDataStore, dataStoreAnnotation);
        } catch (NoSuchMethodException ex) {
            LOGGER.error("{}: Missing method factory method '{}' or wrong signature!", dataStoreClass.getSimpleName(), factoryName);
            throw new IllegalArgumentException(
                    dataStoreClass.getSimpleName() + ": Missing method factory method '" + factoryName + "' or wrong signature!",
                    ex
            );
        } catch (Exception ex) {
            LOGGER.error("Caught unexpected exception while invoking {} on {}", factoryName, dataStoreClass.getSimpleName());
            throw new RuntimeException(
                    "Caught exception, while invoking " + factoryName + " on " + dataStoreClass.getSimpleName(),
                    ex
            );
        }
    }
}
