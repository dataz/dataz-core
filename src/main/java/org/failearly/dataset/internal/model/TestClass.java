/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */
package org.failearly.dataset.internal.model;

import org.failearly.dataset.SuppressDataSet;
import org.failearly.common.annotation.utils.AnnotationUtils;
import org.failearly.dataset.internal.annotations.DataSetMarkerAnnotation;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.*;

/**
 * TestClass represents a single test class. It is responsible for collecting {@link TestMethod}s by name.
 */
public final class TestClass {
    private final Map<String,TestMethod> testMethods=new HashMap<>();
    private final Class<?> testClass;

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
        Arrays.stream(testClass.getMethods())
              // TODO: JUnit specific. Use Predication lambda instead.
              .filter((method) -> method.isAnnotationPresent(Test.class))
              .filter((method) -> ! method.isAnnotationPresent(Ignore.class))
              //
              .forEach((testMethod) -> testMethods.put(testMethod.getName(), createTestMethod(testMethod)));
    }

    private TestMethod createTestMethod(Method testMethod) {
        if( testMethod.isAnnotationPresent(SuppressDataSet.class) ) {
            return new NullTestMethod(testMethod.getName());
        }

        if( ! AnnotationUtils.checkForMetaAnnotation(testMethod, DataSetMarkerAnnotation.class) ) {
            return new NullTestMethod(testMethod.getName());
        }

        return TestMethodImpl.createTestMethod(testMethod, testClass);
    }

    /**
     * Return the test method instance.
     * @param testMethodName the test methods name
     * @return the test method or an instance of {@link org.failearly.dataset.internal.model.NullTestMethod}.
     */
    public TestMethod getTestMethod(String testMethodName) {
        final TestMethod testMethod = testMethods.get(testMethodName);
        Objects.requireNonNull(testMethod, "Implementation failure in TestClass.resolveTestMethods(). The test method " + testMethodName + " has not been created");
        return testMethod;
    }
}
