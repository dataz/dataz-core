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

package org.failearly.dataset.internal.annotation.standard;

import org.failearly.dataset.internal.annotation.*;
import org.junit.Test;

import static org.failearly.dataset.test.TestUtils.resolveMethodFromClass;

/**
 * StandardAnnotationTraversersTest contains tests for
 *      {@link AnnotationTraversers#createAnnotationTraverser(Class, TraverseStrategy, TraverseDepth)}.
 */
public class StandardAnnotation_BottomUp_ClassHierarchy_TraversersTest extends AnnotationTraversersTestBase<StandardAnnotation> {

    private final AnnotationTraverser<StandardAnnotation> topDownClassHierarchyTraverser = AnnotationTraversers.createAnnotationTraverser(
            StandardAnnotation.class,
            TraverseStrategy.BOTTOM_UP,
            TraverseDepth.CLASS_HIERARCHY
    );

    @Test
    public void traverse_on_class_hierarchy__should_return_class_hierarchy_annotations() throws Exception {
        // act / when
        topDownClassHierarchyTraverser.traverse(ClassHierarchy.class, testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.standard.StandardAnnotation(name=BaseClass)", //
                "@org.failearly.dataset.internal.annotation.standard.StandardAnnotation(name=SuperClass)", //
                "@org.failearly.dataset.internal.annotation.standard.StandardAnnotation(name=ClassHierarchy)" //
        );
    }

    @Test
    public void traverse_on_method_within_class_hierarchy__should_return_first_method_and_then_class_hierarchy_annotations() throws Exception {
        // act / when
        topDownClassHierarchyTraverser.traverse(resolveMethodFromClass("anyMethod", ClassHierarchy.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.standard.StandardAnnotation(name=BaseClass)", //
                "@org.failearly.dataset.internal.annotation.standard.StandardAnnotation(name=SuperClass)", //
                "@org.failearly.dataset.internal.annotation.standard.StandardAnnotation(name=ClassHierarchy)", //
                "@org.failearly.dataset.internal.annotation.standard.StandardAnnotation(name=ClassHierarchy.anyMethod)"  //
        );
    }

    @Test
    public void traverse_on_inherited_method_within_class_hierarchy__should_return_first_method_s_and_then_declared_classes_annotations() throws Exception {
        // act / when
        topDownClassHierarchyTraverser.traverse(resolveMethodFromClass("inheritedMethod", ClassHierarchy.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.standard.StandardAnnotation(name=BaseClass)", //
                "@org.failearly.dataset.internal.annotation.standard.StandardAnnotation(name=BaseClass#inheritedMethod)" //
        );
    }
}
