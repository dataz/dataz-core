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

package org.failearly.dataz.template.simple;

import org.failearly.common.annotations.Tests;
import org.failearly.dataz.internal.template.simple.ConstantFactory;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectFactory;

import java.lang.annotation.*;

/**
 * "Generates" a single {@link #value} value.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@TemplateObjectFactory.Definition(ConstantFactory.class)
@Documented
@Repeatable(Constant.List.class)
@Tests("ConstantTest")
public @interface Constant {
    /**
     * Every template object needs a name.
     * <br><br>
     * The template object will be accessible by name within your templates. Could be used within (Velocity)
     * templates by {@code $<name>} or {@code ${<name>}}.
     * <br><br>
     * IMPORTANT: The name must be unique (within the template).
     *
     * @return The name of the template object.
     */
    String name();

    /**
     * Assign the template object to datasets.
     * <br><br>
     * The template object could be used by more then one template. If you assign it to one (or more) dataset(s),
     * only the templates of the assigned datasets could access this template object.
     * <br><br>
     * If you <b>do not</b> assign any dataset (by using the default), this template object is accessible by
     * all datasets within scope.
     *
     * @return The associated datasets.
     *
     * @see org.failearly.dataz.DataSet#name()
     * @see org.failearly.dataz.DataSetup#name()
     * @see org.failearly.dataz.DataCleanup#name()
     */
    String[] datasets() default {};

    /**
     * @return The scope of the template object (either {@link Scope#LOCAL} or {@link Scope#GLOBAL}.
     *
     * @see Scope
     */
    Scope scope() default Scope.DEFAULT;

    /**
     * Constant value ({@code null} is permitted).
     *
     * @return any constant value as String.
     */
    String value();

    /**
     * Containing Annotation Type.
     * <p>
     * Remark: This will be used by Java8 compiler.
     *
     * @see java.lang.annotation.Repeatable
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Constant[] value();
    }
}
