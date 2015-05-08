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
package org.failearly.dataset.annotations;

import org.failearly.dataset.internal.annotations.DataSetMarkerAnnotation;
import org.failearly.dataset.resource.DataResourcesFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DataSetupResourceFactoryDefinition is a (set up) meta annotation used for creating DataResource objects, by associating a
 * {@link DataResourcesFactory} to any DataSet annotation.
 *
 * @see org.failearly.dataset.internal.annotation.AnnotationTraversers
 * @see org.failearly.dataset.internal.annotation.AnnotationTraverser
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@DataSetMarkerAnnotation
public @interface DataSetupResourceFactoryDefinition {
    /**
     * @return the factory class
     */
    Class<? extends DataResourcesFactory> factory();
}

