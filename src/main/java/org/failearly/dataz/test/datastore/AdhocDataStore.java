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

package org.failearly.dataz.test.datastore;

import org.failearly.dataz.common.Property;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.datastore.AbstractDataStore;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStoreBase;
import org.failearly.dataz.datastore.DataStoreFactory;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.internal.test.datastore.AdhocDataStoreFactory;

import java.lang.annotation.*;

/**
 * AdhocDataStore could be used for fast and easy without the need to define a specific
 * DataStore impl. The only thing you have to provide is a implementation of {@link DataStore} and a static factory
 * method (see {@link #factoryName()}).
 * Example:<br><br>
 * <pre>
 *   {@code @AdhocDataStore(implementation=MyDataStore.class)}
 *   {@code @AdhocDataStore(implementation=MyDataStore.class, factoryName="createMyDataStore")}
 *    class MyNameDataStore extends {@link NamedDataStore} {}
 *
 *    class MyDataStore extends {@link DataStoreBase} {
 *        // the default {@link #factoryName()}
 *        static DataStore createDataStore({@code Class<? extends NamedDataStore>} namedDataStore, AdhocDataStore impl) {
 *              return new MyDataStore(namedDataStore, impl);
 *        }
 *        // custom {@link #factoryName()}
 *        static DataStore createMyDataStore({@code Class<? extends NamedDataStore>} namedDataStore, AdhocDataStore impl) {
 *              return new MyDataStore(namedDataStore, impl);
 *        }
 *
 *        // .... rest ommitted for brevity
 *    }
 * </pre>
 * <br><br>
 * <b>Remark</b>: Usually you should use real DataStore annotations provided by DataStore implementations.
 * <br><br>
 * Currently known {@link DataStore} annotations are:
 * <br><br>
 * <ul>
 * <li>{@literal @SqlDataStore} from datastore-sql module</li>
 * <li>{@literal @Neo4JDataStore} from datastore-neo4j module</li>
 * <li>more to come</li>
 * </ul>
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(AdhocDataStore.AdhocDataStores.class)
@DataStoreFactory.Definition(factory = AdhocDataStoreFactory.class)
public @interface AdhocDataStore {
    /**
     * If your tests uses multiple datastores per {@link NamedDataStore}, you must identify each data store impl.
     * <br><br>
     * Remark: {@link DataStore#getId()} uses usually {@link #name()},
     * the {@link NamedDataStore} and the actually {@link DataStore} implementation (see {@link #implementation()}.
     *
     * @return the (unique) data store name.
     *
     * @see DataStore#getId()
     */
    String name() default Constants.DATAZ_DEFAULT_DATASTORE_NAME;

    /**
     * The datastore configuration file will be used by the actually DataStore Implementation. So what's inside these configuration property file depends
     * on the DataStore type. If you use multiple DataStoreDefinitions each should have it's own configuration property file.
     * <br><br>
     * The default value is "{@value Constants#DATAZ_NO_CONFIG_FILE}".
     *
     * @return the datastore configuration file(name).
     */
    String config() default Constants.DATAZ_NO_CONFIG_FILE;

    /**
     * Please provide an implementation of {@link DataStore},
     * which should provide a static factory method ({@link #factoryName()}.
     *
     * @return the {@link DataStore} class
     */
    Class<? extends DataStore> implementation() default NullDataStore.class;

    /**
     * The factory method name of the factory method. The default name is {@value #DEFAULT_FACTORY_METHOD}.
     *
     * Caution: The signature must be {@link DataStoreFactory#createDataStore(Class, Annotation)}.
     *
     * @return the name of the (static) factory method.
     *
     * @see DataStoreFactory#createDataStore(Class, Annotation)
     * @see AdhocDataStoreFactory
     */
    String factoryName() default DEFAULT_FACTORY_METHOD;

    /**
     * Optional properties or named arguments (key value pairs).
     * @return an array of {@link Property}.
     */
    Property[] properties() default {};


    /**
     * Containing Annotation Type.
     * <p>
     * Remark: This will be used by Java8 compiler.
     *
     * @see java.lang.annotation.Repeatable
     */
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface AdhocDataStores {
        AdhocDataStore[] value();
    }

    final class NullDataStore extends AbstractDataStore {

        private static final NullDataStore NULL_DATA_STORE = new NullDataStore();

        private NullDataStore() {}

        @SuppressWarnings("unused")
        private static NullDataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
            return NULL_DATA_STORE;
        }
    }

    String DEFAULT_FACTORY_METHOD="createDataStore";
}