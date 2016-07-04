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

package org.failearly.dataz.junit4;

import org.failearly.dataz.internal.junit4.TestRuleBase;
import org.failearly.dataz.internal.junit4.TestRuleSupport;
import org.failearly.dataz.internal.model.TestClass;
import org.failearly.dataz.internal.model.AtomicTest;
import org.junit.runner.Description;

/**
 * DataSetRule is responsible for creating the {@link TestClass} object.
 */
public final class DataSetRule extends TestRuleBase<AtomicTest> {

    private static final TestRuleSupport<DataSetRule> support = new TestRuleSupport<>(DataSetRule.class);

    private TestClass testClass;


    /**
     * Create a {@link DataSetRule} instance.
     *
     * @param testInstance the test instance.
     *
     * @return the TestRule instance for current test class.
     */
    public static DataSetRule createDataSetRule(Object testInstance) {
        return createDataSetRule(testInstance.getClass());
    }

    /**
     * Create a {@link DataSetRule} instance.
     *
     * @param testClass the test class.
     *
     * @return the TestRule instance for current test class.
     */
    public static DataSetRule createDataSetRule(Class<?> testClass) {
        return support.createTestRule(testClass);
    }

    public static void reset() {
        support.cleanInstances();
    }

    @Override
    protected void initialize(Class<?> testClass, Object notUsed) {
        this.testClass = TestClass.create(testClass);
    }

    @Override
    protected AtomicTest createContext(Description description) {
        final String methodName = description.getMethodName();
        return testClass.getTestInstance(methodName);
    }

    @Override
    protected boolean shouldApplyOriginStatement(AtomicTest currentTestMethod) {
        return ! currentTestMethod.isValid();
    }

    @Override
    protected void beforeTest(AtomicTest currentTestMethod) {
        currentTestMethod.setup();
    }

    @Override
    protected void afterTest(AtomicTest currentTestMethod) {
        currentTestMethod.cleanup();
    }

    @Override
    protected void dropTestClass(Class<?> testClass) {
        support.dropInstance(testClass);
    }

}
