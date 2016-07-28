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
