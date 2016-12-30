/*
 * Copyright (c) 2009.
 *
 * Date: 31.10.16
 *
 */
package org.failearly.dataz.template.generator.support.test;

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.generator.Limit;

import java.lang.annotation.*;

/**
 * SampleLimitedGenerator is a Sample Generator Annotation.
 *
 * No responsibility at all.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(SampleLimitedGenerator.List.class)
@org.failearly.common.annotations.Tests("SampleLimitedGeneratorTest")
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
     * @return Limit type.
     *
     * @see org.failearly.dataz.template.generator.Limit#LIMITED
     * @see org.failearly.dataz.template.generator.Limit#UNLIMITED
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
