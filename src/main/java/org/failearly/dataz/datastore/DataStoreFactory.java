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
package org.failearly.dataz.datastore;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.support.ReflectionDataStoreFactory;
import org.failearly.dataz.test.datastore.AdhocDataStore;

import java.lang.annotation.*;

import static org.failearly.dataz.config.Constants.DATAZ_DEFAULT_DATASTORE_FACTORY_METHOD;

/**
 * DataStoreFactory is the abstract factory for creating {@link org.failearly.dataz.datastore.DataStore}.
 * You must either implement {@link #createDataStore(Class, Annotation)} or {@link #createDataStore(Class, Annotation,Definition)}.
 */
@SuppressWarnings("WeakerAccess")
public interface DataStoreFactory<T extends Annotation> {

    /**
     * Create an instance of {@link org.failearly.dataz.datastore.DataStore} based on a data store impl.
     *
     * @param namedDataStore the class object an DataStore annotation is applied on.
     * @param dataStoreAnnotation a DataStore annotation which represents a single {@link DataStore} instance.
     * @param dataStoreDefinition the datastore annotation definition.
     * @return a new data store instance.
     *
     * @see Definition
     */
    default DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, T dataStoreAnnotation, Definition dataStoreDefinition) {
        return createDataStore(namedDataStore, dataStoreAnnotation);
    }

    /**
     * Create an instance of {@link org.failearly.dataz.datastore.DataStore} based on a data store impl.
     *
     * @param namedDataStore the class object an DataStore annotation is applied on.
     * @param dataStoreAnnotation a data store impl which represents a single {@link DataStore} instance.
     * @return a new data store instance.
     *
     * @see Definition
     */
    default DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, T dataStoreAnnotation) {
        throw new UnsupportedOperationException("You must override one of the createDataStore methods.");
    }

    /**
     * Definition is the meta annotation marking an annotation as DataStore implementation and providing a {@link DataStoreFactory}.
     * <br><br>
     * Any DataStore annotation using this annotation ...
     * <ul>
     *      <li>must have an id element returning a String: {@code String name()}.</li>
     *      <li>should have a configuration part: {@code String config()}.</li>
     * </ul>
     * <br><br>
     * Usage Example could be found in dataZ's datastore sub projects: {@code SqlDataStore} or {@code Neo4jDataStore}<br>
     *
     * @see AdhocDataStore
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface Definition {
        /**
         * @return a {@link DataStoreFactory} class. The default is {@link ReflectionDataStoreFactory}.
         */
        Class<? extends DataStoreFactory> factory() default ReflectionDataStoreFactory.class;

        /**
         * Your {@link DataStore} implementation. You must provide a {@link DataStore}, if you use {@link ReflectionDataStoreFactory} for
         * {@link #factory()}.
         * <br><br>
         * Used by {@link ReflectionDataStoreFactory}.
         * @return your {@link DataStore} implementation
         */
        Class<? extends DataStore> dataStore() default NullDataStore.class;

        /**
         * The (static) factory method used for creating your {@link DataStore} implementation. The default name is
         * {@code createDataStore}.
         * <br><br>
         * Used by {@link ReflectionDataStoreFactory}.
         * @return the factory method's name of your {@link DataStore} implementation.
         */
        String factoryMethod() default DATAZ_DEFAULT_DATASTORE_FACTORY_METHOD;

        /**
         * The datastore annotation which uses this meta annotation. You must provide your DataStore annotation,
         * if you use {@link ReflectionDataStoreFactory} for {@link #factory()}.
         * <br><br>
         * Used by {@link ReflectionDataStoreFactory}.
         * @return the target datastore annotation.
         */
        Class<? extends Annotation> dataStoreAnnotation() default NoDataStoreAnnotation.class;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface NoDataStoreAnnotation {}

    final class NullDataStore extends AbstractDataStore {}

}
