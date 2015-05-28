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

import org.failearly.dataset.template.TemplateObjectFactoryDefinition;
import org.failearly.dataset.internal.generator.standard.RandomRangeGeneratorFactory;

import java.lang.annotation.*;

/**
 * Generates random integer values from {@link #start()} to {@link #end()}.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@TemplateObjectFactoryDefinition(factory = RandomRangeGeneratorFactory.class)
@Documented
@Repeatable(RandomRangeGenerator.RandomRangeGenerators.class)
public @interface RandomRangeGenerator {
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
     * Define the Generator as limited or unlimited. Caution: If the generator should produce
     * unique values (see {@link #unique()}), the element will be ignored.
     * @return Limit type.
     *
     * @see org.failearly.dataset.generator.Limit#LIMITED
     * @see org.failearly.dataset.generator.Limit#UNLIMITED
     */
    Limit limit() default Limit.UNLIMITED;

    /**
     * Set to a appropriate value in case of {@link Limit#LIMITED}. Otherwise no effect.
     *
     * @return the limit counter
     */
    int count() default -1;

    /**
     * The lower bound. All generated values will be in range {@code [start,end]}
     *
     * @return the lower bound.
     */
    int start() default 0;

    /**
     * The upper bound. All generated values will be in range {@code [start,end]}
     *
     * @return the upper bound ({@code >=start}).
     */
    int end() default Integer.MAX_VALUE-1;

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
     * Mark the generator as unique. A unique generator will forced to a limited generator - even you declare it {@code Limit.UNLIMITED}.
     *
     * @return generate only unique values.
     *
     * @see #limit()
     */
    boolean unique() default false;

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
    @interface RandomRangeGenerators {
        RandomRangeGenerator[] value();
    }
}
