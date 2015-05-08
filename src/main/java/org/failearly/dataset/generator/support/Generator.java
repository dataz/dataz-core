/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.generator.support;

/**
 * Any generator used by {@link GeneratorFactoryDefinition} must implement at least current
 * interface.
 * <p>
 * A generator will be created by {@link GeneratorFactory}.
 */
public interface Generator<T> extends Iterable<T> {
    /**
     * @return  the name of the generator. The name will be used within the Velocity template as {@code $name}.
     */
    String name();

    /**
     * The name of the (associated dataset).
     * Remark: If there is no dataset with given {@link org.failearly.dataset.DataSet#name()},
     * the generator will not be used, even when the name used within the Velocity template.
     *
     * @return the name of the (associated dataset).
     *
     * @see org.failearly.dataset.DataSet#name()
     */
    String dataset();

    /**
     * @return the next value or {@code null}.
     */
    T next();
}
