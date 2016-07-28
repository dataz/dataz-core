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

import org.failearly.common.annotations.Tests;
import org.failearly.dataz.template.Scope;

import java.lang.annotation.*;

/**
 * CsvGenerator is a Generator Annotation. DO NOT USE.
 *
 * @deprecated NOT YET IMPLEMENTED
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(CsvGenerator.CsvGenerators.class)
// @TemplateObjectFactory.Definition(CsvGeneratorFactory.class)
@Tests("CsvGeneratorTest")
@Deprecated
public @interface CsvGenerator {
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

    // TODO: Add Template Object specific attributes


    /**
     * Used by @Repeatable.
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface CsvGenerators {
        CsvGenerator[] value();
    }
}
