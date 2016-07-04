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

package org.failearly.dataz.internal.model;

import org.failearly.common.annotation.traverser.MetaAnnotationTraverser;
import org.failearly.common.annotation.traverser.TraverseDepth;
import org.failearly.common.annotation.traverser.TraverseStrategy;
import org.failearly.dataz.NoDataSet;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.failearly.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;
import static org.failearly.dataz.resource.DataResourcesFactory.DataSetMarker;

/**
 * TestClass represents a single test class. It is responsible for collecting {@link AtomicTest}s by name.
 */
public final class TestClass {
    private final Map<String,AtomicTest> testInstances =new HashMap<>();
    private final Class<?> testClass;
    private static final MetaAnnotationTraverser<DataSetMarker> DATA_SET_MARKER_TRAVERSER = metaAnnotationTraverser(DataSetMarker.class)
            .withTraverseDepth(TraverseDepth.HIERARCHY)
            .withTraverseStrategy(TraverseStrategy.TOP_DOWN)
            .build();

    private TestClass(Class<?> testClass) {
        this.testClass = testClass;
    }

    /**
     * Factory method.
     *
     * @param testClass the test class.
     *
     * @return the {@code TestClass} instance.
     */
    public static TestClass create(Class<?> testClass) {
        final TestClass instance = new TestClass(testClass);
        instance.resolveTestMethods();
        return instance;
    }

    private void resolveTestMethods() {
        Arrays.stream(testClass.getMethods())                                      //
              // TODO: JUnit4 specific. Use Predication lambda instead.            //
              .filter((method) -> method.isAnnotationPresent(Test.class))          //
              .filter((method) -> ! method.isAnnotationPresent(Ignore.class))      //
              .forEach((testMethod) -> testInstances.put(testMethod.getName(), createTestInstance(testMethod)));
    }

    private AtomicTest createTestInstance(Method testMethod) {
        if( isAnnotatedWithSuppressDataSet(testMethod) || isNotAnnotatedWithDataSetMarker(testMethod) ) {
            return new NullTest(testMethod.getName());
        }

        return AtomicTestInstance.createTestInstance(testMethod, testClass);
    }

    private static boolean isAnnotatedWithSuppressDataSet(Method testMethod) {
        return testMethod.isAnnotationPresent(NoDataSet.class);
    }

    private boolean isNotAnnotatedWithDataSetMarker(Method testMethod) {
        return ! DATA_SET_MARKER_TRAVERSER.anyAnnotationAvailable(testMethod);
    }

    /**
     * Return the test method instance.
     * @param testMethodName the test methods name
     * @return the test method or an instance of {@link NullTest}.
     */
    public AtomicTest getTestInstance(String testMethodName) {
        final AtomicTest testInstance = testInstances.get(testMethodName);
        Objects.requireNonNull(testInstance, "Implementation failure in TestClass.resolveTestMethods(). The test instance " + testMethodName + " has not been created");
        return testInstance;
    }
}
