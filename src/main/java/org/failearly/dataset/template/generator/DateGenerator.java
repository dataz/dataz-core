/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */

package org.failearly.dataset.template.generator;

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.internal.template.generator.DateGeneratorFactory;
import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObjectFactoryDefinition;
import org.failearly.dataset.template.generator.support.DateTime;

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
@Repeatable(DateGenerator.DateGenerators.class)
@TemplateObjectFactoryDefinition(factory=DateGeneratorFactory.class)
public @interface DateGenerator {
    /**
     * @return The name of the template object. Could be used in Velocity templates by {@code $<name>}.
     */
    String name();

    /**
     * @return The name of the associated dataset.
     *
     * @see org.failearly.dataset.DataSet#name()
     */
    String dataset() default Constants.DATASET_DEFAULT_NAME;

    /**
     * @return The scope of the template object (either {@link Scope#LOCAL} or {@link Scope#GLOBAL}.
     */
    Scope scope() default Scope.DEFAULT;

    /**
     * @return Limit type.
     *
     * @see org.failearly.dataset.template.generator.Limit#LIMITED
     * @see org.failearly.dataset.template.generator.Limit#UNLIMITED
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
     * @see java.text.SimpleDateFormat
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
    @interface DateGenerators {
        DateGenerator[] value();
    }

}
