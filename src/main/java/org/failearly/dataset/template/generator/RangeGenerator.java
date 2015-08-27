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

package org.failearly.dataset.template.generator;

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObjectFactoryDefinition;
import org.failearly.dataset.internal.template.generator.RangeGeneratorFactory;

import java.lang.annotation.*;

/**
 * Generates integer values from {@link #from()} to {@link #to()} and step width {@link #step()}.
 *
 * <br>Usage Example:<br><br>
 * <pre>
 *   {@literal @Test}
 *   {@literal @}RangeGenerator(name="range", from=4, to=12, step=2)
 *    public void my_test() {
 *        // The 'range' generator generates:
 *        // 4, 6, 8, 10, 12
 *    }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@TemplateObjectFactoryDefinition(factory = RangeGeneratorFactory.class)
@Documented
@Repeatable(RangeGenerator.RangeGenerators.class)
public @interface RangeGenerator {

    /**
     * @return The name of the template object. Could be used in Velocity templates by {@code $<name>}.
     */
    String name();

    /**
     * @return The name of the associated dataset.
     *
     * @see org.failearly.dataset.DataSet#name()
     */
    String dataset() default Constants.DATASET_DEFAULT_NAME;

    /**
     * @return The scope of the template object (either {@link Scope#LOCAL} or {@link Scope#GLOBAL}.
     */
    Scope scope() default Scope.DEFAULT;

    /**
     * The Limit type. Limited by {@code to-from+1}.
     *
     * @return Limit type.
     *
     * @see org.failearly.dataset.template.generator.Limit#LIMITED
     * @see org.failearly.dataset.template.generator.Limit#UNLIMITED
     */
    Limit limit() default Limit.LIMITED;

    /**
     * The lower bound. All generated values will be in range {@code [from,to]}
     *
     * @return the lower bound.
     */
    int from() default 0;

    /**
     * The upper bound. All generated values will be in range {@code [from,to]}
     *
     * @return the upper bound ({@code >= from}).
     */
    int to() default Integer.MAX_VALUE-1;

    /**
     * The step width.
     * @return the step width ({@code >= 1})
     */
    int step() default 1;

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
    @interface RangeGenerators {
        RangeGenerator[] value();
    }
}
