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
package org.failearly.dataz.template.generator.support.test;

import org.failearly.dataz.common.Tests;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.generator.Limit;

import java.lang.annotation.*;

/**
 * SampleLimitedGenerator is a Generator Annotation.
 *
 * TODO: What is the responsibility of SampleLimitedGenerator?
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(SampleLimitedGenerator.List.class)
@Tests("SampleLimitedGeneratorTest")
@TemplateObjectFactory.Definition(SampleLimitedGeneratorFactory.class)
public @interface SampleLimitedGenerator {
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
    Limit limit() default Limit.LIMITED;



    // TODO: What are the SampleLimitedGenerator specific attributes?


    /**
     * Used by @Repeatable.
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        SampleLimitedGenerator[] value();
    }
}
