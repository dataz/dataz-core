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

import org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation;
import org.failearly.dataset.internal.annotation.AnyOtherAnnotation;
import org.failearly.dataset.test.AssertException;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * AnnotationElementResolverTest contains tests for ... .
 */
public class AnnotationElementResolverTest {

    @Test
    public void existingAnnotationElement() throws Exception {
        // arrange / given
        final AnnotationElementResolver<String> resolver = AnnotationElementResolvers.createResolver(
                                String.class, "name");

        // assert / then
        assertThat("MyAnnotation.name() exists?", resolver.hasElement(getAnnotation(UsingMetaAnnotation.class)), is(true));
        assertThat("MyAnnotation.name() returns?", resolver.resolveElementValue(getAnnotation(UsingMetaAnnotation.class)), is("MyAnnotation"));
        assertThat("OtherAnnotation.name() exists?", resolver.hasElement(getAnnotation(AnyOtherAnnotation.class)), is(true));
        assertThat("OtherAnnotation.name() returns?", resolver.resolveElementValue(getAnnotation(AnyOtherAnnotation.class)), is("Other"));
        assertThat("NameArrayAnnotation.name() has wrong type?", resolver.hasElement(getAnnotation(NameArrayAnnotation.class)), is(false));
        AssertException.assertException(RuntimeException.class,
                "Invoke name() failed",
                () -> resolver.resolveElementValue(getAnnotation(NameArrayAnnotation.class)));
    }

    @Test
    public void existingAnnotationElement_weak_predicate() throws Exception {
        // arrange / given
        final AnnotationElementResolver<String> resolver = AnnotationElementResolvers.createResolver(
                                String.class, "name", (m) -> true);

        // assert / then
        assertThat("NameArrayAnnotation.name() correct?", resolver.hasElement(getAnnotation(NameArrayAnnotation.class)), is(true));
        AssertException.assertException(ClassCastException.class,
                "Cannot cast [Ljava.lang.String; to java.lang.String",
                () -> resolver.resolveElementValue(getAnnotation(NameArrayAnnotation.class)));
    }

    @Test
    public void existingAnnotationElement_string_array() throws Exception {
        // arrange / given
        final AnnotationElementResolver<String[]> resolver = AnnotationElementResolvers.createResolver(
                                String[].class, "name");

        // assert / then
        assertThat("NameArrayAnnotation.name() exists?", resolver.hasElement(getAnnotation(NameArrayAnnotation.class)), is(true));
        assertThat("NameArrayAnnotation.name() returns?", resolver.resolveElementValue(getAnnotation(NameArrayAnnotation.class)), arrayContaining("name array"));
    }

    @Test
    public void existingAnnotationElement_annotation_array() throws Exception {
        // arrange / given
        final AnnotationElementResolver<Annotation[]> resolver = AnnotationElementResolvers.createResolver(
                                Annotation[].class, "name",
                                (m) -> m.getReturnType().isArray() && m.getReturnType().getComponentType().isAnnotation()
                    );

        // assert / then
        assertThat("NameArrayAnnotations.value() exists?", resolver.hasElement(getAnnotation(NameArrayAnnotations.class)), is(true));
        assertThat("NameArrayAnnotations.value() returns?", Arrays.toString(resolver.resolveElementValue(getAnnotation(NameArrayAnnotations.class))),
                is("[@org.failearly.dataset.internal.annotation.invoker.AnnotationElementResolverTest$NameArrayAnnotation(name=[via NameArrayAnnotations])]"));
    }


    private Annotation getAnnotation(Class<? extends Annotation> annotationClass) {
        return TestFixture.class.getAnnotation(annotationClass);
    }

    private boolean valueFilter(Method method) {
        return false;
    }

    @UsingMetaAnnotation(name = "MyAnnotation")
    @AnyOtherAnnotation(name="Other")
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
        NameArrayAnnotation[] name() default {@NameArrayAnnotation(name="via NameArrayAnnotations")};
    }
}