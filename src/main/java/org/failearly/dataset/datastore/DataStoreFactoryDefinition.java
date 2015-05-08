/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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

import org.failearly.dataset.config.Constants;

import java.lang.annotation.*;

/**
 * DataStoreMetaDefinition is the meta annotation marking an annotation as DataStore annotations.
 * <br><br>
 * Any annotation using this meta annotation ...
 * <ul>
 *      <li>must have an id element returning a String: {@code String id()}.</li>
 *      <li>should have a configuration part: {@code String config()}.</li>
 * </ul>
 * <br><br>
 * Usage Example:<br>
 * <pre>
 *    {@literal @Target({ElementType.TYPE})}
 *    {@literal @Retention(RetentionPolicy.RUNTIME)}
 *    {@literal @DataStoreFactoryDefinition(dataStoreFactory = MyDataStoreFactory.class)}
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
 * @see org.failearly.dataset.datastore.DataStoreFactory
 * @see org.failearly.dataset.DataStoreDefinition
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface DataStoreFactoryDefinition {
    Class<? extends DataStoreFactory> dataStoreFactory();
}
