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

package org.failearly.dataset.datastore;

import org.failearly.dataset.AdhocDataStore;
import org.failearly.dataset.config.Constants;

import java.lang.annotation.*;

/**
 * DataStoreFactory is the abstract factory for creating {@link org.failearly.dataset.datastore.DataStore}.
 */
@SuppressWarnings("WeakerAccess")
public interface DataStoreFactory<T extends Annotation> {
    /**
     * Create an instance of {@link org.failearly.dataset.datastore.DataStore} based on a data store annotation.
     *
     * @param annotation a data store annotation with the meta annotation {@link Definition}.
     * @param context a (optional) context object used for creating.
     *
     * @return a new data store instance.
     *
     * @see Definition
     * @see AdhocDataStore
     */
    DataStore createDataStore(T annotation, Object context);

    /**
     * Definition is the meta annotation marking an annotation as DataStore annotation and providing the {@link DataStoreFactory}.
     * <br><br>
     * Any annotation using this annotation ...
     * <ul>
     *      <li>must have an id element returning a String: {@code String id()}.</li>
     *      <li>should have a configuration part: {@code String config()}.</li>
     * </ul>
     * <br><br>
     * Usage Example:<br>
     * <pre>
     *    {@literal @Target({ElementType.TYPE})}
     *    {@literal @Retention(RetentionPolicy.RUNTIME)}
     *    {@literal @DataStoreFactory.Definition(factory = MyDataStoreFactory.class)}
     *    {@literal @}{@link java.lang.annotation.Repeatable}(MyDataStore.MyDataStores.class)
     *    public {@literal @}interface MyDataStore {
     *        String id() default {@link Constants#DATASET_DEFAULT_DATASTORE_ID};
     *
     *        String config() default "/my-datastore-default.properties";
     *
     *        // more elements omitted for brevity
     *        // ...
     *
     *        // Java8 stores repeating {@literal @MyDataStore} in the associated container annotation.
     *        {@literal @interface} MyDataStores {
     *             MyDataStore[] value();
     *        }
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
        Class<? extends DataStoreFactory> value();
    }
}
