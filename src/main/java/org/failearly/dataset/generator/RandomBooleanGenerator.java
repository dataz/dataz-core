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
import org.failearly.dataset.internal.generator.standard.RandomBooleanGeneratorFactory;

import java.lang.annotation.*;

/**
 * RandomBooleanGenerator generates a random sequence of {@code true} and {@code false}. It's only available as
 * {@link org.failearly.dataset.generator.Limit#UNLIMITED}  generator.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@GeneratorFactoryDefinition(generatorFactory = RandomBooleanGeneratorFactory.class)
@Documented
@Repeatable(RandomBooleanGenerator.RandomBooleanGenerators.class)
public @interface RandomBooleanGenerator {
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
     * @return the percentage ({@code >0} and {@code < 100}) of {@code true} values.
     */
    float percent();

    /**
     * Seed value for {@link java.util.Random}. ({@value GeneratorConstants#NO_SEED} means dynamic
     * or no seed value).
     *
     * @return the seed value or {@link org.failearly.dataset.generator.GeneratorConstants#NO_SEED}.
     *
     * @see GeneratorConstants#NO_SEED
     * @see java.util.Random#seed
     */
    int seed() default GeneratorConstants.NO_SEED;

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
    @interface RandomBooleanGenerators {
        RandomBooleanGenerator[] value();
    }
}
