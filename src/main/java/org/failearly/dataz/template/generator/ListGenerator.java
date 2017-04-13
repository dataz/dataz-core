/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.template.generator;

import org.failearly.dataz.internal.template.generator.ListGeneratorFactory;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectFactory;

import java.lang.annotation.*;

/**
 * ListGenerator provides a list of constant {@link #values()}.
 * <br>Usage Example:<br><br>
 * <pre>
 *   {@literal @Test}
 *   {@literal @}ListGenerator(name="names", values={"Marko", "Adam", "Brian"})
 *    public void my_test() {
 *        // The 'names' generator generates:
 *        // Marko, Adam, Brian
 *    }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ListGenerator.List.class)
@TemplateObjectFactory.Definition(ListGeneratorFactory.class)
public @interface ListGenerator {
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
     * @return Limit type.
     *
     * @see org.failearly.dataz.template.generator.Limit#LIMITED
     * @see org.failearly.dataz.template.generator.Limit#UNLIMITED
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
    @interface List {
        ListGenerator[] value();
    }
}
