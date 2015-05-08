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

import java.util.LinkedList;
import java.util.List;

import static org.failearly.dataset.internal.annotation.AnnotationTraversersTestUtils.createHandler;
import static org.failearly.dataset.test.TestUtils.resolveMethodFromClass;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * StandardAnnotationTraversersTest is responsible for ...
 */
public class StandardAnnotationTraversersTest {

    private final List<String> annotationNames = new LinkedList<>();
    private final AnnotationTraverser<AnnotationWithMetaAnnotation> standardTraverser = AnnotationTraversers.createAnnotationTraverser(
            AnnotationWithMetaAnnotation.class,
            TraverseStrategy.TOP_DOWN,
            Order.UNCHANGED);

    @Test
    public void traverseSimpleClass() throws Exception {
        // act / when
        standardTraverser.traverse(MySimpleClass.class, createHandler((annotation) -> annotationNames.add(annotation.name())));

        // assert / then
        assertThat("Annotations?", annotationNames, contains("OneAnnotation"));
    }

    @Test
    public void traverseSimpleClassAndMethod() throws Exception {
        // act / when
        standardTraverser.traverse(resolveMethodFromClass("anyMethod", MySimpleClass.class), createHandler((annotation) -> annotationNames.add(annotation.name())));

        // assert / then
        assertThat("Annotations?", annotationNames, contains("anyMethod", "OneAnnotation"));
    }

    @Test
    public void traverseClassHierarchy() throws Exception {
        // act / when
        standardTraverser.traverse(MyClass.class, createHandler((annotation) -> annotationNames.add(annotation.name())));

        // assert / then
        assertThat("Annotations?", annotationNames, contains("MyClass", "MyBaseClass1", "MyBaseClass2"));
    }

    @Test
    public void traverseClassHierarchyAndMethod() throws Exception {
        // act / when
        standardTraverser.traverse(resolveMethodFromClass("anyMethod", MyClass.class), createHandler((annotation) -> annotationNames.add(annotation.name())));

        // assert / then
        assertThat("Annotations?", annotationNames, contains("anyMethod1", "anyMethod2", "MyClass", "MyBaseClass1", "MyBaseClass2"));
    }

    @Test
    public void traverseClassHierarchyAndInheritedMethod() throws Exception {
        // act / when
        standardTraverser.traverse(resolveMethodFromClass("inheritedMethod", MyClass.class), createHandler((annotation) -> annotationNames.add(annotation.name())));

        // assert / then
        assertThat("Only annotations of method and declaring class?", annotationNames, contains("inheritedMethod", "MyBaseClass1", "MyBaseClass2"));
    }

    @Test
    public void traverseRepeatedAnnotations() throws Exception {
        // act / when
        standardTraverser.traverse(MyClassWithRepeatedAnnotations.class, createHandler((annotation) -> annotationNames.add(annotation.name())));

        // assert / then
        assertThat("Repeatable annotations?", annotationNames, contains("MyClassWithRepeatedAnnotations1", "MyClassWithRepeatedAnnotations2"));
    }

    @Test
    public void traverseRepeatedAnnotationOnMethod() throws Exception {
        // act / when
        standardTraverser.traverse(resolveMethodFromClass("anyMethod", MyClassWithRepeatedAnnotations.class), createHandler((annotation) -> annotationNames.add(annotation.name())));

        // assert / then
        assertThat("Repeatable annotations?", annotationNames, contains("anyMethod1", "anyMethod2", "MyClassWithRepeatedAnnotations1", "MyClassWithRepeatedAnnotations2"));
    }

    @Test
    public void traverseNoneRepeatableAnnotation() throws Exception {
        // arrange / given
        AnnotationTraverser<NoMetaAnnotation> traverser = AnnotationTraversers.createAnnotationTraverser(NoMetaAnnotation.class, TraverseStrategy.TOP_DOWN, Order.UNCHANGED);

        // act / when
        traverser.traverse(resolveMethodFromClass("anyMethod", MySimpleClass.class), createHandler((annotation) -> annotationNames.add(annotation.name())));

        // assert / then
        assertThat("Repeatable annotations?", annotationNames, contains("anyMethod", "OneAnnotation"));
    }

    @Test
    public void traverseRepeatedAnnotationOnMethod_reverted() throws Exception {
        // arrange / given
        AnnotationTraverser<AnnotationWithMetaAnnotation> traverser = AnnotationTraversers.createAnnotationTraverser(
                AnnotationWithMetaAnnotation.class, TraverseStrategy.TOP_DOWN, Order.REVERTED);

        // act / when
        traverser.traverse(resolveMethodFromClass("anyMethod", MyClassWithRepeatedAnnotations.class), createHandler((annotation) -> annotationNames.add(annotation.name())));

        // assert / then
        assertThat("Repeatable annotations (in reverse order)?", annotationNames,
                contains("MyClassWithRepeatedAnnotations2", "MyClassWithRepeatedAnnotations1", "anyMethod2", "anyMethod1"));
    }

    @Test
    public void traverseRepeatedAnnotationOnMethod_bottom_up() throws Exception {
        // arrange / given
        AnnotationTraverser<AnnotationWithMetaAnnotation> traverser = AnnotationTraversers.createAnnotationTraverser(
                AnnotationWithMetaAnnotation.class, TraverseStrategy.BOTTOM_UP, Order.UNCHANGED);

        // act / when
        traverser.traverse(resolveMethodFromClass("anyMethod", MyClass.class), createHandler((annotation) -> annotationNames.add(annotation.name())));

        // assert / then
        assertThat("Repeatable annotations (bottom up/unchanged order)?", annotationNames,
                contains("MyBaseClass1", "MyBaseClass2", "MyClass", "anyMethod1", "anyMethod2"));
    }


    @AnnotationWithMetaAnnotation(name = "OneAnnotation")
    @NoMetaAnnotation(name = "OneAnnotation")
    static class MySimpleClass {
        @AnnotationWithMetaAnnotation(name = "anyMethod")
        @NoMetaAnnotation(name = "anyMethod")
        public void anyMethod() {
        }
    }

    @AnnotationWithMetaAnnotation(name = "MyBaseClass1")
    @AnnotationWithMetaAnnotation(name = "MyBaseClass2")
    @NoMetaAnnotation
    abstract static class MyBaseClass {
        @AnnotationWithMetaAnnotation(name = "inheritedMethod")
        @NoMetaAnnotation
        public void inheritedMethod() {
        }
    }

    private static class MyMiddleClass extends MyBaseClass {
    }

    @AnnotationWithMetaAnnotation(name = "MyClass")
    @NoMetaAnnotation
    static class MyClass extends MyMiddleClass {
        @AnnotationWithMetaAnnotation(name = "anyMethod1")
        @AnnotationWithMetaAnnotation(name = "anyMethod2")
        @NoMetaAnnotation
        public void anyMethod() {
        }
    }

    @AnnotationWithMetaAnnotation(name = "MyClassWithRepeatedAnnotations1")
    @AnnotationWithMetaAnnotation(name = "MyClassWithRepeatedAnnotations2")
    @NoMetaAnnotation
    static class MyClassWithRepeatedAnnotations {
        @AnnotationWithMetaAnnotation(name = "anyMethod1")
        @AnnotationWithMetaAnnotation(name = "anyMethod2")
        @NoMetaAnnotation
        public void anyMethod() {
        }
    }


}
