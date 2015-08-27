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

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * DateUnitTest contains tests for ... .
 */
public class DateUnitTest {

    private static final long MILLI_SECONDS_FACTOR = 1;
    private static final long SECONDS_FACTOR = 1000;
    private static final long MINUTES_FACTOR = 60*SECONDS_FACTOR;
    private static final long HOURS_FACTOR = 60*MINUTES_FACTOR;
    private static final long DAYS_FACTOR = 24*HOURS_FACTOR;
    private static final long WEEKS_FACTOR = 7*DAYS_FACTOR;
    private static final long MONTHS_FACTOR = 30*DAYS_FACTOR;
    private static final long YEARS_FACTOR = 365*DAYS_FACTOR;

    @Test
    public void milliseconds_next() throws Exception {
        assertNext(DateUnit.MILLISECONDS, MILLI_SECONDS_FACTOR);
    }

    @Test
    public void seconds() throws Exception {
        assertNext(DateUnit.SECONDS, SECONDS_FACTOR);
    }

    @Test
    public void minutes() throws Exception {
        assertNext(DateUnit.MINUTES, MINUTES_FACTOR);
    }

    @Test
    public void hours() throws Exception {
        assertNext(DateUnit.HOURS, HOURS_FACTOR);
    }

    @Test
    public void days() throws Exception {
        assertNext(DateUnit.DAYS, DAYS_FACTOR);
    }

    @Test
    public void weeks() throws Exception {
        assertNext(DateUnit.WEEKS, WEEKS_FACTOR);
    }

    @Test
    public void months() throws Exception {
        assertNext(DateUnit.MONTHS, MONTHS_FACTOR);
    }

    @Test
    public void years() throws Exception {
        assertNext(DateUnit.YEARS, YEARS_FACTOR);
    }

    private static void assertNext(DateUnit unit, long factor) {
        assertThat(unit.next(1), is(factor));
        assertThat(unit.next(42), is(42L * factor));
        assertThat(unit.next(Integer.MAX_VALUE), is(Integer.MAX_VALUE * factor));
        assertThat(unit.next(Integer.MIN_VALUE), is(Integer.MIN_VALUE * factor));
    }
}