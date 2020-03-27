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

package org.failearly.dataz;

import org.failearly.dataz.config.Constants;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.internal.resource.factory.dataset.DataSetCleanupResourcesFactory;
import org.failearly.dataz.internal.resource.factory.dataset.DataSetSetupResourcesFactory;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;
import org.failearly.dataz.template.TemplateObject;

import java.lang.annotation.*;

/**
 * DataSet defines the data resource for setup and (optional) cleanup. This impl is applicable to test classes and methods.
 * <br><br>
 * Usage example:<br><br>
 * <pre>
 *     package com.company.module;
 *
 *     public class MyTestClass {
 *         {@literal @Test}
 *         {@literal @DataSet}
 *          public void testMethod() {
 *              // Your data base test
 *          }
 *      }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(DataSet.List.class)
@DataResourcesFactory.SetupDefinition(DataSetSetupResourcesFactory.class)
@DataResourcesFactory.CleanupDefinition(DataSetCleanupResourcesFactory.class)
public @interface DataSet {

    /**
     * The DataSet's name. Multiple DataSets with the same name will be executed.
     * The name is necessary if you are using {@link TemplateObject}s.
     * <p>
     * <br><br>
     * <p>
     * Remark: In case you don't use any generator, it's recommended to use the default name.
     *
     * @return The DataSet's name.
     */
    String name() default Constants.DATASET_DEFAULT_NAME;

    /**
     * The data store(s) the data set resource will be applied on. If ommitted the default data store
     * will be used.
     *
     * @return the associated data stores
     *
     * @see DataResource#getNamedDataStore()
     * @see DataSetProperties#getDefaultNamedDataStore()
     */
    Class<? extends NamedDataStore>[] datastores() default {};

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
     *
     * @see Constants#DATAZ_PROPERTY_DEFAULT_SETUP_SUFFIX
     */
    String[] setup() default {};

    /**
     * The name(s) of the cleanup resource(s).<br>
     * <b>Remark</b>: The cleanup resource is optional. So there will be not error at all, if the resource does not exists.
     * <br><br>
     * dataSet uses an evaluation procedure:
     * <dl>
     * <dt>Absolute path: at least one resource name is set and starts with {@code /}</dt>
     * <dd>dataSet loads each resource by using ({@link Class#getResourceAsStream(String)}).</dd>
     * <dt>Relative path: at least one resource name is set and starts <b>not</b> with {@code /}</dt>
     * <dd>dataSet loads each resource by using ({@link Class#getResourceAsStream(String)}) and uses the path of declaring test class.</dd>
     * <dt>No cleanup resource</dt>
     * <dd>
     * Then dataSet will search for a resource with following convention:
     * <ul>
     * <li>{@literal @DataSet} applied on <em>test class</em>, then dataSet will look for a (single) cleanup resource file relative to declaring
     * class, with pattern {@code <class-name>.<datastore-cleanup-suffix>} (i.e. {@code MyTestClass.cleanup}).
     * </li>
     * <li>@literal @DataSet} is applied on <em>test method</em>, then dataSet will look for a (single) cleanup resource file relative to declaring
     * class, with pattern {@code <class-name>-<test-method-name>.<datastore-cleanup-suffix>}.
     * </li>
     * </ul>
     * </dd>
     * </dl>
     * <br><br>
     *
     * @return The name(s) of the cleanup resource(s) or empty.
     *
     * @see Constants#DATAZ_PROPERTY_DEFAULT_CLEANUP_SUFFIX
     */
    String[] cleanup() default {};

    /**
     * (Optional) Controls the transactional behaviour of {@link org.failearly.dataz.datastore.DataStore}.
     *
     * + **default {@code true}** : run a single setup/cleanup resource file within a single transaction.
     * + {@code false}: each statement within the resource will be stored in its own transaction.
     *
     * @return {@code true} or {@code false}.
     *
     * @see DataResource#isTransactional()
     * @see #setup()
     * @see #cleanup()
     * @see Constants#DATASET_DEFAULT_TRANSACTIONAL_VALUE
     */
    boolean transactional() default Constants.DATASET_DEFAULT_TRANSACTIONAL_VALUE;

    /**
     * (Optional) Set to {@code false} if you want to ignore any error while applying the data set resource.
     *
     * This is useful, if you have a datastore with schema and to apply only the changes.
     *
     * @return {@code true} (default value) if the dataSet should fail otherwise {@code false}.
     *
     * @see DataResource#isFailOnError()
     * @see Constants#DATASET_DEFAULT_FAIL_ON_ERROR_VALUE
     */
    boolean failOnError() default Constants.DATASET_DEFAULT_FAIL_ON_ERROR_VALUE;

    /**
     * Containing Annotation Type.
     * <p>
     * Remark: This will be used by Java8 compiler.
     *
     * @see java.lang.annotation.Repeatable
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        DataSet[] value();
    }
}

