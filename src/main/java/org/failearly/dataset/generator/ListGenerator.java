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
package org.failearly.dataset.generator;

import org.failearly.dataset.generator.support.GeneratorFactoryDefinition;
import org.failearly.dataset.internal.generator.standard.ListGeneratorFactory;

import java.lang.annotation.*;

/**
 * ListGenerator provides a list of constant {@link #values()}.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@GeneratorFactoryDefinition(generatorFactory = ListGeneratorFactory.class)
@Documented
@Repeatable(ListGenerator.ListGenerators.class)
public @interface ListGenerator {
    /**
     * @return The name of the generator. Could be used in Velocity templates by {@code $<name>}.
     */
    String name();

    /**
     * @return The name of the associated dataset.
     *
     * @see org.failearly.dataset.DataSet#name()
     */
    String dataset() default "dataset";

    /**
     * @return Limit type.
     *
     * @see org.failearly.dataset.generator.Limit#LIMITED
     * @see org.failearly.dataset.generator.Limit#UNLIMITED
     */
    Limit limit() default Limit.LIMITED;

    /**
     * The values to be used by current. The values are strings, but could be used as int values as well.
     * @return the values.
     */
    String[] values();

    /**
     * Containing Annotation Type.
     *
     * Remark: This will be used by Java8 compiler.
     *
     * @see java.lang.annotation.Repeatable
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface ListGenerators {
        ListGenerator[] value();
    }
}