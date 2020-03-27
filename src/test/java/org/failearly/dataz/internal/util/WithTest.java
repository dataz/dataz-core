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

package org.failearly.dataz.internal.util;

import org.failearly.dataz.exception.DataSetException;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * WithTest contains tests for ... .
 */
public class WithTest {
    private static final String ANY_DESCRIPTION = "Any description";
    private Exception exception;
    private String description;

    @Test
    public void actionNoException() throws Exception {
        // arrange / given
        final With with = With.create((d, ex) -> {
            exception = ex;
            description=d;
        }, "unknown");

        // act / when
        with.action(ANY_DESCRIPTION, () -> {/* do nothing*/});


        // assert / then
        assertThat("Exception?", exception, nullValue());
        assertThat("Description?", description, nullValue());
    }

    @Test
    public void actionException() throws Exception {
        // arrange / given
        final With with = With.create((d, ex) -> {
            exception = ex;
            description=d;
        }, "unknown");

        // act / when
        with.action(ANY_DESCRIPTION, () -> { throw new RuntimeException("Just a test");});


        // assert / then
        assertThat("Exception?", exception, notNullValue());
        assertThat("Description?", description, is(ANY_DESCRIPTION));
    }

    @Test(expected = DataSetException.class)
    public void actionDataSetException() throws Exception {
        // arrange / given
        final With with = With.create((d, ex) -> {
            exception = ex;
            description=d;
        }, "unknown");

        // act / when
        with.action(ANY_DESCRIPTION, () -> {
            throw new DataSetException("Just a test");
        });
    }

    @Test(expected = Error.class)
    public void actionError() throws Exception {
        // arrange / given
        final With with = With.create((d, ex) -> {
            exception = ex;
            description=d;
        }, "unknown");

        // act / when
        with.action(ANY_DESCRIPTION, () -> {
            throw new Error("Just a test");
        });
    }

    @Test
    public void producerNoException() throws Exception {
        // arrange / given
        final With with = With.create((d, ex) -> {
            exception = ex;
            description=d;
        }, "unknown");

        // act / when
        Boolean value=with.producer(ANY_DESCRIPTION, ()->true );


        // assert / then
        assertThat("Exception?", exception, nullValue());
        assertThat("Description?", description, nullValue());
        assertThat("Produced value?", value, is(true));

    }

    @Test
    public void producerException() throws Exception {
        // arrange / given
        final With with = With.create((d, ex) -> {
            exception = ex;
            description=d;
        }, "unknown");

        // act / when
        Boolean value=with.producer(ANY_DESCRIPTION, () -> { throw new RuntimeException("Just a test");});


        // assert / then
        assertThat("Exception?", exception, notNullValue());
        assertThat("Description?", description, is(ANY_DESCRIPTION));
        assertThat("Produced value?", value, nullValue());
    }

    @Test(expected = DataSetException.class)
    public void produceDataSetException() throws Exception {
        // arrange / given
        final With with = With.create((d, ex) -> {
            exception = ex;
            description=d;
        }, "unknown");

        // act / when
        with.producer(ANY_DESCRIPTION, () -> {
            throw new DataSetException("Just a test");
        });
    }

    @Test(expected = Error.class)
    public void producerError() throws Exception {
        // arrange / given
        final With with = With.create((d, ex) -> {
            exception = ex;
            description=d;
        }, "unknown");

        // act / when
        with.producer(ANY_DESCRIPTION, () -> {
            throw new Error("Just a test");
        });
    }

}