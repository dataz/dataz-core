/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.internal.util;

import java.io.InputStream;

/**
 * ResourceUtils provides utilities for resource handling.
 */
public final class ResourceUtils {

    /**
     * Check for existence of the named resource in the classpath
     *
     * @param testClass the test class.
     * @param fullQualifiedResourceName the full qualified resource name.
     *
     * @return {@code true} if the resource exists in the classpath.
     */
    public static boolean resourceExistsInClassPath(Class<?> testClass, String fullQualifiedResourceName) {
        return null!=testClass.getResource(fullQualifiedResourceName);
    }

    /**
     * Opens the named resource as {@link java.io.InputStream}.
     *
     * @param testClass the test class.
     * @param fullQualifiedResourceName the full qualified resource name.
     *
     * @return the input stream.
     */
    public static InputStream openResource(Class<?> testClass, String fullQualifiedResourceName) {
        return IOUtils.autoClose(testClass.getResourceAsStream(fullQualifiedResourceName));
    }

}
