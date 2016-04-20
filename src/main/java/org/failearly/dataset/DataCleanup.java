/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */

package org.failearly.dataset;

import org.failearly.dataset.annotations.DataCleanupResourceFactoryDefinition;
import org.failearly.dataset.config.Constants;
import org.failearly.dataset.internal.resource.factory.DataCleanupResourcesFactory;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.template.TemplateObject;

import java.lang.annotation.*;

/**
 * DataCleanup defines data resource(s) (only) for setup. This annotation is applicable to test classes and methods.
 * <br><br>
 * Usage example:<br><br>
 * <pre>
 *     package com.company.module;
 *
 *     public class MyTestClass {
 *         {@literal @Test}
 *         {@literal @DataCleanup}
 *          public void testMethod() {
 *              // Uses
 *              // * {@link #datastore()} with id {@link Constants#DATASET_DEFAULT_DATASTORE_ID}. This means that any setup/cleanup resource will be applied
 *              //     on this data store.
 *              // * as the {@link #name} (not ID!!) {@link Constants#DATASET_DEFAULT_NAME}
 *              // * ... for {@link #value()} resource in classpath /com/company/module/MyTestClass-testMethod.setup  (<b>mandatory</b>)
 *              // * {@link #transactional()} and {@link #failOnError()} defaults to true
 *          }
 *      }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(DataCleanup.DataCleanups.class)
@DataCleanupResourceFactoryDefinition(factory = DataCleanupResourcesFactory.class)
public @interface DataCleanup {

    /**
     * The DataSet's name. Multiple DataSets with the same name will be executed.
     * The name becomes necessary if you are using {@link TemplateObject}.
     * <p>
     * <br><br>
     * <p>
     * Remark: In case you don't use any generator, it's recommended to use the default name.
     *
     * @return The DataSet's name.
     */
    String name() default Constants.DATASET_DEFAULT_NAME;

    /**
     * The associated DataStore. If the DataStore does not exists, the DataSet resources will be ignored.
     *
     * @return the ID of an associated DataStore.
     *
     * @see AdhocDataStore#id()
     * @see org.failearly.dataset.datastore.DataStore#getId()
     *
     * @deprecated Will replaced in Release 0.6 (with new element datastores)
     */
    @Deprecated
    String datastore() default Constants.DATASET_DEFAULT_DATASTORE_ID;

    /**
     * The name(s) of the setup resource(s).<br>
     * <b>Caution</b>: There must be at least one setup resource. If dataSet does not found any setup resource within classpath an exception will be thrown.
     * <br><br>
     * dataSet uses this evaluation procedure:
     * <dl>
     * <dt>Absolute path: at least one resource name is set and starts with {@code /}</dt>
     * <dd>dataSet loads each resource by using ({@link Class#getResourceAsStream(String)}).</dd>
     * <dt>Relative path: at least one resource name is set and starts <b>not</b> with {@code /}</dt>
     * <dd>dataSet loads each resource by using ({@link Class#getResourceAsStream(String)}) and uses the path of declaring test class.</dd>
     * <dt>No setup resource</dt>
     * <dd>
     * then dataSet will search for a resource with following convention:
     * <ul>
     * <li>{@literal @DataSet} applied on <em>test class</em>, then dataSet will look for a (single) setup resource file relative to declaring
     * class, with pattern {@code <class-name>.<datastore-setup-suffix>} (i.e. {@code MyTestClass.setup}).
     * </li>
     * <li>@literal @DataSet} applied on <em>test method</em>, then dataSet will look for a (single) setup resource file relative to declaring
     * class, with pattern {@code <class-name>-<test-method-name>.<datastore-setup-suffix>} (i.e. {@code MyTestClass-testMethod.setup}).
     * </li>
     * </ul>
     * </dd>
     * </dl>
     * <br><br>
     *
     * @return The name(s) of the setup resource(s) or empty.
     * @see org.failearly.dataset.datastore.DataStore#getSetupSuffix()
     * @see AdhocDataStore#setupSuffix()
     * @see Constants#DATASET_PROPERTY_DEFAULT_SETUP_SUFFIX
     */
    String[] value() default {};

    /**
     * Controls the transactional behaviour of {@link org.failearly.dataset.datastore.DataStore}.
     * <br><br>
     * <ul>
     * <li><b>default {@code true}</b>: run a single setup/cleanup resource file within a transaction.</li>
     * <li>{@code false}: No transaction, each statement will be executed within a new transaction.</li>
     * </ul>
     *
     * @return {@code true} or {@code false}.
     * @see DataResource#isTransactional()
     * @see #value()
     */
    boolean transactional() default true;

    /**
     * (Optional) Set to {@code false} if you want to ignore any error while applying the data set resource.
     *
     * @return {@code true} (default value) if the dataSet should fail otherwise {@code false}.
     * @see DataResource#isFailOnError()
     */
    boolean failOnError() default true;

    /**
     * Containing Annotation Type.
     * <p>
     * Remark: This will be used by Java8 compiler.
     *
     * @see Repeatable
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface DataCleanups {
        DataCleanup[] value();
    }
}

