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

package org.failearly.dataset.internal.annotation;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

import static org.failearly.dataset.internal.annotation.AnnotationTraversersTestUtils.createHandler;
import static org.failearly.dataset.test.TestUtils.resolveMethodFromClass;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * MetaAnnotationTraversersTest contains tests for ... .
 */
public class MetaAnnotationTraversersTest {

    private final List<String> annotations = new LinkedList<>();
    private final AnnotationTraverser<Annotation> standardTraverser = AnnotationTraversers.createMetaAnnotationTraverser(
                                                            MetaAnnotation.class,
                                                            TraverseStrategy.TOP_DOWN,
                                                            Order.UNCHANGED
                                                        );

    @Test
    public void traverseOneAnnotation() throws Exception {
        // act / when
        standardTraverser.traverse(OneAnnotation.class, createHandler(
                        (annotation) -> annotations.add(annotation.toString())
                )
        );

        // assert / then
        assertThat("Annotations?", annotations, contains("@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=OneAnnotation)"));
    }

    @Test
    public void traverseOneAnnotationAndMethod() throws Exception {
        // act / when
        standardTraverser.traverse(resolveMethodFromClass("anyMethod", OneAnnotation.class), createHandler(
                        (annotation) -> annotations.add(annotation.toString()))
        );

        // assert / then
        assertThat("Annotations?", annotations, contains(
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=anyMethod)",
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=OneAnnotation)"
        ));
    }


    @Test
    public void traverseRepeatedAnnotations() throws Exception {
        // act / when
        standardTraverser.traverse(RepeatedAnnotations.class, createHandler(
                        (annotation) -> annotations.add(annotation.toString())
                )
        );

        // assert / then
        assertThat("Annotations?", annotations, contains(
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=RepeatedAnnotations1)",
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=RepeatedAnnotations2)"
        ));
    }

    @Test
    public void traverseRepeatedAnnotationsAndMethod() throws Exception {
        // act / when
        standardTraverser.traverse(resolveMethodFromClass("anyMethod", RepeatedAnnotations.class), createHandler(
                        (annotation) -> annotations.add(annotation.toString())
                )
        );

        // assert / then
        assertThat("Annotations?", annotations, contains(
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=anyMethod1)",
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=anyMethod2)",
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=RepeatedAnnotations1)",
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=RepeatedAnnotations2)"
        ));
    }

    @Test
    public void traverseBottomUp() throws Exception {
        // arrange / given
        final AnnotationTraverser<Annotation> traverser = AnnotationTraversers.createMetaAnnotationTraverser(
                MetaAnnotation.class,
                TraverseStrategy.BOTTOM_UP,
                Order.UNCHANGED
        );

        // act / when
        traverser.traverse(resolveMethodFromClass("anyMethod", RepeatedAnnotations.class), createHandler(
                        (annotation) -> annotations.add(annotation.toString())
                )
        );

        // assert / then
        assertThat("Annotations?", annotations, contains(
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=RepeatedAnnotations1)",
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=RepeatedAnnotations2)",
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=anyMethod1)",
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=anyMethod2)"
        ));
    }

    @Test
    public void traverseReverted() throws Exception {
        // arrange / given
        final AnnotationTraverser<Annotation> traverser = AnnotationTraversers.createMetaAnnotationTraverser(
                MetaAnnotation.class,
                TraverseStrategy.TOP_DOWN,
                Order.REVERTED
        );

        // act / when
        traverser.traverse(resolveMethodFromClass("anyMethod", RepeatedAnnotations.class), createHandler(
                        (annotation) -> annotations.add(annotation.toString())
                )
        );

        // assert / then
        assertThat("Annotations?", annotations, contains(
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=RepeatedAnnotations2)",
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=RepeatedAnnotations1)",
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=anyMethod2)",
                "@org.failearly.dataset.internal.annotation.AnnotationWithMetaAnnotation(name=anyMethod1)"
        ));
    }

    @AnnotationWithMetaAnnotation(name = "OneAnnotation")
    @NoMetaAnnotation(name = "OneAnnotation")
    static class OneAnnotation {
        @AnnotationWithMetaAnnotation(name = "anyMethod")
        @NoMetaAnnotation
        public void anyMethod() {
        }
    }

    @AnnotationWithMetaAnnotation(name = "RepeatedAnnotations1")
    @AnnotationWithMetaAnnotation(name = "RepeatedAnnotations2")
    @NoMetaAnnotation
    static class RepeatedAnnotations {
        @AnnotationWithMetaAnnotation(name = "anyMethod1")
        @AnnotationWithMetaAnnotation(name = "anyMethod2")
        @NoMetaAnnotation
        public void anyMethod() {
        }
    }


}