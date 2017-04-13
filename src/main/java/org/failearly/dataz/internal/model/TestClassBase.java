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

import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotationTraverser;
import org.failearly.dataz.internal.common.annotation.traverser.TraverseDepth;
import org.failearly.dataz.internal.common.annotation.traverser.TraverseStrategy;
import org.failearly.dataz.NoDataSet;
import org.failearly.dataz.resource.DataResourcesFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.failearly.dataz.internal.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;

/**
 * TestClassBase is the base implementation for any {@link TestClass}.
 *
 * You have to implement {@link #createInstanceFrom(Class)}
 *
 * @see TestClassCollection
 */
public abstract class TestClassBase implements TestClass {

    private static final MetaAnnotationTraverser<DataResourcesFactory.DataSetMarker> DATA_SET_MARKER_TRAVERSER = metaAnnotationTraverser(DataResourcesFactory.DataSetMarker.class)
            .withTraverseDepth(TraverseDepth.HIERARCHY)
            .withTraverseStrategy(TraverseStrategy.TOP_DOWN)
            .build();
    private final Map<String,AtomicTest> testInstances =new HashMap<>();
    private final Class<?> testClass;

    protected TestClassBase(Class<?> testClass) {
        this.testClass = testClass;
    }

    @Override
    public final AtomicTest getAtomicTest(String testMethodName) {
        final AtomicTest testInstance = testInstances.get(testMethodName);
        Objects.requireNonNull(testInstance, "Implementation failure in isTestMethod(). The test instance " + testMethodName + " has not been created");
        return testInstance;
    }

    private static boolean isAnnotatedWithNoDataSet(Method testMethod) {
        return testMethod.isAnnotationPresent(NoDataSet.class);
    }

    private AtomicTest createTestInstance(Method testMethod) {
        if( isAnnotatedWithNoDataSet(testMethod) || isNotAnnotatedWithDataSetAnnotation(testMethod) ) {
            return new NullTest(testMethod.getName());
        }

        return AtomicTestInstance.createTestInstance(testMethod, testClass);
    }

    private boolean isNotAnnotatedWithDataSetAnnotation(Method testMethod) {
        return ! DATA_SET_MARKER_TRAVERSER.anyAnnotationAvailable(testMethod);
    }


    final TestClass resolveTestInstances() {
        Arrays.stream(this.testClass.getMethods())    //
            .filter(this::isTestMethod)          //
            .forEach((testMethod) -> testInstances.put(testMethod.getName(), createTestInstance(testMethod)));
        return this;
    }

    /**
     * Prototype pattern. Create a new instance of TestClass for given {@code testClass}.
     * @param testClass the test class.
     * @return a {@link TestClassBase} extension
     */
    protected abstract TestClassBase createInstanceFrom(Class<?> testClass);

    /**
     * If there are any marker annotations, implement this method.
     *
     * The default implementation returns {@code true}.
     *
     * @param method the method instance.
     * @return {@code false} if not a test method, which should not be collected.
     */
    protected boolean isTestMethod(Method method) {
        return true;
    }
}
