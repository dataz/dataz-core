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

package org.failearly.dataz.internal.junit4;

/**
* CRE is a Class Rule Emulation test rule.
*/
final class FakeTestRule extends TestRuleBase<Object> {

    private static TestRuleSupport<FakeTestRule> support=new TestRuleSupport<>(FakeTestRule.class);

    int countDrop=0;
    int countInit=0;
    int countBefore =0;
    int countAfter =0;

    private FakeTestRule() {
    }

    static FakeTestRule create(Object testInstance) {
        return support.createTestRule(testInstance.getClass());
    }

    static FakeTestRule cre(Class<?> testClass) {
        return support.createTestRule(testClass);
    }


    static void reset() {
        support = new TestRuleSupport<>(FakeTestRule.class);
    }

    @Override
    protected void beforeTest(Object context) {
        countBefore++;
    }

    @Override
    protected void afterTest(Object context) {
        countAfter++;
    }

    @Override
    protected void initialize(Class<?> testClass, Object context) {
        countInit++;
    }

    @Override
    protected void dropTestClass(Class<?> testClass) {
        countDrop++;
    }
}
