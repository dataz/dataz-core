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

/**
 * Contains meta annotations for assigning a {@link org.failearly.dataz.resource.DataResourcesFactory} to a specific dataSet annotation.
 * <br><br>
 * Usage Example:<br><br>
 * <pre>
 *     {@literal @}Target({ElementType.METHOD, ElementType.TYPE})
 *     {@literal @}Retention(RetentionPolicy.RUNTIME)
 *     {@literal @}Repeatable(DataSet.DataSets.class)
 *     {@literal @}<i>DataSetupResourceFactoryDefinition(factory = SetupResourcesFactory.class)</i>
 *     {@literal @}<i>DataCleanupResourceFactoryDefinition(factory = CleanupResourcesFactory.class)</i>
 *      public {@literal @}interface DataSet {
 *          String name() default Constants.DATASET_DEFAULT_NAME;
 *
 *          String datastore() default Constants.DATAZ_DEFAULT_DATASTORE_ID;
 *
 *  	    // The setup data resources
 *          String[] setup() default {};
 *
 *  	    // The cleanup data resources
 *          String[] cleanup() default {};
 *
 *          boolean transactional() default true;
 *
 *          boolean failOnError() default true;
 *
 *          {@literal @}Target({ElementType.METHOD, ElementType.TYPE})
 *          {@literal @}Retention(RetentionPolicy.RUNTIME)
 *          {@literal @}interface DataSets {
 *               DataSet[] value();
 *           }
 *      }
 * </pre>
 *
 * @see org.failearly.dataz.DataSet
 * @see org.failearly.dataz.DataSetup
 * @see org.failearly.dataz.DataCleanup
 */
package org.failearly.dataz.annotations;