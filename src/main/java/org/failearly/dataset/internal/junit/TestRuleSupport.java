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
package org.failearly.dataset.internal.junit;

import org.failearly.dataset.util.ObjectCreator;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * ClassRuleEmulationSupport is responsible for creating a (single) instance per test class and
 * resolving the actually number of tests.
 */
public final class TestRuleSupport<T extends TestRuleBase> {
    private final Class<T> testRuleClass;
    private final Map<Class<?>, T> testClasses = new ConcurrentHashMap<>();

    public TestRuleSupport(Class<T> testRuleClass) {
        this.testRuleClass = testRuleClass;
    }

    public T createTestRule(Class<?> testClass, Object context, Predicate<Method> predicate) {
        return testClasses.computeIfAbsent(testClass, (cls) -> createInstance(cls, context, predicate));
    }

    public T createTestRule(Class<?> testClass) {
        return createTestRule(testClass, null);
    }

    public T createTestRule(Class<?> testClass, Object context) {
        return createTestRule(testClass, context, (cls) -> true);
    }

    public void cleanInstances() {
        testClasses.clear();
    }

    /**
     * To be called by implementation of {@link TestRuleBase#dropTestClass(Class)}.
     *
     * @param testClass the test class.
     */
    public void dropInstance(Class<?> testClass) {
        testClasses.remove(testClass);
    }

    @SuppressWarnings("unchecked")
    private T createInstance(Class<?> testClass, Object context, Predicate<Method> predicate) {
        final T instance = ObjectCreator.createInstanceByConstructor(testRuleClass);

        instance.init(testClass, context, resolveNumberOfTests(testClass, predicate));

        return instance;
    }

    private static int resolveNumberOfTests(Class<?> testClass, Predicate<Method> predicate) {
        if (!testClass.isAnnotationPresent(Ignore.class)) {
            return (int) Arrays.asList(testClass.getMethods()).stream()
                    .filter(TestRuleSupport::isValidJUnit4Method)
                    .filter(predicate)
                    .count();
        }
        return 0;
    }

    private static boolean isValidJUnit4Method(Method method) {
        return method.isAnnotationPresent(Test.class)
                && !method.isAnnotationPresent(Ignore.class)
                && method.getParameterCount() == 0
                && method.getReturnType().equals(void.class)
                && 0 != (method.getModifiers() & Modifier.PUBLIC);
    }

}
