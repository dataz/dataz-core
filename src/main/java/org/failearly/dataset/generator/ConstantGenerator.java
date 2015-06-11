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

package org.failearly.dataset.generator;

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObjectFactoryDefinition;
import org.failearly.dataset.internal.generator.standard.ConstantGeneratorFactory;

import java.lang.annotation.*;

/**
 * "Generates" a single {@link #constant} value.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@TemplateObjectFactoryDefinition(factory = ConstantGeneratorFactory.class)
@Documented
@Repeatable(ConstantGenerator.ConstantGenerators.class)
public @interface ConstantGenerator {

    /**
     * @return The name of the generator. Could be used in Velocity templates by {@code $<name>}.
     */
    String name();

    /**
     * @return The name of the associated dataset.
     *
     * @see org.failearly.dataset.DataSet#name()
     */
    String dataset() default Constants.DATASET_DEFAULT_NAME;;

    /**
     * @return Limit type.
     *
     * @see org.failearly.dataset.generator.Limit#LIMITED
     * @see org.failearly.dataset.generator.Limit#UNLIMITED
     */
    Limit limit() default Limit.UNLIMITED;

    /**
     * @return the count, if the generator used as {@link #limit()} = Limit.LIMITED.
     */
    int count() default 1;

    /**
     * Constant value ({@code null} is permitted).
     *
     * @return any constant value as String.
     */
    String constant();

    /**
     * @return The scope of the template object (either {@link Scope#LOCAL} or {@link Scope#GLOBAL}.
     */
    Scope scope() default Scope.DEFAULT;

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
    @interface ConstantGenerators {
        ConstantGenerator[] value();
    }
}
