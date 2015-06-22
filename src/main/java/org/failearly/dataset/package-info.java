/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

/**
 * Contains basic annotations for defining DataSet based tests.
 *
 * <dl>
 *     <dt>{@link org.failearly.dataset.DataSet}</dt>
 *     <dd>Mark classes and/or test method as DataSet based test. At least <em>one</em> {@literal @DataSet}
 *          must be assigned on your test class, any of super class of your test class or test method, otherwise
 *         {@link org.failearly.dataset.junit4.DataSetDriver} ignores the test.</dd>
 *    <dt>{@link org.failearly.dataset.DataStoreSetup}</dt>
 *    <dd>Quiet similar to {@link org.failearly.dataset.DataSet}, but resources configured with {@code InitialDataSet} will be
 *    applied once per associated {@link org.failearly.dataset.datastore.DataStore}. Useful for scheme setups.</dd>
 *    <dt>{@link org.failearly.dataset.SuppressDataSet}</dt>
 *    <dd>If you mark the entire test class with {@link org.failearly.dataset.DataSet}, you can
 *         force {@link org.failearly.dataset.junit4.DataSetDriver} to ignore the test method. The test will be executed, but
 *         not as DataSet based test</dd>
 *    <dt>{@link org.failearly.dataset.SuppressCleanup}</dt>
 *    <dd>Usually cleanup resources will be applied after the test has been executed (either successful or failed).
 *     But if test fails it's necessary to suppress this behaviour, for debugging the test.
 *     For these cases DataSet provides {@link org.failearly.dataset.SuppressCleanup}</dd>
 * </dl>
 * <br><br>
 * Simple example:<br><br>
 * <pre>
 *    package com.company.project.module;
 *
 *    {@literal @}{@link org.failearly.dataset.DataSet}
 *    public class MyTest {
 *
 *      // <b>Mandatory</b>! Otherwise {@literal @DataSet} has no effect.
 *      {@literal @}{@link org.junit.Rule}
 *      private final {@link org.junit.rules.TestRule} dataSetDriver = {@link org.failearly.dataset.junit4.DataSetDriver}.createDataSetDriver(this);
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
 *      {@literal @}{@link org.failearly.dataset.SuppressDataSet}
 *      public void noDataSetTest() {
 *          // ...
 *      }
 *    }
 * </pre>
 *
 * This setup will search for
 * <br><br>
 * <ol>
 *    <li>a {@link org.failearly.dataset.datastore.DataStore} configuration file (/datastore.config) in your classpath.
 *          (see {@link org.failearly.dataset.AdhocDataStore#config()}). This file should contain all necessary configuration for your
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
 *    {@literal @}{@link org.failearly.dataset.DataSet}
 *    public class MyTest extends {@link org.failearly.dataset.junit4.AbstractDataSetTest} {
 *      // Some dataSet tests, like above.
 *      // Omitted for brevity ...
 *    }
 * </pre>
 *
 * @see org.failearly.dataset.junit4.DataSetDriver
 * @see org.failearly.dataset.DataSet
 * @see org.failearly.dataset.AdhocDataStore
 */
package org.failearly.dataset;