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
 * TestClass contains one or more {@link AtomicTest} instances.
 */
public interface TestClass {
    /**
     * Return the test method instance.
     * @param testMethodName the test methods name
     * @return the test method or an instance of {@link NullTest}.
     */
    AtomicTest getAtomicTest(String testMethodName);

}
