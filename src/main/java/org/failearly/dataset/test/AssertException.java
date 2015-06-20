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

package org.failearly.dataset.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Contains utility methods for testing exceptions, to avoid <code>@Test(expected=MyException.class)</code> tests.
 */
public final class AssertException {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssertException.class);

    private AssertException() {
    }

    /**
     * Assert an exception of type {@code exceptionClass} .
     *
     * @param exceptionClass  the expected exception class.
     * @param action          the action to execute.
     */
    public static void assertException(Class<? extends Throwable> exceptionClass, TestAction action) {
        assertException(exceptionClass, null, action);
    }

    /**
     * Assert an exception of type {@code exceptionClass} and given {@code expectedMessage}.
     *
     * @param exceptionClass  the expected exception class.
     * @param expectedMessage the expected {@link Throwable#getMessage()} or null.
     * @param action          the action to execute.
     */
    public static void assertException(Class<? extends Throwable> exceptionClass, String expectedMessage, TestAction action) {
        boolean exception = false;
        try {
            action.run();
        } catch (Throwable ex) {
            LOGGER.info("Caught exception", ex);
            exception = true;
            assertThat("Exception type?", ex, instanceOf(exceptionClass));
            if (null != expectedMessage)
                assertThat("Exception message?", ex.getMessage(), is(expectedMessage));
        }
        if (!exception) {
            fail("No exception caught. Expected exception type: " + exceptionClass.getSimpleName() + "!");
        }
    }

    /**
     * TestAction used by {@link #assertException(Class, String, TestAction)}.
     */
    @FunctionalInterface
    public interface TestAction {
        void run() throws Throwable;
    }
}