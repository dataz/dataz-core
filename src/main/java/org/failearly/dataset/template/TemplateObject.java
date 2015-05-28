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
package org.failearly.dataset.template;

/**
 * TemplateObject is a named object which could be used in {@link org.failearly.dataset.resource.DataResource} templates. The template object
 * could be used within any {@link org.failearly.dataset.resource.DataResource} with the same dataset name.
 *
 * @see TemplateEngine
 * @see org.failearly.dataset.resource.DataResource
 */
public interface TemplateObject {
    /**
     * The id is {@literal <dataset>-<name>}.
     */
    String id();

    /**
     * The name will be used within the Velocity template as {@code $name}. So be carefully with the name. Not every name will be accepted by
     * Velocity.
     *
     * @return  the name of the template engine.
     */
    String name();

    /**
     * The name of the (associated dataset).
     *
     * <b>Remark</b>: If there is no dataset with given (i.e.) {@link org.failearly.dataset.DataSet#name()},
     * the template object will not be used, even when the name has been used within the Velocity template.
     *
     * @return the name of the (associated dataset).
     *
     * @see org.failearly.dataset.DataSet#name()
     * @see org.failearly.dataset.DataSetup#name()
     * @see org.failearly.dataset.DataCleanup#name()
     */
    String dataset();

    void __use_TemplateObjectBase__instead_of_implementing_TemplateObject();
}
