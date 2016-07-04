/*
 * Copyright (c) 2009.
 *
 * Date: 31.05.16
 * 
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
