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

package org.failearly.dataz.internal.junit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TestRuleBase is the base implementation for {@link org.junit.rules.TestRule}.
 * It must be used with {@link org.failearly.dataz.internal.junit.TestRuleSupport}.
 *
 * @see org.failearly.dataz.internal.junit.TestRuleSupport
 * @see org.failearly.dataz.junit4.DataSetRule
 * @see org.failearly.dataz.junit4.DataStoreLoaderRule
 */
public abstract class TestRuleBase<T> implements TestRule {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private boolean isInitialized = false;
    private int numberOfTests = 0;
    private int numberOfExecutedTests = 0;
    private Class<?> testClass;
    private Object context;
    private boolean availableTests;

    protected TestRuleBase() {
    }

    /**
     * Will be called by {@link TestRuleSupport}.
     *
     * @param testClass     the test class
     * @param context       (optional) context object. Might be {@code null}.
     * @param numberOfTests the number of (resolved) tests.
     */
    final void init(Class<?> testClass, Object context, int numberOfTests) {
        if (testClass == null) {
            throw new IllegalArgumentException("Argument testClass must no be null");
        }
        assert this.testClass == null : "Init is not called once.";

        this.numberOfTests = numberOfTests;
        this.availableTests = numberOfTests > 0;
        this.testClass = testClass;
        this.context = context;

        assert this.numberOfExecutedTests == 0 : "No test should be executed";
        assert this.numberOfTests >= 0 : "# expected test(s) < 0";
    }

    protected Class<?> getTestClass() {
        return testClass;
    }

    @Override
    public Statement apply(Statement originStatement, Description description) {
        if (!availableTests) {
            return originStatement;
        }

        doInitialize();

        final T context = createContext(description);

        if (shouldApplyOriginStatement(context)) {
            numberOfExecutedTests++;
            return originStatement;
        }

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    LOGGER.debug("Execute test '{}'", description.getDisplayName());
                    beforeTest(context);
                    originStatement.evaluate();
                } finally {
                    try {
                        numberOfExecutedTests++;
                        afterTest(context);
                    } finally {
                        if (numberOfExecutedTests >= numberOfTests) {
                            LOGGER.debug("After last test. Execute dropTestClass() on test class {}!", description.getClassName());
                            dropTestClass(testClass);
                        }
                    }
                }
            }
        };
    }

    private void doInitialize() {
        if (!isInitialized && availableTests) {
            try {
                LOGGER.info("Initialize Rule for test class {}", testClass.getName());
                initialize(testClass, context);
            } finally {
                isInitialized = true;
            }
        }
    }

    /**
     * Do initialize the test rule instance. The default implementation is <em>empty</em>.
     *
     * @param testClass the test class
     * @param context   a context object (see 2nd parameter of {@link org.failearly.dataz.internal.junit.TestRuleSupport#createTestRule(Class, Object)})
     */
    protected void initialize(Class<?> testClass, Object context) {
        /* no-op */
    }

    /**
     * Creates a context object of type T.
     *
     * @param description the test description object.
     * @return a context object
     */
    protected T createContext(Description description) {
        /* no-op */
        return null;
    }

    /**
     * Sometimes the origin statement should be executed. The default implementation returns {@code false}.
     *
     * @param context the context object from {@link #createContext(org.junit.runner.Description)}.
     * @return {@code true} if the origin {@link org.junit.runners.model.Statement} should be executed.
     */
    protected boolean shouldApplyOriginStatement(T context) {
        /* no-op */
        return false;
    }

    /**
     * Called before the test (method). The default implementation is <em>empty</em>.
     *
     * @param context the context created by {@link #createContext(org.junit.runner.Description)}.
     */
    protected void beforeTest(T context) {
        /* no-op */
    }

    /**
     * Called after the test (method). The default implementation is <em>empty</em>.
     *
     * @param context the context created by {@link #createContext(org.junit.runner.Description)}.
     */
    protected void afterTest(T context) {
        /* no-op */
    }

    /**
     * At least drop the test class by calling {@link TestRuleSupport#dropInstance(Class)}.
     *
     * @param testClass the test class (class object)
     */
    protected abstract void dropTestClass(Class<?> testClass);
}
