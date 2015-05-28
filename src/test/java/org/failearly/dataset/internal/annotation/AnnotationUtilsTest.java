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

package org.failearly.dataset.internal.annotation;

import org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * AnnotationUtilsTest contains tests for {@link AnnotationUtils}.
 */
public class AnnotationUtilsTest {
    @Test
    public void getMetaAnnotation__should_return_meta_annotation_or_null() throws Exception {
        assertThat("Existing meta annotation?",                                                                                                       //
                AnnotationUtils.getMetaAnnotation(MetaAnnotation.class, WithMetaAnnotation.class.getAnnotation(UsingMetaAnnotation.class)),  //
                is(notNullValue()));

        assertThat("None Existing meta annotation?",                                                                                                  //
                AnnotationUtils.getMetaAnnotation(MetaAnnotation.class, WithoutMetaAnnotation.class.getAnnotation(AnyOtherAnnotation.class)),           //
                is(nullValue()));
    }

    @Test
    public void checkForMetaAnnotation__should_return_false_if_the_meta_annotation_is_not_available() throws Exception {
        assertFalse("Has no meta annotation?", AnnotationUtils.checkForMetaAnnotation(getMethod(WithoutMetaAnnotation.class), MetaAnnotation.class));
    }

    @Test
    public void checkForMetaAnnotation__should_return_true_if_the_meta_annotation_is_assigned_to_the_method() throws Exception {
        assertTrue("Has meta annotation on method?", AnnotationUtils.checkForMetaAnnotation(getMethod(MethodWithMetaAnnotation.class), MetaAnnotation.class));
    }

    @Test
    public void checkForMetaAnnotation__should_return_true_if_multiple_meta_annotation_are_available() throws Exception {
        assertTrue("Has multiple meta annotations (Java8 feature)?", AnnotationUtils.checkForMetaAnnotation(getMethod(MultipleMetaAnnotation.class), MetaAnnotation.class));
    }

    @Test
    public void checkForMetaAnnotation__should_return_true_if_meta_meta_annotation_are_available() throws Exception {
        assertTrue("Has meta meta annotations?", AnnotationUtils.checkForMetaAnnotation(getMethod(MethodWithMetaAnnotation.class), MarkerMetaAnnotation.class));
    }

    @Test
    public void checkForMetaAnnotation__should_return_true_if_the_meta_annotation_is_assigned_to_the_declaring_class() throws Exception {
        assertTrue("Has meta annotation on declaring class?", AnnotationUtils.checkForMetaAnnotation(getMethod(WithMetaAnnotation.class), MetaAnnotation.class));
    }

    @Test
    public void checkForMetaAnnotation__should_return_true_if_the_meta_annotation_is_assigned_to_any_super_class() throws Exception {
        assertTrue("Has meta annotation on super class?", AnnotationUtils.checkForMetaAnnotation(getMethod(SuperClassHasMetaAnnotation.class), MetaAnnotation.class));
    }



    private static Method getMethod(Class<?> clazz) throws NoSuchMethodException {
        return clazz.getMethod("anyMethod");
    }

    @UsingMetaAnnotation
    @AnyOtherAnnotation
    @SuppressWarnings("unused")
    private static class WithMetaAnnotation {
        public void anyMethod() {}
    }

    @AnyOtherAnnotation
    @SuppressWarnings("unused")
    private static class WithoutMetaAnnotation {
        public void anyMethod() {}
    }

    @SuppressWarnings("unused")
    private static class MethodWithMetaAnnotation {
        @UsingMetaAnnotation
        public void anyMethod() {}
    }

    @UsingMetaAnnotation(name = "1")
    @UsingMetaAnnotation(name = "2")
    @SuppressWarnings("unused")
    private static class MultipleMetaAnnotation {
        public void anyMethod() {}
    }

    @UsingMetaAnnotation
    private static class BaseClassWithMetaAnnotation {
    }

    @SuppressWarnings("unused")
    private static class SuperClassHasMetaAnnotation extends BaseClassWithMetaAnnotation {
       public void anyMethod() {}
    }
}