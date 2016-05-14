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

package org.failearly.dataz.datastore;

import org.failearly.dataz.resource.DataResourcesFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DataStoreSetupResourceFactoryDefinition is a (set up) meta annotation used for creating DataResource objects, by associating a
 * {@link DataResourcesFactory} to any DataStore setup annotation.
 *
 * @see org.failearly.common.annotation.traverser.AnnotationTraverserBuilder#metaAnnotationTraverser(Class)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface DataStoreSetupResourceFactoryDefinition {
    /**
     * @return the factory class
     */
    Class<? extends DataResourcesFactory> factory();
}