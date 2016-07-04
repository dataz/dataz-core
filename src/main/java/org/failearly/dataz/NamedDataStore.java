/*
 * Copyright (c) 2009.
 *
 * Date: 16.05.16
 * 
 */
package org.failearly.dataz;

import org.failearly.dataz.config.Constants;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.test.datastore.AdhocDataStore;

/**
 * # NamedDataStore is an (abstract) marker class, which makes a class or interface as a {@linkplain DataStore DataStore Object}.
 *
 * Each class object represents at least one {@link DataStore} object. If you apply more then
 * one DataStore annotation (for example {@code SqlDataStore} or {@link AdhocDataStore}), for each annotation, DataZ will
 * create a {@link DataStore} instance - this could be useful for running tests multithreaded.
 *
 * **Remarks**:
 *
 * + DataZ will not create any objects from {@code NamedDataStore}.
 * + It's only purpose is to carry at least DataStore Annotations (like {@code SqlDataStore}) and
 * + provide {@linkplain DataSet#setup() Setup DataSets} and  {@linkplain DataSet#cleanup() Cleanup DataSets}.
 * + You can make a DataStore to the default {@code NamedDataStore} by setting the property '{@link Constants#DATAZ_PROPERTY_DEFAULT_DATA_STORE}'.
 *
 * Usage:
 *
 * ```java
 * // MasterDB.java
 *   {@literal @SqlDataStore}(name="1st", config="/h2-config.properties", properties=@Property(k = "num", v = "1"))
 *   {@literal @SqlDataStore}(name="2nd", config="/h2-config.properties", properties=@Property(k = "num", v = "2")}
 *   {@literal @DataSet}(setup="master-schema.sql")
 *   {@literal @DataSetup}("insert-reference-data.sql")
 *   {@literal @DataCleanup}("delete-reference-data.sql")
 *    public class MasterDB extends NamedDataStore {
 *        // There is currently no execution logic
 *    }
 * ```
 *
 * The property file `h2-config.properties`:
 * ```
 *    jdbc.driver = org.h2.Driver
 *    // ${num} will be replaced with 1 or 2
 *    jdbc.url = jdbc:h2:file:${user.dir}/temp/h2-test-${num};AUTO_SERVER=TRUE
 *    jdbc.user = SA
 *    jdbc.password = (empty)
 * ```
 *
 *
 * @see DataSet#datastores()
 * @see DataSetup#datastores()
 * @see DataCleanup#datastores()
 * @see AdhocDataStore
 * @see DataStore
 */
public abstract class NamedDataStore {
    /**
     * Currently DataZ works only with the class object of `NamedDataStore`.
     *
     * So to express this, the constructor will throw an exception.
     *
     * @throws UnsupportedOperationException must not be called
     */
    protected NamedDataStore() {
        throw new UnsupportedOperationException("Must not create any instance");
    }
}
