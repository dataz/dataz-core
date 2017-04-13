/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.internal.common.annotation.utils;

import org.failearly.dataz.internal.common.annotation.test.AnyOtherAnnotation;
import org.failearly.dataz.internal.common.annotation.test.MetaAnnotation;
import org.failearly.dataz.internal.common.annotation.test.UsingMetaAnnotation;
import org.failearly.dataz.internal.common.test.ExceptionVerifier;
import org.failearly.dataz.internal.common.test.annotations.Subject;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.assertThat;

/**
 * AnnotationUtilsTest contains tests for {@link AnnotationUtils}.
 */
@Subject(AnnotationUtils.class)
public class AnnotationUtilsTest {
    @Test
    public void getMetaAnnotation__should_return_meta_annotation_or_null() throws Exception {
        assertThat("Existing meta impl?",                                                                                                       //
                AnnotationUtils.getMetaAnnotation(MetaAnnotation.class, WithMetaAnnotation.class.getAnnotation(UsingMetaAnnotation.class)),  //
                Matchers.is(Matchers.notNullValue()));

        assertThat("None Existing meta impl?",                                                                                                  //
                AnnotationUtils.getMetaAnnotation(MetaAnnotation.class, WithoutMetaAnnotation.class.getAnnotation(AnyOtherAnnotation.class)),           //
                Matchers.is(Matchers.nullValue()));
    }

    @Test
    public void resolveValueOfAnnotationAttribute_on_existing_attribute__should_return_correct_value() throws
        Exception {
        // arrange / given
        final Annotation annotation=TestFixtureForResolveValueOfAnnotationAttribute.class.getAnnotation(AnyOtherAnnotation.class);

        // act / when
        final String value=AnnotationUtils.resolveValueOfAnnotationAttribute(
                annotation,
                "name",
                String.class
            );

        // assert / then
        assertThat("The value of AnyOtherAnnotation.name()?", value, Matchers.is("The Name"));
    }

    @Test
    public void resolveValueOfAnnotationAttribute_on_not_existing_attribute__should_throw_an_exception() throws
        Exception {
        // arrange / given
        final Annotation annotation=TestFixtureForResolveValueOfAnnotationAttribute.class.getAnnotation(AnyOtherAnnotation.class);

        // act / when
        // assert / then
        ExceptionVerifier.on(
                ()->AnnotationUtils.resolveValueOfAnnotationAttribute(
                    annotation,
                    "do_not_exist",
                    String.class
                )
            )
            .expect(IllegalArgumentException.class)
            .expect("Can't access or resolve value of attribute 'do_not_exist()' of impl @AnyOtherAnnotation")
            .verify();
    }

    private static Method getMethod(Class<?> clazz) throws NoSuchMethodException {
        return clazz.getMethod("anyMethod");
    }

    @AnyOtherAnnotation(name = "The Name")
    private static class TestFixtureForResolveValueOfAnnotationAttribute {}

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