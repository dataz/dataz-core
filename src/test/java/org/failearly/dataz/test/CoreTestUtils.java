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

package org.failearly.dataz.test;

import org.failearly.common.test.utils.ReflectionUtils;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.internal.template.TemplateObjectsResolver;

import java.io.*;

public class CoreTestUtils {

    private static TemplateObjectsResolver templateObjectsResolver= TemplateObjectsResolver.testTemplateObjectResolver();

    /**
     * Tiny wrapper around {@link TemplateObjectsResolver} to resolve {@link TemplateObjects}.
     *
     * @param methodName the method name
     * @param clazz      the class to resolve from
     * @return list of generator creators.
     * @throws NoSuchMethodException method has not been found
     */
    public static TemplateObjects resolveTemplateObjects(String methodName, Class<?> clazz) throws NoSuchMethodException {
        return templateObjectsResolver.resolveFromMethod(
                ReflectionUtils.resolveMethodFromClass(methodName, clazz)
        );
    }

    /**
     * Reads the entire file content.
     *
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
}