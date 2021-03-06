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

import org.failearly.dataz.internal.template.generator.DateGeneratorFactory;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.generator.support.DateTime;

import java.lang.annotation.*;
import java.util.TimeZone;

/**
 * DateGenerator generates dates or date times. It's possible to customize ...
 * <br><br>
 * <ul>
 * <li>format ({@link #format()}),</li>
 * <li>time zone ({@link #timeZone()}),</li>
 * <li>(step) unit ({@link #unit()}) and</li>
 * <li>step width {@link #step()}</li>
 * </ul>.
 * <br><br>
 * Remark: DateGenerator generates {@link DateTime} objects, not {@link java.util.Date}.
 * <br><br>
 * There are some predefined formats available in {@link PredefinedDateFormats}.
 * <br>Usage Example:<br><br>
 * <pre>
 *   {@literal @Test}
 *   {@literal @}DateGenerator(name="three_weeks", from="2015-01-01", to="2015-12-31", unit={@link DateUnit#WEEKS},
 * step=3)
 *    public void my_test() {
 *        // The 'three_weeks' generator (using the default {@link PredefinedDateFormats#ISO_DATE_FORMAT}) generates:
 *        // 2015-01-01, 2015-01-22, 2015-02-12, ... , 2015-12-24
 *    }
 * </pre>
 *
 * @see PredefinedDateFormats
 * @see DateTime
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(DateGenerator.List.class)
@TemplateObjectFactory.Definition(DateGeneratorFactory.class)
public @interface DateGenerator {
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
     * The from date. All generated values will be in range {@code [from,to]}
     *
     * @return the from date.
     */
    String from();

    /**
     * The to date. All generated values will be in range {@code [from,to]}
     *
     * @return the to date ({@code >=from}).
     */
    String to();

    /**
     * The format String. Default is {@link PredefinedDateFormats#ISO_DATE_FORMAT}.
     *
     * @return the format string
     *
     * @see PredefinedDateFormats
     */
    String format() default PredefinedDateFormats.ISO_DATE_FORMAT;

    /**
     * The next value (depends on {@link #unit()} or step width.
     *
     * @return the number of {@link DateUnit}s.
     */
    int step() default 1;

    /**
     * The date/time unit. Be careful with {@link DateUnit#MONTHS} and {@link DateUnit#YEARS}.
     *
     * @return the date time unit.
     *
     * @see #step()
     */
    DateUnit unit() default DateUnit.DAYS;

    /**
     * The time zone of the generated date times. The default uses the default timezone of the local machine.
     *
     * @return time zone of the generated date times.
     *
     * @see java.util.TimeZone#getTimeZone(String)
     * @see TimeZone#getDefault()
     */
    String timeZone() default PredefinedTimeZones.USE_DEFAULT;

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
        DateGenerator[] value();
    }

}
