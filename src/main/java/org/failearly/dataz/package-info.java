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

/**
 * Contains basic annotations for defining DataSet based tests.
 *
 * <dl>
 *     <dt>{@link org.failearly.dataz.DataSet}</dt>
 *     <dd>Mark classes and/or test method as DataSet based test. At least <em>one</em> {@literal @DataSet}
 *          must be assigned on your test class, any of super class of your test class or test method, otherwise
 *         {@code org.failearly.dataz.junit4.DataSetDriver} ignores the test.</dd>
 *    <dt>{@link org.failearly.dataz.NoDataSet}</dt>
 *    <dd>If you mark the entire test class with {@link org.failearly.dataz.DataSet}, you can
 *         force {@code org.failearly.dataz.junit4.DataSetDriver} to ignore the test method. The test will be executed, but
 *         not as DataSet based test</dd>
 *    <dt>{@link org.failearly.dataz.SuppressCleanup}</dt>
 *    <dd>Usually cleanup resources will be applied after the test has been executed (either successful or failed).
 *     But if test fails it's necessary to suppress this behaviour, for debugging the test.
 *     For these cases DataSet provides {@link org.failearly.dataz.SuppressCleanup}</dd>
 * </dl>
 * <br><br>
 * Simple example:<br><br>
 * <pre>
 *    package com.company.project.module;
 *
 *    {@literal @}{@link org.failearly.dataz.DataSet}
 *    public class MyTest {
 *
 *      // <b>Mandatory</b>! Otherwise {@literal @DataSet} has no effect.
 *      {@literal @}{@link org.junit.Rule}
 *      private final {@code org.junit.rules.TestRule} dataSetDriver = {@code org.failearly.dataz.junit4.DataSetDriver}.createDataSetDriver(this);
 *
 *      {@literal @Test}
 *      public void anyTest() {
 *          // ...
 *      }
 *      {@literal @Test}
 *      public void secondTest() {
 *          // ...
 *      }
 *      {@literal @Test}
 *      {@literal @}{@link org.failearly.dataz.NoDataSet}
 *      public void noDataSetTest() {
 *          // ...
 *      }
 *    }
 * </pre>
 *
 * This setup will search for
 * <br><br>
 * <ol>
 *    <li>a {@link org.failearly.dataz.datastore.DataStore} configuration file (/datastore.config) in your classpath.
 *          (see {@link org.failearly.dataz.test.datastore.AdhocDataStore#config()}). This file should contain all necessary configuration for your
 *          chosen {@code DataStore} implementation.</li>
 *    <li>a (mandatory) setup file named {@code "/com/company/project/module/MyClass.setup"} in your classpath. If omitted the test will fail.</li>
 *    <li>a (optional) cleanup file named {@code "/com/company/project/module/MyClass.cleanup"} in your classpath.</li>
 * </ol>
 *
 * In this simple example, the mentioned setup file will be applied on your configured {@code DataStore} for each test (method) and if the
 * cleanup file exists, this will applied after each test. The 3rd test method {@code noDataSetTest} will not be handled as dataSet test. So
 * the resources will be applied only two times.
 * <br><br>
 * The same can be expressed by extending {@code AbstractDataSetTest}. Example:<br><br>
 * <pre>
 *    package com.company.project.module;
 *
 *    {@literal @}{@link org.failearly.dataz.DataSet}
 *    public class MyTest extends {@code org.failearly.dataz.junit4.AbstractDataSetTest} {
 *      // Some dataSet tests, like above.
 *      // Omitted for brevity ...
 *    }
 * </pre>
 *
 * @see org.failearly.dataz.DataSet
 * @see org.failearly.dataz.DataSetup
 * @see org.failearly.dataz.DataCleanup
 * @see org.failearly.dataz.test.datastore.AdhocDataStore
 */
package org.failearly.dataz;