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

package org.failearly.dataset.internal.annotation.repeatable;

import org.failearly.dataset.internal.annotation.*;
import org.junit.Test;

import static org.failearly.dataset.test.CoreTestUtils.resolveMethodFromClass;

/**
 * RepeatableAnnotationTraversersTest contains tests for
 *      {@link AnnotationTraversers#createAnnotationTraverser(Class, TraverseStrategy, TraverseDepth)}.
 */
public class RepeatableAnnotation_TopDown_ClassHierarchy_TraversersTest extends AnnotationTraversersTestBase<RepeatableAnnotation> {

    private final AnnotationTraverser<RepeatableAnnotation> topDownClassHierarchyTraverser = AnnotationTraversers.createAnnotationTraverser(
            RepeatableAnnotation.class,
            TraverseStrategy.TOP_DOWN,
            TraverseDepth.CLASS_HIERARCHY
    );

    @Test
    public void traverse_on_simple_class_using_single_annotation__should_return_only_the_class_annotation() throws Exception {
        // act / when
        topDownClassHierarchyTraverser.traverse(UseOnlyOneAnnotation.class, testAnnotationHandler());

        // assert / then
        assertAnnotations("@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=UseOnlyOneAnnotation)");
    }

    @Test
    public void traverse_on_simple_method_using_single_annotation__should_return_only_the_method_and_class_annotations() throws Exception {
        // act / when
        topDownClassHierarchyTraverser.traverse(resolveMethodFromClass("anyMethod", UseOnlyOneAnnotation.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=UseOnlyOneAnnotation.anyMethod)",    //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=UseOnlyOneAnnotation)" //
        );
    }

    @Test
    public void traverse_on_simple_class_with_multiple_annotations__should_return_the_classes_annotations() throws Exception {
        // act / when
        topDownClassHierarchyTraverser.traverse(UseMultipleAnnotations.class, testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=UseMultipleAnnotations#1)",  //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=UseMultipleAnnotations#2)"   //
        );
    }

    @Test
    public void traverse_on_simple_method_with_multiple_annotations__should_return_the_method_and_classes_annotations() throws Exception {
        // act / when
        topDownClassHierarchyTraverser.traverse(resolveMethodFromClass("anyMethod", UseMultipleAnnotations.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=UseMultipleAnnotations.anyMethod#1)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=UseMultipleAnnotations.anyMethod#2)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=UseMultipleAnnotations#1)",  //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=UseMultipleAnnotations#2)"   //
        );
    }

    @Test
    public void traverse_on_class_hierarchy__should_return_class_hierarchy_annotations() throws Exception {
        // act / when
        topDownClassHierarchyTraverser.traverse(ClassHierarchy.class, testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=ClassHierarchy)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=SuperClass)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=BaseClass#1)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=BaseClass#2)"
        );
    }

    @Test
    public void traverse_on_method_within_class_hierarchy__should_return_first_method_and_then_class_hierarchy_annotations() throws Exception {
        // act / when
        topDownClassHierarchyTraverser.traverse(resolveMethodFromClass("anyMethod", ClassHierarchy.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=ClassHierarchy.anyMethod#1)",  //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=ClassHierarchy.anyMethod#2)",  //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=ClassHierarchy)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=SuperClass)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=BaseClass#1)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=BaseClass#2)"
        );
    }

    @Test
    public void traverse_on_inherited_method_within_class_hierarchy__should_return_first_method_s_and_then_declared_classes_annotations() throws Exception {
        // act / when
        topDownClassHierarchyTraverser.traverse(resolveMethodFromClass("inheritedMethod", ClassHierarchy.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=BaseClass#inheritedMethod)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=BaseClass#1)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=BaseClass#2)"
        );
    }
}
