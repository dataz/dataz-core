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

package org.failearly.dataz.resource;


import org.failearly.dataz.internal.model.AtomicTest;
import org.failearly.dataz.internal.template.TemplateObjects;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.List;

/**
 * DataSetupResourceFactory creates {@link DataResource}s from DataSet annotations annotated with
 * {@link SetupDefinition} and/or {@link CleanupDefinition}.
 *
 * @see SetupDefinition
 * @see CleanupDefinition
 * @see org.failearly.dataz.DataSet
 * @see org.failearly.dataz.DataSetup
 * @see org.failearly.dataz.DataCleanup
 */
public interface DataResourcesFactory {
    /**
     * Create {@link DataResource}s from the annotation and the templateObjectCreators.
     *
     * @param annotatedClass    the annotated element (in this case the test class). Used for default resource name (if no resource has been set).
     * @param annotation        the actually annotation
     * @param templateObjects the template objects
     * @return the resources
     */
    List<DataResource> createDataResources(Class<?> annotatedClass, Annotation annotation, TemplateObjects templateObjects);

    /**
     * Create {@link DataResource}s from the annotation and the templateObjectCreators.
     *
     * @param annotatedMethod   the annotated element (in this case the test method). Used for default resource name (if no resource has been set).
     * @param annotation        the actually annotation
     * @param templateObjects the template objects
     * @return the resources
     */
    List<DataResource> createDataResources(Method annotatedMethod, Annotation annotation, TemplateObjects templateObjects);

    /**
     * SetupDefinition is a (set up) meta annotation used for creating DataResource objects, by associating a
     * {@link DataResourcesFactory} to any DataSet annotation.
     *
     * @see org.failearly.common.annotation.traverser.AnnotationTraverserBuilder#metaAnnotationTraverser(Class)
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @DataSetMarker
    @interface SetupDefinition {
        /**
         * @return the factory class
         */
        Class<? extends DataResourcesFactory> value();
    }

    /**
     * SetupDefinition is a (clean up) meta annotation used for creating DataResource objects, by associating a
     * {@link DataResourcesFactory} to any DataSet annotation.
     *
     * @see org.failearly.common.annotation.traverser.AnnotationTraverserBuilder#metaAnnotationTraverser(Class)
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @DataSetMarker
    @interface CleanupDefinition {
        /**
         * @return the factory class
         */
        Class<? extends DataResourcesFactory> value();
    }

    /**
     * DataSetMarker is a marker (meta) annotation, for simplifying the check for relevant test methods.
     *
     * @see org.failearly.dataz.internal.model.TestClassBase
     * @see AtomicTest
     *
     * @see SetupDefinition
     * @see CleanupDefinition
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface DataSetMarker {
    }
}
