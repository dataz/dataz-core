/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 marko (http://fail-early.com/contact)
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
package org.failearly.dataset.internal.annotations;


import org.failearly.dataset.annotations.DataCleanupResourceFactoryDefinition;
import org.failearly.dataset.annotations.DataSetupResourceFactoryDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DataSetMarkerAnnotation is a marker (meta) annotation, for simplifying the check for relevant test methods.
 *
 * @see org.failearly.dataset.internal.model.TestClass
 * @see org.failearly.dataset.internal.model.TestMethod
 *
 * @see DataSetupResourceFactoryDefinition
 * @see DataCleanupResourceFactoryDefinition
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface DataSetMarkerAnnotation {
}
