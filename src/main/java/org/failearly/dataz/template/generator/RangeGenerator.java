/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.template.generator;

import org.failearly.dataz.internal.template.generator.RangeGeneratorFactory;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectFactory;

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
@TemplateObjectFactory.Definition(RangeGeneratorFactory.class)
@Documented
@Repeatable(RangeGenerator.List.class)
public @interface RangeGenerator {

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
     * The Limit type. Limited by {@code to-from+1}.
     *
     * @return Limit type.
     *
     * @see org.failearly.dataz.template.generator.Limit#LIMITED
     * @see org.failearly.dataz.template.generator.Limit#UNLIMITED
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
    @interface List {
        RangeGenerator[] value();
    }
}
