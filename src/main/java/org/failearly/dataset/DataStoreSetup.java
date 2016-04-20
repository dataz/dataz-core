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

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.template.TemplateObject;

import java.lang.annotation.*;

/**
 * Sometimes a {@link org.failearly.dataset.datastore.DataStore} needs some initial settings before any test could access the datastore and cleaning after all
 * tests (of an entire test run).
 * <br><br>
 * The resources of {@code DataStoreSetup} will be <em>applied only once</em> per {@link org.failearly.dataset.datastore.DataStore#getId()}.
 * <br><br>
 * Similar to a {@link org.failearly.dataset.DataSet}, but should be used for DataStore setup or cleanup, like creating schemes or
 * master data, which won't be manipulated at all.
 *
 * @see org.failearly.dataset.datastore.DataStore#setupDataStore(java.util.List, org.failearly.dataset.internal.template.TemplateObjects)
 * @see org.failearly.dataset.datastore.DataStore#cleanupDataStore()
 *
 * @deprecated Will replaced in Release 0.6
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(DataStoreSetup.DataStoreSetups.class)
@Deprecated
public @interface DataStoreSetup {
    /**
     * The initializer's name. If omitted, use {@link #datastore()}.
     * <p>
     * The name becomes necessary if you are using {@link TemplateObject}.
     *
     * @return The initializer's name or {@link #datastore()}
     */
    String name() default "";

    /**
     * The associated DataStore. If the DataStore does not exists, the Initial DataSet resources will be ignored.
     *
     * @return the ID of an associated DataStore.
     * @see AdhocDataStore#id()
     * @see org.failearly.dataset.datastore.DataStore#getId()
     */
    String datastore() default Constants.DATASET_DEFAULT_DATASTORE_ID;

    /**
     * The name(s) of the initial setup resource(s). There must be at least one setup resource.
     *
     * @return The name(s) of the setup resource(s).
     */
    String[] setup();

    /**
     * The name(s) of the (optional) cleanup resource(s).
     *
     * @return The name(s) of the cleanup resource(s).
     */
    String[] cleanup() default {};

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
     * @see #setup()
     * @see #cleanup()
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
     * @see java.lang.annotation.Repeatable
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface DataStoreSetups {
        DataStoreSetup[] value();
    }
}