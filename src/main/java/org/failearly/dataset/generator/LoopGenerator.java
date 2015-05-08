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
import org.failearly.dataset.internal.generator.standard.LoopGeneratorFactory;

import java.lang.annotation.*;

/**
 * LoopGenerator starts with {@code 1} and ends with {@link #size()}. Useful in conjunction with unlimited generators.
 *
 * @see org.failearly.dataset.generator.Limit
 * @see org.failearly.dataset.generator.RandomRangeGenerator
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@GeneratorFactoryDefinition(generatorFactory = LoopGeneratorFactory.class)
@Documented
@Repeatable(LoopGenerator.LoopGenerators.class)
public @interface LoopGenerator {
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
     * The loop's size. The last value generated will be {@code size}.
     * @return the loop's size.
     */
    int size();

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
    @interface LoopGenerators {
        LoopGenerator[] value();
    }
}
