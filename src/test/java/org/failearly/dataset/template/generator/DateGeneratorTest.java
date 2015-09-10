/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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
 */

package org.failearly.dataset.template.generator;

import org.failearly.dataset.internal.template.generator.DateGeneratorFactory;
import org.failearly.dataset.template.generator.support.test.GeneratorTestBase;
import org.junit.*;

import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * DateGeneratorTest contains tests for {@link DateGenerator}.
 */
public class DateGeneratorTest extends GeneratorTestBase<String, DateGenerator, DateGeneratorFactory> {
    private static final int DATE_WITH_DAY_UNIT_GENERATOR=0;
    private static final int DATE_TIME_WITH_2_HOUR_STEP_GENERATOR=1;
    private static final int INVALID_DATE_GENERATOR=2;
    private static final int ONE_DATE_GENERATOR=3;
    private static final int TIMEZONE_GMT_PLUS_3_GENERATOR=4;

    private static TimeZone defaultTimeZone;

    public DateGeneratorTest() {
        super(DateGenerator.class, DateGeneratorFactory.class, TestFixture.class);
    }


    @BeforeClass
    public static void saveDefaultTimeZone() throws Exception {
        defaultTimeZone=TimeZone.getDefault();
    }

    @After
    public void restoreDefaultTimeZone() throws Exception {
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    public void external_iterator_on_date_generator__should_generate_dates_with_1_day_difference() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createGenerator(DATE_WITH_DAY_UNIT_GENERATOR)
        );

        // assert / then
        assertThat(generated, is("2008-02-28;2008-02-29;2008-03-01;"));
    }

    @Test
    public void internal_iterator__on_date_generator_with_max_limit__should_generate_all_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_INTERNAL_ITERATOR, 3),
                createGenerator(DATE_WITH_DAY_UNIT_GENERATOR)
        );

        // assert / then
        assertThat(generated, is(
            "(1) next=2008-02-28,last=2008-02-28/" +
                "(2) next=2008-02-29,last=2008-02-29/" +
                "(3) next=2008-03-01,last=2008-03-01/"
        ));
    }

    @Test
    public void external_iterator__on_date_time_and_2_hour_steps_generator___should_generate_datetimes_with_1_hour_difference() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createGenerator(DATE_TIME_WITH_2_HOUR_STEP_GENERATOR)
        );

        // assert / then
        assertThat(generated, is(
            "2015-07-26 00:00;" +
                "2015-07-26 02:00;" +
                "2015-07-26 04:00;" +
                "2015-07-26 06:00;" +
                "2015-07-26 08:00;"
        ));
    }

    @Test
    public void invalid_generator__should_generate_nothing() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createGenerator(INVALID_DATE_GENERATOR)
        );

        // assert / then
        assertThat(generated, is(""));
    }


    @Test
    public void one_date_generator__should_generate_only_one_date() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createGenerator(ONE_DATE_GENERATOR)
        );

        // assert / then
        assertThat(generated, is("2008-01-01;"));
    }

    @Test
    public void timezone_date_generator__should_generate_date_times_accordingly() throws Exception {
        // arrange / given
        TimeZone.setDefault(TimeZone.getTimeZone(PredefinedTimeZones.GMT));

        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createGenerator(TIMEZONE_GMT_PLUS_3_GENERATOR)
        );

        // assert / then
        assertThat(generated, is("2015-07-26 03:00;"));
    }

    @DateGenerator(name=TEMPLATE_OBJECT_NAME, from="2008-02-28", to="2008-03-01", format="yyyy-MM-dd", unit=DateUnit.DAYS)
    @DateGenerator(name=TEMPLATE_OBJECT_NAME, from="2015-07-26 00:00", to="2015-07-26 08:00", format="yyyy-MM-dd HH:mm", unit=DateUnit.HOURS, step= 2)
    @DateGenerator(name=TEMPLATE_OBJECT_NAME, from="2008-02-28", to="1999-01-01")
    @DateGenerator(name=TEMPLATE_OBJECT_NAME, from="2008-01-01", to="2008-01-01", unit=DateUnit.MILLISECONDS)
    @DateGenerator(name=TEMPLATE_OBJECT_NAME, from="2015-07-26 00:00", to="2015-07-26 00:00", format="yyyy-MM-dd HH:mm", timeZone = "GMT+3")
    private static class TestFixture {
    }
}
