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

package org.failearly.dataz.test;

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectFactory;

import java.lang.annotation.*;

/**
 * SimpleTemplateObject is a template object annotation as replacement for real ones, which has only the minimal set of annotation's attributes.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@TemplateObjectFactory.Definition(SimpleTemplateObjectFactory.class)
@Repeatable(SimpleTemplateObject.List.class)
public @interface SimpleTemplateObject {

    /**
     * @return The name of the generator. Could be used in Velocity templates by {@code $<name>}.
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
     * The description.
     */
    String description() default "<no description>";

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
        SimpleTemplateObject[] value();
    }
}
