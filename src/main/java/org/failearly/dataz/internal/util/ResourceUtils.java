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

package org.failearly.dataz.internal.util;

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
