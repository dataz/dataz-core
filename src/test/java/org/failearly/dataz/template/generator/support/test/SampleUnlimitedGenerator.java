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
package org.failearly.dataz.template.generator.support.test;

import org.failearly.dataz.common.Tests;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.generator.Limit;

import java.lang.annotation.*;

/**
 * SampleUnlimitedGenerator is a Generator Annotation.
 *
 * TODO: What is the responsibility of SampleUnlimitedGenerator?
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(SampleUnlimitedGenerator.List.class)
@Tests("SampleUnlimitedGeneratorTest")
// TODO @TemplateObjectFactory.Definition(SampleUnlimitedGeneratorFactory.class)
public @interface SampleUnlimitedGenerator {
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
     * The generator type: LIMITED or UNLIMITED.
     *
     * @return Limit type.
     *
     * @see Limit#LIMITED
     * @see Limit#UNLIMITED
     */
    Limit limit() default Limit.UNLIMITED;


    // TODO: What are the SampleUnlimitedGenerator specific attributes?


    /**
     * Used by @Repeatable.
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        SampleUnlimitedGenerator[] value();
    }
}
