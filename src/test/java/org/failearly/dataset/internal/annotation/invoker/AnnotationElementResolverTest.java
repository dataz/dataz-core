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

package org.failearly.dataset.internal.annotation.invoker;

import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.failearly.dataset.util.ExceptionVerifier.on;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * AnnotationElementResolverTest contains tests for ... .
 */
public class AnnotationElementResolverTest {

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
    public void not_existing_element__should_return_false_and_throw_an_exception() throws Exception {
        // arrange / given
        final AnnotationElementResolver<String> resolver=AnnotationElementResolvers.createResolver(
            String.class, "not_existing_element");

        // assert / then
        assertThat("not_existing_element() does not exist?", resolver.hasElement(getAnnotation(SimpleNamedAnnotation.class)), is(false));
        on(() -> resolver.resolveElementValue(getAnnotation(SimpleNamedAnnotation.class)))
            .expect(RuntimeException.class)
            .expect("Invoke not_existing_element() failed")
            .verify();
    }

    @Test
    public void incorrect_element_type__should_return_false_and_throw_an_exception() throws Exception {
        // arrange / given
        final AnnotationElementResolver<String> resolver=AnnotationElementResolvers.createResolver(
            String.class, "name");

        // assert / then
        assertThat("name() does not exist?", resolver.hasElement(getAnnotation(NameArrayAnnotation.class)), is(false));
        on(() -> resolver.resolveElementValue(getAnnotation(NameArrayAnnotation.class)))
            .expect(RuntimeException.class)
            .expect("Invoke name() failed")
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
            is("[@org.failearly.dataset.internal.annotation.invoker.AnnotationElementResolverTest$NameArrayAnnotation(name=[via NameArrayAnnotations])]"));
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