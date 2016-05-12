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

package org.failearly.common.test;

import org.failearly.dataset.exception.DataSetException;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

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