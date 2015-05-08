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
package org.failearly.dataset;

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.datastore.DataStoreFactoryDefinition;
import org.failearly.dataset.datastore.DataStoreType;
import org.failearly.dataset.datastore.DefaultDataStoreFactory;
import org.failearly.dataset.datastore.NullDataStoreType;

import java.lang.annotation.*;

/**
 * DataStoreDefinition must be used if you are using multiple {@link org.failearly.dataset.datastore.DataStore} either homogeneous or not.
 * For a simple setup (only one database used), just add {@code /datastore.properties} to your classpath.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(DataStoreDefinition.DataStoreDefinitions.class)
@DataStoreFactoryDefinition(dataStoreFactory = DefaultDataStoreFactory.class)
public @interface DataStoreDefinition {
    /**
     * If your tests uses multiple data stores, you must identify each data store.
     *
     * @return the (unique) data store id.
     */
    String id() default Constants.DATASET_DEFAULT_DATASTORE_ID;

    /**
     * The datastore configuration file will be used by the actually DataStore Implementation. So what's inside these configuration property file depends
     * on the DataStore type. If you use multiple DataStoreDefinitions each should have it's own configuration property file.
     * <br><br>
     * The default value is {@code "/datastore.properties"}.
     *
     * @return the datastore configuration file(name).
     */
    String config() default "/datastore.properties";

    /**
     * If you use multiple datastore types, you must explicit set the type. The default implementation uses the
     * property {@link Constants#DATASET_PROPERTY_DATASTORE_TYPE_CLASS_NAME}, which will be set in
     * {@link Constants#DATASET_DATASTORE_PROPERTY_FILE} of each module.
     *
     * @return the {@link org.failearly.dataset.datastore.DataStoreType} class.
     *
     * @see org.failearly.dataset.datastore.DefaultDataStoreFactory
     */
    Class<? extends DataStoreType> type() default NullDataStoreType.class;

    /**
     * For each datastore it's possible to define it's own default (setup) suffix. This suffix will be used to generate the setup resource name.
     * If not specified the value of {@link Constants#DATASET_PROPERTY_DEFAULT_SETUP_SUFFIX}
     * will be used.
     * <br><br>
     * Example:<br><br>
     * <pre>
     *      package com.company.project.module;
     *
     *      {@literal @DataStoreDefinition(.., setupSuffix="setup.sql.vm")}
     *      public class MyTest {
     *          // ...
     *
     *          // 1. DataSet will search for a setup resource "/com/company/project/module/MyName-anytest.setup.sql.vm" within your classpath.
     *          //
     *          // 2. DataSet will search for a cleanup resource "/com/company/project/module/MyName-anytest.cleanup" within your classpath, because
     *          // there is no {@link #cleanupSuffix()} specified!
     *          //
     *          {@literal @Test} {@literal @DataSet}
     *          public void anyTest() {
     *          }
     *      }
     * </pre>
     *
     * @return suffix to be used for {@link DataSet#setup()} (if no setup resource is specified).
     */
    String setupSuffix() default Constants.DATASET_USE_DEFAULT_SUFFIX;

    /**
     * Analog to {@link #setupSuffix()}. If not specified, the value of {@link Constants#DATASET_PROPERTY_DEFAULT_CLEANUP_SUFFIX}
     * will be used.
     * @return suffix to be used for {@link DataSet#cleanup()} (if no cleanup resource is specified).
     */
    String cleanupSuffix() default Constants.DATASET_USE_DEFAULT_SUFFIX;

    /**
     * Containing Annotation Type.
     *
     * Remark: This will be used by Java8 compiler.
     *
     * @see java.lang.annotation.Repeatable
     */
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface DataStoreDefinitions {
        DataStoreDefinition[] value();
    }
}