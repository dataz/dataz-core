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

import org.failearly.dataz.config.Constants;
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
     * @param namedDataStore the class object an DataStore impl is applied on.
     * @param dataStoreAnnotation a data store impl which represents a single {@link DataStore} instance.
     * @param dataStoreDefinition the datastore definition impl.
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
     * @param namedDataStore the class object an DataStore impl is applied on.
     * @param dataStoreAnnotation a data store impl which represents a single {@link DataStore} instance.
     * @return a new data store instance.
     *
     * @see Definition
     */
    default DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, T dataStoreAnnotation) {
        throw new UnsupportedOperationException("You must override one of the createDataStore methods.");
    }

    /**
     * Definition is the meta impl marking an impl as DataStore impl and providing a {@link DataStoreFactory}.
     * <br><br>
     * Any impl using this impl ...
     * <ul>
     *      <li>must have an id element returning a String: {@code String name()}.</li>
     *      <li>should have a configuration part: {@code String config()}.</li>
     * </ul>
     * <br><br>
     * Usage Example:<br>
     * <pre>
     * // ---- MyDataStore.java
     *   {@literal @Target({ElementType.TYPE})}
     *   {@literal @Retention(RetentionPolicy.RUNTIME)}
     *   {@literal @Repeatable}(MyDataStore.MyDataStores.class)
     *
     * // Three different ways to provide an appropriate {@link DataStoreFactory}:
     *    // <b>1.</b> The DataStoreFactory.Definition with own implementation.
     *   {@literal @}DataStoreFactory.Definition(factory = MyDataStoreFactory.class)
     *
     *    // <b>2.</b> with {@link ReflectionDataStoreFactory} and <i>default</i> factory method
     *   {@literal @}DataStoreFactory.Definition(datastore=MyDataStoreImplementation.class, dataStoreAnnotation=MyDataStore.class)
     *
     *    // <b>3.</b> with {@link ReflectionDataStoreFactory} and <i>custom</i> factory method
     *   {@literal @}DataStoreFactory.Definition(datastore=MyDataStoreImplementation.class, dataStoreAnnotation=MyDataStore.class, factoryMethod="create")
     *    public {@literal @}interface MyDataStore {
     *        // omitted for brevity
     *
     *        // Java8 stores repeating {@literal @MyDataStore} in the associated container impl.
     *        {@literal @interface} MyDataStores {
     *             MyDataStore[] value();
     *        }
     *   }
     *
     * // ---- MyDataStoreFactory.java (used only if using {@link #factory()})
     *   public final class MyDataStoreFactory implement {@link DataStoreFactory}{@literal <MyDataStore>} {
     *      DataStore createDataStore(Class{@literal <? extends NamedDataStore>} namedDataStore, MyDataStore dataStoreAnnotation) {
     *          return new MyDataStoreImplementation(namedDataStore, dataStoreAnnotation);
     *      }
     *   }
     *
     *
     * // ---- MyDataStoreImplementation.java
     *   public final MyDataStoreImplementation extends {@link DataStoreBase} {
     *       MyDataStoreImplementation(Class{@literal <? extends NamedDataStore>} namedDataStore, MyDataStore dataStoreAnnotation) {
     *           super(namedDataStore, dataStoreAnnotation);
     *       }
     *
     *       // using the <i>default</i> factory method
     *       <b>static</b> DataStore {@value Constants#DATAZ_DEFAULT_DATASTORE_FACTORY_METHOD}(Class{@literal <? extends NamedDataStore>} namedDataStore, MyDataStore dataStoreAnnotation) {
     *           return new MyDataStoreImplementation(namedDataStore, dataStoreAnnotation);
     *       }
     *
     *       // using <i>custom</i> factory method
     *       <b>static</b> DataStore create(Class{@literal <? extends NamedDataStore>} namedDataStore, MyDataStore dataStoreAnnotation) {
     *           return new MyDataStoreImplementation(namedDataStore, dataStoreAnnotation);
     *       }
     *
     *       // omitted for brevity
     *   }
     * </pre>
     *
     *
     * @see DataStoreFactory
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
         * {@value Constants#DATAZ_DEFAULT_DATASTORE_FACTORY_METHOD}.
         * <br><br>
         * Used by {@link ReflectionDataStoreFactory}.
         * @return the factory method's name of your {@link DataStore} implementation.
         */
        String factoryMethod() default DATAZ_DEFAULT_DATASTORE_FACTORY_METHOD;

        /**
         * The datastore impl which uses this meta impl. You must provide your DataStore impl,
         * if you use {@link ReflectionDataStoreFactory} for {@link #factory()}.
         * <br><br>
         * Used by {@link ReflectionDataStoreFactory}.
         * @return the target datastore impl.
         */
        Class<? extends Annotation> dataStoreAnnotation() default NoDataStoreAnnotation.class;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface NoDataStoreAnnotation {}

    final class NullDataStore extends AbstractDataStore {}

}
