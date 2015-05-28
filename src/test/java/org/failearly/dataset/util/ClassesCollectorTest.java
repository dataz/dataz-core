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

package org.failearly.dataset.util;

import org.failearly.dataset.test.TestUtils;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * ClassesCollectorTest contains tests for {@link ClassesCollector}.
 */
public class ClassesCollectorTest {

    private final ClassesCollector topDownCollector=new ClassesCollector(ClassesCollector.Order.TOP_DOWN, ClassesCollector.ENTIRE_HIERARCHY_DEPTH);
    private final ClassesCollector bottomUpCollector=new ClassesCollector(ClassesCollector.Order.BOTTOM_UP, ClassesCollector.ENTIRE_HIERARCHY_DEPTH);

    @Test
    public void collect_from_method__should_be_the_same_collecting_from_declaring_class() throws Exception {
        // arrange / given
        final Method method = TestUtils.resolveMethodFromClass("aMethod", DerivedFromBaseClass.class);
        final Class<?> declaringClass = method.getDeclaringClass();

        // assert / then
        assertThat("Same result?",
                topDownCollector.collect(method),
                is(topDownCollector.collect(declaringClass))
        );
    }

    @Test
    public void collect_from_method_in_base_class() throws Exception {
        final Method method = TestUtils.resolveMethodFromClass("aMethod", BaseClass.class);
        assertThat("SuperClasses in correct order?",                                   //
                topDownCollector.collect(method),                                      //
                contains(BaseClass.class)                                              //
        );
    }

    @Test
    public void collect_from_method_in_derived_class_top_down() throws Exception {
        final Method method = TestUtils.resolveMethodFromClass("aMethod", DerivedFromBaseClass.class);
        assertThat("SuperClasses in correct order?",                                    //
                topDownCollector.collect(method),                                       //
                contains(DerivedFromBaseClass.class, BaseClass.class)                   //
        );
    }

    @Test
    public void collect_from_method_in_derived_class_bottom_up() throws Exception {
        final Method method = TestUtils.resolveMethodFromClass("aMethod", DerivedFromBaseClass.class);
        assertThat("SuperClasses in correct (reverted) order?",                          //
                bottomUpCollector.collect(method),                                       //
                contains(BaseClass.class, DerivedFromBaseClass.class)                    //
        );
    }

    @Test
    public void collect_only_declaring_class_from_method_top_down() throws Exception {
        // arrange / given
        final Method method = TestUtils.resolveMethodFromClass("aMethod", DerivedFromBaseClass.class);

        // act / when
        final ClassesCollector restrictedTopDownCollector=new ClassesCollector(ClassesCollector.Order.TOP_DOWN, ClassesCollector.ONLY_DECLARING_CLASS_DEPTH);

        // assert / then
        assertThat("Just the declaring class (TOP DOWN)?",                         //
                restrictedTopDownCollector.collect(method),                        //
                contains(DerivedFromBaseClass.class)                               //
        );
    }

    @Test
    public void collect_only_declaring_class_from_method_bottom_up() throws Exception {
        // arrange / given
        final Method method = TestUtils.resolveMethodFromClass("aMethod", DerivedFromBaseClass.class);

        // act / when
        final ClassesCollector restrictedBottomUpCollector=new ClassesCollector(ClassesCollector.Order.BOTTOM_UP, ClassesCollector.ONLY_DECLARING_CLASS_DEPTH);

        // assert / then
        assertThat("Just the declaring class (BOTTOM UP)?",                         //
                restrictedBottomUpCollector.collect(method),                        //
                contains(DerivedFromBaseClass.class)                               //
        );
    }

    // Test Fixtures.
    @SuppressWarnings("unused")
    private static class BaseClass {
        public void aMethod() {}
    }

    private static class DerivedFromBaseClass extends BaseClass {
        @Override
        public void aMethod() {}
    }

}