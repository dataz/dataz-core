/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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

import org.junit.*;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runners.MethodSorters;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * ClassRuleEmulationTest contains tests for ... .
 */
public class TestRuleBaseTest {
    private static boolean runWithinTest=false;

    /**
     * Skip all tests if not running within enclosing test class ClassRuleEmulationTest.
     */
    private static class IgnoreTestRule implements TestRule {
        private static final Logger LOGGER = LoggerFactory.getLogger(IgnoreTestRule.class);

        @Override
        public Statement apply(Statement base, Description description) {
            if( runWithinTest ) {
                return base;
            }

            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    LOGGER.debug("Test {} ignored!", description);
                    throw new AssumptionViolatedException("No tests actually. This is a fake test class");
                }
            };
        }
    }

    @BeforeClass @AfterClass
    public static void reset() {
        runWithinTest = ! runWithinTest;
    }

    private static FakeTestRule createTestRule(Class<?> testClass) {
        final FakeTestRule fakeTestRule = FakeTestRule.cre(testClass);
        assertThat("#Initialisation?", fakeTestRule.countInit, is(0));
        assertThat("#Before?", fakeTestRule.countBefore, is(0));
        assertThat("#After?", fakeTestRule.countAfter, is(0));
        assertThat("#Drop?", fakeTestRule.countDrop, is(0));
        return fakeTestRule;
    }

    private static void assertCallingsOnTestRule(FakeTestRule fakeTestRule, int expectedApplications) {
        assertThat("#Initialisation?", fakeTestRule.countInit, is(1));
        assertThat("#Before?", fakeTestRule.countBefore, is(expectedApplications));
        assertThat("#After?", fakeTestRule.countAfter, is(expectedApplications));
        assertThat("#Drop?", fakeTestRule.countDrop, is(1));
    }

    private static void assertTestRuleWithinTest(FakeTestRule fakeTestRule, int expectedApplications) {
        assertThat("#Initialisation?", fakeTestRule.countInit, is(1));
        assertThat("#Before?", fakeTestRule.countBefore, is(expectedApplications));
        assertThat("#After?", fakeTestRule.countAfter, is(expectedApplications-1));
        assertThat("#Drop?", fakeTestRule.countDrop, is(0));
    }

    @Before
    public void setUp() throws Exception {
        FakeTestRule.reset();
    }

    @Test
    public void runEmptyTestClass() throws Exception {
        // arrange / given
        final FakeTestRule fakeTestRule = createTestRule(EmptyTestClass.class);

        // act / when
        JUnitCore.runClasses(EmptyTestClass.class);

        // assert / then
        assertThat("#Initialisation?", fakeTestRule.countInit, is(0));
        assertThat("#Drop?", fakeTestRule.countDrop, is(0));
        assertThat("#Before?", fakeTestRule.countBefore, is(0));
        assertThat("#After?", fakeTestRule.countAfter, is(0));
    }

    private static void assertJunitResult(Result result, int expectedFailures, int expectedIgnored, int expectedRun) {
        Assert.assertThat("#Failed test(s)?", result.getFailureCount(), is(expectedFailures));
        Assert.assertThat("#Ignored test(s)?", result.getIgnoreCount(), is(expectedIgnored));
        Assert.assertThat("#Run test(s)?", result.getRunCount(), is(expectedRun));
    }

    @Test
    public void runSingleTestClass() throws Exception {
        // arrange / given
        final FakeTestRule fakeTestRule = createTestRule(SingleTestClass.class);

        // act / when
        final Result result=JUnitCore.runClasses(SingleTestClass.class);

        // assert / then
        assertCallingsOnTestRule(fakeTestRule, 3);
        assertJunitResult(result, 1 /*failed*/, 1/*ignored*/, 3/*total run*/);
        assertThat("CRE?", SingleTestClass.lastFakeTestRule, sameInstance(fakeTestRule));
    }

    @Test
    public void runSingleSubTestClass() throws Exception {
        // arrange / given
        final FakeTestRule fakeTestRule = createTestRule(SubTestClass.class);

        // act / when
        JUnitCore.runClasses(SubTestClass.class);

        // assert / then
        assertCallingsOnTestRule(fakeTestRule, 4);
        assertThat("CRE?", SingleTestClass.lastFakeTestRule, sameInstance(fakeTestRule));
    }

    @Test
    public void runMultipleTestClasses() throws Exception {
        // arrange / given
        final FakeTestRule fakeTestRule1 = createTestRule(SingleTestClass.class);
        final FakeTestRule fakeTestRule2 = createTestRule(SubTestClass.class);

        // act / when
        JUnitCore.runClasses(SingleTestClass.class, SubTestClass.class);

        // assert / then
        assertCallingsOnTestRule(fakeTestRule1, 3);
        assertCallingsOnTestRule(fakeTestRule2, 4);
        assertThat("CRE?", SingleTestClass.lastFakeTestRule, sameInstance(fakeTestRule2));
    }

    // FAKE TESTS.
    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    public static class SingleTestClass {
        static FakeTestRule lastFakeTestRule;

        @Rule
        public TestRule ignoreTests=new IgnoreTestRule();

        @Rule
        public FakeTestRule fakeTestRule = FakeTestRule.create(this);


        @After
        public void setUp() throws Exception {
            lastFakeTestRule = fakeTestRule;
        }

        @Test
        public void test1() throws Exception {
            assertTestRuleWithinTest(fakeTestRule, 1);
            assertTrue(true);
        }

        @Test
        public void test2() throws Exception {
            assertTestRuleWithinTest(fakeTestRule, 2);
            assertFalse(true);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test3() throws Exception {
            assertTestRuleWithinTest(fakeTestRule, 3);
            throw new IllegalArgumentException();
        }

        @Test @Ignore
        public void test4() throws Exception {
            throw new UnsupportedOperationException("Should not be executed at all");
        }
    }

    public static class SubTestClass extends SingleTestClass {
        @Test
        public void test5() throws Exception {
            assertTrue(true);
        }
    }

    public static class EmptyTestClass {
        @Rule
        public TestRule ignoreTests=new IgnoreTestRule();

        @Rule
        public FakeTestRule fakeTestRule = FakeTestRule.create(this);

        @Test @Ignore
        public void onlyIgnored() throws Exception {
            throw new UnsupportedOperationException("Should not be executed at all");
        }
    }

}