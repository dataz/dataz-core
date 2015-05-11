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
package org.failearly.dataset.resource;


import org.failearly.dataset.annotations.DataCleanupResourceFactoryDefinition;
import org.failearly.dataset.annotations.DataSetupResourceFactoryDefinition;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * DataSetupResourceFactory creates {@link DataResource}s from dataSet annotations annotated with
 * {@link DataSetupResourceFactoryDefinition} and/or {@link DataCleanupResourceFactoryDefinition}.
 * <br><br>
 * Remark: Any implementation won't be called directly.
 *
 * @see DataSetupResourceFactoryDefinition
 * @see DataCleanupResourceFactoryDefinition
 */
public interface DataResourcesFactory {
    /**
     * Create {@link org.failearly.dataset.resource.DataResource}s from the annotation and the generatorCreators.
     *
     * @param annotatedClass    the annotated element (in this case the test class). Used for default resource name (if no resource has been set).
     * @param annotation        the actually annotation
     * @param generatorCreators the generators
     * @return the resources
     */
    List<DataResource> createDataResources(Class<?> annotatedClass, Annotation annotation, List<GeneratorCreator> generatorCreators);

    /**
     * Create {@link org.failearly.dataset.resource.DataResource}s from the annotation and the generatorCreators.
     *
     * @param annotatedMethod   the annotated element (in this case the test method). Used for default resource name (if no resource has been set).
     * @param annotation        the actually annotation
     * @param generatorCreators the generators
     * @return the resources
     */
    List<DataResource> createDataResources(Method annotatedMethod, Annotation annotation, List<GeneratorCreator> generatorCreators);
}
