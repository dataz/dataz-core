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

package org.failearly.dataz.internal.model;

/**
 * AtomicTest collect all necessary information from a Test {@link java.lang.reflect.Method} - a public method
 * annotated with {@link org.junit.Test} and not {@link org.junit.Ignore}.
 */
public interface AtomicTest {
    /**
     * @return {@code true} if there is any valid resource to apply.
     */
    boolean isValid();

    /**
     * @return the test methods name.
     */
    String getName();

    /**
     * Apply setup resources.
     */
    void setup();

    /**
     * Apply cleanup resources.
     */
    void cleanup();

    /**
     * @return {@code true} if the method or class has been annotated with {@link org.failearly.dataz.SuppressCleanup}.
     */
    boolean isSuppressCleanup();
}
