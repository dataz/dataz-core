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

import org.failearly.dataset.internal.template.TemplateObjectsResolver;
import org.failearly.dataset.internal.template.TemplateObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public final class TestUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

    private TestUtils() {
    }

    /**
     * Resolve {@link java.lang.reflect.Method} instance from class with given methodName.
     *
     * @param methodName the method's name
     * @param clazz      the class
     * @return the method instance
     * @throws NoSuchMethodException method has not been found
     */
    public static Method resolveMethodFromClass(String methodName, Class<?> clazz) throws NoSuchMethodException {
        return clazz.getMethod(methodName);
    }

    /**
     * Tiny wrapper around {@link TemplateObjectsResolver} to resolve {@link TemplateObjects}.
     *
     * @param methodName the method name
     * @param clazz      the class to resolve from
     * @return list of generator creators.
     * @throws NoSuchMethodException method has not been found
     */
    public static TemplateObjects resolveTemplateObjects(String methodName, Class<?> clazz) throws NoSuchMethodException {
        return TemplateObjectsResolver.resolveFromTestMethod(resolveMethodFromClass(methodName, clazz));
    }

    /**
     * Reads the entire file content.
     * @param file the file
     * @return the content of the file
     * @throws FileNotFoundException thrown file does not exists
     */
    public static String fileToString(File file) throws FileNotFoundException {
        return inputStreamToString(new FileInputStream(file));
    }

    /**
     * Converts the {@code inputStream} to String. The {@code inputStream} will be consumed.
     *
     * @param inputStream the input stream
     * @return the content as string.
     */
    public static String inputStreamToString(InputStream inputStream) {
        final StringBuilder output = new StringBuilder();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while (null != (line = reader.readLine())) {
                output.append(line).append('\n');
            }
        } catch (IOException ex) {
            throw new RuntimeException("Caught IO Exception", ex);
        }


        return output.toString();
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
}