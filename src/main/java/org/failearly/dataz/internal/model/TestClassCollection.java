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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TestClassCollection creates a collection of test classes using a {@link TestClassBase} prototype.
 */
public final class TestClassCollection {
    private final ConcurrentMap<Class<?>, TestClass> testClasses=new ConcurrentHashMap<>();
    private final TestClassBase testClassPrototype;

    public TestClassCollection(TestClassBase testClassPrototype) {
        this.testClassPrototype = testClassPrototype;
    }

    public TestClass createOrFetchTestClass(Class<?> testClassInstance) {
        return testClasses.computeIfAbsent(testClassInstance, (testClass) -> this.testClassPrototype.createInstanceFrom(testClass).resolveTestInstances());
    }


}
