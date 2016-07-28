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

package org.failearly.dataz.template.generator;

import org.failearly.dataz.internal.template.generator.LoopGeneratorFactory;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectFactory;

import java.lang.annotation.*;

/**
 * LoopGenerator starts with {@code 1} and ends with {@link #size()}. Useful in conjunction with unlimited generators.
 * <br>Usage Example:<br><br>
 * <pre>
 *   {@literal @Test}
 *   {@literal @}LoopGenerator(name="loop", size=10)
 *    public void my_test() {
 *        // The 'loop' generator generates:
 *        // 1, 2, ... , 10
 *    }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(LoopGenerator.List.class)
@TemplateObjectFactory.Definition(LoopGeneratorFactory.class)
public @interface LoopGenerator {
    /**
     * @return The name of the template object. Could be used in Velocity templates by {@code $<name>}.
     */
    String name();

    /**
     * Each template object could be assigned to one or more Datasets.
     *
     * There are following options:
     *
     * + No assignment (empty or default datasets): Assign it to all DataSets in scope.
     * + A set of datasets: Assign it to the given DataSets in scope.
     *
     * @return The associated or all DataSets.
     *
     * @see org.failearly.dataz.DataSet#name()
     * @see org.failearly.dataz.DataSetup#name()
     * @see org.failearly.dataz.DataCleanup#name()
     */
    String[] datasets() default {};


    /**
     * @return The scope of the template object (either {@link Scope#LOCAL} or {@link Scope#GLOBAL}.
     */
    Scope scope() default Scope.DEFAULT;

    /**
     * The loop's size (at least 1). The last value generated will be {@code size}.
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
    @interface List {
        LoopGenerator[] value();
    }
}
