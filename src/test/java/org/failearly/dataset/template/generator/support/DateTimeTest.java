/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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

package org.failearly.dataset.template.generator.support;

import org.failearly.dataset.internal.template.generator.DateEncoder;
import org.failearly.dataset.template.generator.DateUnit;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * DateTimeTest contains tests for ... .
 */
public class DateTimeTest {

    private static final long NINE_ELEVEN_IN_MILLI_SECONDS=1_000_190_790_000L;
    private final DateTime dateTime=new DateTime(new DateEncoder("yyyy-MM-dd'T'HH:mm:ss"), NINE_ELEVEN_IN_MILLI_SECONDS);

    private void assertDateTime(DateTime newDateTime, String expectedDateTime, long expectedMilliSeconds) {
        assertThat(newDateTime, not(sameInstance(this.dateTime)));
        assertThat("As string?", newDateTime.toString(), is(expectedDateTime));
        assertThat("In milliseconds?", newDateTime.getMilliSeconds(), is(expectedMilliSeconds));
    }

    private static long nineElevenPlus(int value, DateUnit unit) {
        return NINE_ELEVEN_IN_MILLI_SECONDS + unit.next(value);
    }

    @Test
    public void getMilliSeconds__should_return_origin_datetime() throws Exception {
        assertThat("In Milliseconds?", dateTime.getMilliSeconds(), is(NINE_ELEVEN_IN_MILLI_SECONDS));
    }

    @Test
    public void toString__should_return_formatted_date() throws Exception {
        assertThat("As String?", dateTime.toString(), is("2001-09-11T08:46:30"));
    }

    @Test
    public void addMilliSeconds__should_create_new_adapted_datetime() throws Exception {
        final int milliseconds=2_000;
        assertDateTime(
                dateTime.addMilliSeconds(milliseconds),
                "2001-09-11T08:46:32",
                nineElevenPlus(milliseconds, DateUnit.MILLISECONDS)
        );
    }

    @Test
    public void addSeconds__should_create_new_adapted_datetime() throws Exception {
        final int seconds=2;
        assertDateTime(
                dateTime.addSeconds(seconds),
                "2001-09-11T08:46:32",
                nineElevenPlus(seconds, DateUnit.SECONDS)
        );
    }

    @Test
    public void addMinutes__should_create_new_adapted_datetime() throws Exception {
        final int minutes=2;
        assertDateTime(
                dateTime.addMinutes(minutes),
                "2001-09-11T08:48:30",
                nineElevenPlus(minutes, DateUnit.MINUTES)
        );
    }


    @Test
    public void addHours__should_create_new_adapted_datetime() throws Exception {
        final int hours=3;
        assertDateTime(
                dateTime.addHours(hours),
                "2001-09-11T11:46:30",
                nineElevenPlus(hours, DateUnit.HOURS)
        );
    }

    @Test
    public void addDays__should_create_new_adapted_datetime() throws Exception {
        final int days=1;
        assertDateTime(
                dateTime.addDays(days),
                "2001-09-12T08:46:30",
                nineElevenPlus(days, DateUnit.DAYS)
        );
    }

    @Test
    public void addWeeks__should_create_new_adapted_datetime() throws Exception {
        final int weeks=1;
        assertDateTime(
                dateTime.addWeeks(weeks),
                "2001-09-18T08:46:30",
                nineElevenPlus(weeks, DateUnit.WEEKS)
        );
    }
}