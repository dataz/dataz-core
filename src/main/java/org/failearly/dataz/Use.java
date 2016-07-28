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
