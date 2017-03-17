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

package org.failearly.dataz.internal.template.generator;

import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * DateEncoderTest contains tests for ... .
 */
public class DateEncoderTest {

    @Test
    public void zero_in_greenwich_mean_time__should_be_converted_to_1st_jan_1970_00_00_00() throws Exception {
        // arrange / given
        final DateEncoder encoder = new DateEncoder("dd-MM-yyyy'T'HH:mm:ss","GMT-0");

        // act / when
        final String date = encoder.toDateString(0L);

        // assert / then
        assertThat(date, is("01-01-1970T00:00:00"));
    }
    @Test
    public void now__should_be_converted_to_today() throws Exception {
        // arrange / given
        final String format = "dd-MM-yyyy";
        final String timezone = "GMT+1";
        final long now = System.currentTimeMillis();
        final DateEncoder encoder = new DateEncoder(format, timezone);

        // act / when
        final String date = encoder.toDateString(now);

        // assert / then
        assertThat(date, is(DateFormatUtils.format(now, format, TimeZone.getTimeZone(timezone))));
    }
}