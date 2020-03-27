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

import org.failearly.dataz.DataSet;
import org.failearly.dataz.internal.template.generator.RandomBooleanGeneratorFactory;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectFactory;

import java.lang.annotation.*;

/**
 * RandomBooleanGenerator generates a random stream of {@code true} and {@code false}. It's only available as
 * {@link org.failearly.dataz.template.generator.Limit#UNLIMITED} generator.
 * Usage Example:<br><br>
 * <pre>
 *   {@literal @Test}
 *   {@literal @}{@link DataSet}(dataz="d1", setup="t1.vm") // content see below
 *   {@literal @}{@link DataSet}(dataz="d2", setup="t2.vm") // content see below
 *   {@literal @}RandomBooleanGenerator(name="ten", dataz="d1", percent=10, seed=314)
 *   {@literal @}RandomBooleanGenerator(name="eighty", dataz="d2", percent=80, seed=42)
 *    public void my_test() {
 *        // The 'ten' with template t1.vm generates:
 *        // 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
 *
 *        // The 'eighty' with template t2.vm generates:
 *        // true, true, true, false, true, true, true, true, false, true,
 *    }
 * </pre>
 * Content of the templates:<br><br>
 * <pre>
 *    // Content of t1.vm
 *    #foreach( $i in [1 .. 20] )if(${ten.next()}) 1,#else 0,#end#end
 *
 *    // Content of t2.vm
 *    #foreach( $i in [1 .. 10] )${eighty.next()}#end
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(RandomBooleanGenerator.List.class)
@TemplateObjectFactory.Definition(RandomBooleanGeneratorFactory.class)
public @interface RandomBooleanGenerator {
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
     * @return the percentage ({@code >0} and {@code < 100}) of {@code true} values.
     */
    float percent();

    /**
     * Seed value for {@link java.util.Random}.
     *
     * @return the seed value or {@link org.failearly.dataz.template.generator.GeneratorConstants#DEFAULT_SEED}.
     *
     * @see GeneratorConstants#DEFAULT_SEED
     * @see java.util.Random#seed
     */
    int seed() default GeneratorConstants.DEFAULT_SEED;

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
        RandomBooleanGenerator[] value();
    }
}
