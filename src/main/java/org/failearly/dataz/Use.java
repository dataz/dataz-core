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

import org.failearly.dataz.internal.resource.factory.use.UseCleanupResourcesFactory;
import org.failearly.dataz.internal.resource.factory.use.UseSetupResourcesFactory;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;

import java.lang.annotation.*;

/**
 * With {@literal @}Use is it possible to predefine {@link DataResource}s for reuse. Example:<br><br>
 * <pre>
 *    public class UserServiceTest {
 *     {@literal @Test}
 *     {@literal @Use}({UserMarko.class,UserFrank.class,OtherUsers.class})
 *      public void lookup_for_admin_users__should_find_user_marko_and_frank() {
 *          // ...
 *      }
 *    }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataResourcesFactory.SetupDefinition(value = UseSetupResourcesFactory.class)
@DataResourcesFactory.CleanupDefinition(value = UseCleanupResourcesFactory.class)
public @interface Use {
    /**
     * (Optional) Overwrites the data store(s) settings of {@link ReusableDataSet}.
     *
     * If ommitted the data store(s) of the {@link ReusableDataSet} will be used.
     *
     * @return the associated data stores
     * @see DataResource#getNamedDataStore()
     */
    Class<? extends NamedDataStore>[] datastores() default {};


    /**
     * Define which datasets should be reused.
     *
     * @return Your reusable datasets(s)
     */
    Class<? extends ReusableDataSet>[] value();

    /**
     * ReusableDataSet is a marker interface for {@link Use}. It provides no functionality.
     */
    interface ReusableDataSet {/* no methods */ }
}
