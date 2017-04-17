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

package org.failearly.dataz.internal.common.annotation.elementresolver;

import org.failearly.dataz.common.test.annotations.Subject;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.failearly.dataz.common.test.ExceptionVerifier.on;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * AnnotationElementResolverTest contains tests for ... .
 */
@Subject({AnnotationElementResolver.class, AnnotationElementResolvers.class})
public class AnnotationElementResolverTest {

    private static final String OUTER_TEST_CLASS = AnnotationElementResolverTest.class.getName();

    @Test
    public void correct_element_type__should_return_true_and_the_expected_value() throws Exception {
        // arrange / given
        final AnnotationElementResolver<String> resolver=AnnotationElementResolvers.createResolver(
            String.class, "name");

        // assert / then
        assertThat("name() exists?", resolver.hasElement(getAnnotation(SimpleNamedAnnotation.class)), is(true));
        assertThat("name() returns expected value?", resolver.resolveElementValue(getAnnotation(SimpleNamedAnnotation.class)), is("Other"));
    }

    @Test
    public void what_should_happen__on_missing_element() throws Exception {
        // arrange / given
        final AnnotationElementResolver<String> resolver=AnnotationElementResolvers.createResolver(
            String.class, "not_existing_element");

        // assert / then
        assertThat("not_existing_element() does not exist?", resolver.hasElement(getAnnotation(SimpleNamedAnnotation.class)), is(false));
        on(() -> resolver.resolveElementValue(getAnnotation(SimpleNamedAnnotation.class)))
            .expect(IllegalArgumentException.class)
            .expect(Matchers.startsWith("Element 'not_existing_element' not found on impl"))
            .verify();
    }

    @Test
    public void what_should_happend__on_existing_element_but_wrong_type() throws Exception {
        // arrange / given
        final AnnotationElementResolver<String> resolver=AnnotationElementResolvers.createResolver(
            String.class, "name");

        // assert / then
        assertThat("name() does not exist?", resolver.hasElement(getAnnotation(NameArrayAnnotation.class)), is(false));
        on(() -> resolver.resolveElementValue(getAnnotation(NameArrayAnnotation.class)))
            .expect(IllegalArgumentException.class)
            .expect(Matchers.startsWith("Element 'name' not found on impl"))
            .verify();
    }

    @Test
    public void weak_method_predicate__will_result_in_casting_exception() throws Exception {
        // arrange / given
        final AnnotationElementResolver<String> resolver=AnnotationElementResolvers.createResolver(
            String.class, "name", (Method m) -> true);

        // assert / then
        assertThat("name() exists, but ...", resolver.hasElement(getAnnotation(NameArrayAnnotation.class)), is(true));
        on(() -> resolver.resolveElementValue(getAnnotation(NameArrayAnnotation.class)))
            .expect(ClassCastException.class)
            .expect("Cannot cast [Ljava.lang.String; to java.lang.String")
            .verify();
    }

    @Test
    public void element_type_string_array__should_return_array() throws Exception {
        // arrange / given
        final AnnotationElementResolver<String[]> resolver=AnnotationElementResolvers.createResolver(
            String[].class, "name");

        // assert / then
        assertThat("name() exists?", resolver.hasElement(getAnnotation(NameArrayAnnotation.class)), is(true));
        assertThat("name() returns array?", resolver.resolveElementValue(getAnnotation(NameArrayAnnotation.class)), arrayContaining("name array"));
    }

    @Test
    public void resolve_annotation_arrays__should_return_annotation_objects() throws Exception {
        // arrange / given
        final AnnotationElementResolver<Annotation[]> resolver=AnnotationElementResolvers.createResolver(
            Annotation[].class, "value",
            (Method m) -> m.getReturnType().isArray() && m.getReturnType().getComponentType().isAnnotation()
        );

        // assert / then
        assertThat("value() exists?", resolver.hasElement(getAnnotation(NameArrayAnnotations.class)), is(true));
        assertThat("value() returns Annotations?", Arrays.toString(resolver.resolveElementValue(getAnnotation(NameArrayAnnotations.class))),
            is("[@"+ OUTER_TEST_CLASS +"$NameArrayAnnotation(name=[via NameArrayAnnotations])]"));
    }


    private Annotation getAnnotation(Class<? extends Annotation> annotationClass) {
        return TestFixture.class.getAnnotation(annotationClass);
    }

    @SimpleNamedAnnotation(name="Other")
    @NameArrayAnnotation
    @NameArrayAnnotations
    static class TestFixture {
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface NameArrayAnnotation {
        String[] name() default {"name array"};
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface NameArrayAnnotations {
        NameArrayAnnotation[] value() default {@NameArrayAnnotation(name="via NameArrayAnnotations")};
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface SimpleNamedAnnotation {
        String name() default "<must not not appear>";
    }

}