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

import org.failearly.dataz.internal.template.generator.RandomRangeGeneratorFactory;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectFactory;

import java.lang.annotation.*;

/**
 * Generates random integer values from {@link #start()} to {@link #end()}. By default a {@code RandomRangeGenerator} is
 * {@link Limit#UNLIMITED} generator. So if you need a limited one, make it either by setting {@link #limit}
 * or {@link #unique()}{@code =true}.
 * <br>
 * Usage Example:<br><br>
 * <pre>
 *   {@literal @Test}
 *   {@literal @}RandomRangeGenerator(name="range1", start=1, end=10, seed=42)
 *   {@literal @}RandomRangeGenerator(name="range2", start=1, end=10, seed=314)
 *   {@literal @}RandomRangeGenerator(name="ids", start=1, end=11, seed=42, unique=true, count=10)
 *   {@literal @}RandomRangeGenerator(name="vals", start=1, end=5, seed=42, limit={@link Limit#LIMITED}, count=10)
 *    public void my_test() {
 *        // The 'range1' generates:
 *        // 1, 4, 9, 5, 1, 6, ...
 *        // The 'range2' generates:
 *        // 2, 6, 4, 1, 3, 7, ...
 *        // The 'ids' generates:
 *        // 8, 5, 9, 6, 4, 2, 3, 11, 7, 1, 10
 *        // The 'vals' generates:
 *        // 1, 4, 4, 5, 1, 1, 1, 4, 5, 4
 *    }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(RandomRangeGenerator.List.class)
@TemplateObjectFactory.Definition(RandomRangeGeneratorFactory.class)
public @interface RandomRangeGenerator {
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
     * Define the Generator as limited or unlimited. Caution: If the generator should produce
     * unique values (see {@link #unique()}), the element will be ignored.
     * @return Limit type.
     *
     * @see org.failearly.dataz.template.generator.Limit#LIMITED
     * @see org.failearly.dataz.template.generator.Limit#UNLIMITED
     */
    Limit limit() default Limit.UNLIMITED;

    /**
     * Set to a appropriate value in case of {@link Limit#LIMITED} or {@link #unique()} generator.
     * Otherwise no effect. If the value is {@literal < 1} count will be ignored and the range size will be used.
     *
     * @return the limit counter (or {@link GeneratorConstants#INVALID_COUNT})
     */
    int count() default GeneratorConstants.INVALID_COUNT;

    /**
     * The start value or lower bound of the range. All generated values will be in range {@code [start,end]}.
     * Default value is {@code 0}.
     *
     * @return the lower bound (&lt;{@code end})
     */
    int start() default 0;

    /**
     * The end value or upper bound of the range. All generated values will be in range {@code [start,end]}.
     * Default value is {@code Integer.MAX_VALUE-1}.
     *
     * @return the upper bound (&gt;{@code start}).
     */
    int end() default Integer.MAX_VALUE-1;

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
     * Mark the generator as unique. A unique generator will forced to a limited generator - even you declare it {@code Limit.UNLIMITED}.
     *
     * @return generate only unique values.
     *
     * @see #limit()
     */
    boolean unique() default false;

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
        RandomRangeGenerator[] value();
    }
}
