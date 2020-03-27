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
     * Create {@link DataResource}s from the impl and the templateObjectCreators.
     *
     * @param annotatedClass    the annotated element (in this case the test class). Used for default resource name (if no resource has been set).
     * @param annotation        the actually impl
     * @param templateObjects the template objects
     * @return the resources
     */
    List<DataResource> createDataResources(Class<?> annotatedClass, Annotation annotation, TemplateObjects templateObjects);

    /**
     * Create {@link DataResource}s from the impl and the templateObjectCreators.
     *
     * @param annotatedMethod   the annotated element (in this case the test method). Used for default resource name (if no resource has been set).
     * @param annotation        the actually impl
     * @param templateObjects the template objects
     * @return the resources
     */
    List<DataResource> createDataResources(Method annotatedMethod, Annotation annotation, TemplateObjects templateObjects);

    /**
     * SetupDefinition is a (set up) meta impl used for creating DataResource objects, by associating a
     * {@link DataResourcesFactory} to any DataSet impl.
     *
     * @see org.failearly.dataz.internal.common.annotation.traverser.AnnotationTraverserBuilder#metaAnnotationTraverser(Class)
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
     * SetupDefinition is a (clean up) meta impl used for creating DataResource objects, by associating a
     * {@link DataResourcesFactory} to any DataSet impl.
     *
     * @see org.failearly.dataz.internal.common.annotation.traverser.AnnotationTraverserBuilder#metaAnnotationTraverser(Class)
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
     * DataSetMarker is a marker (meta) impl, for simplifying the check for relevant test methods.
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
