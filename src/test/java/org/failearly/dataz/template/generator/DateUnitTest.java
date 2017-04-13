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

package org.failearly.dataz.template.generator;

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