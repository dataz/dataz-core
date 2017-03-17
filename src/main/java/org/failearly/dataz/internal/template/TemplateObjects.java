/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.failearly.dataz.internal.template;

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;

import java.util.List;
import java.util.function.Consumer;

/**
 * TemplateObjects is responsible for holding {@link TemplateObject} instances.
 *
 * @see TemplateObjectsResolver
 */
public interface TemplateObjects {
    /**
     * Creates and collect all {@link TemplateObject} instances and checks for duplicates.
     *
     * @return template object instances
     */
    List<TemplateObject> collectTemplateObjectInstances();

    /**
     * Add a template object creator instance. Called by {@link TemplateObjectsResolver}.
     *
     * @param templateObjectCreator a new instance.
     */
    void add(TemplateObjectCreator templateObjectCreator);

    /**
     * Filter the template objects which belongs to given data set.
     *
     * @param dataSetName the name of the data set.
     * @return filtered instance of template objects.
     */
    TemplateObjects filterByDataSet(String dataSetName);

    /**
     * Return all template objects with {@link Scope#GLOBAL}.
     *
     * @return new template objects instance.
     */
    TemplateObjects filterGlobalScope();

    /**
     * Merge two template objects.
     *
     * @param templateObjects the other template objects instance.
     * @return a new instance.
     */
    TemplateObjects merge(TemplateObjects templateObjects);

    /**
     * Applies all (newly created) template objects and send them to {@code templateObjectConsumer}.
     *
     * If two template objects have the same {@code name}, the second will be ignored.
     *
     * @param templateObjectConsumer a consumer function
     * @see TemplateObject#name()
     */
    void apply(Consumer<TemplateObject> templateObjectConsumer);

    /**
     * # *DO NOT USE*
     * Just for test purposes.
     */
    static TemplateObjects noTemplateObjects() {
        return TemplateObjectsImpl.NO_TEMPLATE_OBJECTS;
    }
}
