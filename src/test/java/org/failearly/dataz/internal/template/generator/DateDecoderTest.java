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

import org.failearly.dataz.internal.common.test.ExceptionVerifier;
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