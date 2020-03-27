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
package org.failearly.dataz.internal.resource.resolver;

import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;

import java.lang.reflect.Method;
import java.util.List;

/**
 * DataResourcesResolver is responsible for resolving {@link DataResource} from method or class objects.
 *
 * @see DataResourcesResolvers
 */
public interface DataResourcesResolver {
    /**
     * Resolve from method object.
     * @param method the method object
     * @param templateObjects the available template objects
     * @return the available resources.
     */
    List<DataResource> resolveFromMethod(Method method, TemplateObjects templateObjects);

    /**
     * Resolve from class object.
     * @param clazz the class object
     * @param templateObjects the available template objects
     * @return the available resources.
     */
    List<DataResource> resolveFromClass(Class<?> clazz, TemplateObjects templateObjects);
}
