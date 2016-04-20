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

package org.failearly.dataset.internal.template.generator;

import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
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