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

package org.failearly.dataset.internal.template.generator;

import org.failearly.common.test.ExceptionVerifier;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * DateDecoderTest contains tests for ... .
 */
public class DateDecoderTest {

    @Test
    public void valid_date_string__should_return_valid_date() throws Exception {
        // arrange / given
        final DateDecoder decoder = new DateDecoder("yyyy-MM-dd");

        // act / when
        final Date date = decoder.toDate("2016-01-12");

        // assert / then
        assertThat(date.toString(), is("Tue Jan 12 00:00:00 CET 2016"));
    }

    @Test
    public void valid_date_time_string__should_return_valid_date_time() throws Exception {
        // arrange / given
        final DateDecoder decoder = new DateDecoder("yyyy-MM-dd HH:mm");

        // act / when
        final Date date = decoder.toDate("2016-01-12 12:31");

        // assert / then
        assertThat(date.toString(), is("Tue Jan 12 12:31:00 CET 2016"));
    }

    @Test
    public void invalid_date_string__should_throw_exception() throws Exception {
        // arrange / given
        final DateDecoder decoder = new DateDecoder("yyyy-MM-dd");

        // assert / then
        ExceptionVerifier.TestAction action=()->decoder.toDate("invalid-date");
        ExceptionVerifier.on(action).expect(IllegalArgumentException.class).expect("Can't parse 'invalid-date' by format 'yyyy-MM-dd'").verify();
    }
    @Test
    public void partial_valid_string__should_throw_exception() throws Exception {
        // arrange / given
        final DateDecoder decoder = new DateDecoder("yyyy-MM-dd HH:mm");

        // assert / then
        ExceptionVerifier.TestAction action=() -> decoder.toDate("2016-01-12");
        ExceptionVerifier.on(action).expect(IllegalArgumentException.class).expect("Can't parse '2016-01-12' by format 'yyyy-MM-dd HH:mm'").verify();

    }


}