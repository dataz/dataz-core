/*
 * dataSet - Test Support For Datastores.
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

package org.failearly.dataset.util;

import org.failearly.dataset.test.TestUtils;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * ClassUtilsTest contains tests for ... .
 */
public class ClassUtilsTest {

    @Test
    public void collectClass() throws Exception {
        final Method testMethod = TestUtils.resolveMethodFromClass("anyTestMethod", BaseClass.class);
        assertThat("SuperClasses in correct order?", ClassUtils.collectClasses(testMethod), contains(BaseClass.class));
    }

    @Test
    public void collectClassHierarchy() throws Exception {
        final Method testMethod = TestUtils.resolveMethodFromClass("anyTestMethod", TestFixture.class);
        assertThat("SuperClasses in correct order?", ClassUtils.collectClasses(testMethod), contains(TestFixture.class, BaseClass.class));
    }

    @Test
    public void collectClassHierarchyReverted() throws Exception {
        final Method testMethod = TestUtils.resolveMethodFromClass("anyTestMethod", TestFixture.class);
        assertThat("SuperClasses in correct order?", ClassUtils.collectClassesReverted(testMethod), contains(BaseClass.class, TestFixture.class));
    }

    private static class BaseClass {
        public void anyTestMethod() {}
    }

    private static class TestFixture extends BaseClass {

        @Override
        public void anyTestMethod() {}

    }

}