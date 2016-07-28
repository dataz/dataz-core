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
