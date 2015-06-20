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

package org.failearly.dataset.internal.annotation.meta;

import org.failearly.dataset.internal.annotation.*;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.failearly.dataset.test.CoreTestUtils.resolveMethodFromClass;

/**
 * Contains tests for {@link AnnotationTraversers#createMetaAnnotationTraverser(Class, TraverseStrategy, TraverseDepth)}.
 */
public class MetaAnnotation_BottomUp_ClassHierarchy_TraversersTest extends AnnotationTraversersTestBase<Annotation> {

    private final AnnotationTraverser<Annotation> bottomUpClassHierarchyTraverser = AnnotationTraversers.createMetaAnnotationTraverser(
            MetaAnnotation.class,
            TraverseStrategy.BOTTOM_UP,
            TraverseDepth.CLASS_HIERARCHY
    );


    @Test
    public void traverse_on_simple_method_with_multiple_annotations__should_return_first_the_classes_and_then_the_method_s_annotations() throws Exception {
        // act / when
        bottomUpClassHierarchyTraverser.traverse(resolveMethodFromClass("anyMethod", UseMultipleAnnotations.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=UseMultipleAnnotations#1)",  //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=UseMultipleAnnotations#2)",   //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=UseMultipleAnnotations.anyMethod#1)", //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=UseMultipleAnnotations.anyMethod#2)"
        );
    }

    @Test
    public void traverse_on_class_hierarchy__should_return_class_hierarchy_annotations() throws Exception {
        // act / when
        bottomUpClassHierarchyTraverser.traverse(ClassHierarchy.class, testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=BaseClass#1)", //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=BaseClass#2)", //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=SuperClass)", //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=ClassHierarchy)" //
        );
    }

    @Test
    public void traverse_on_method_within_class_hierarchy__should_return_first_the_class_hierarchy_and_then_the_method_s_annotations() throws Exception {
        // act / when
        bottomUpClassHierarchyTraverser.traverse(resolveMethodFromClass("anyMethod", ClassHierarchy.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=BaseClass#1)", //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=BaseClass#2)",
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=SuperClass)", //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=ClassHierarchy)", //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=ClassHierarchy.anyMethod#1)",  //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=ClassHierarchy.anyMethod#2)"
        );
    }

    @Test
    public void traverse_on_inherited_method_within_class_hierarchy__should_return_first_the_declared_class_and_then_the_method_s_annotations() throws Exception {
        // act / when
        bottomUpClassHierarchyTraverser.traverse(resolveMethodFromClass("inheritedMethod", ClassHierarchy.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=BaseClass#1)", //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=BaseClass#2)",
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=BaseClass#inheritedMethod)"
        );
    }
}
