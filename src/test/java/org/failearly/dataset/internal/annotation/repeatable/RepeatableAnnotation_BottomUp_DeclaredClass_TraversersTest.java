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
 * RepeatableAnnotationTopDownDeclaredClassTraversersTest contains tests for
 *      {@link AnnotationTraversers#createAnnotationTraverser(Class, TraverseStrategy, TraverseDepth)}.
 */
public class RepeatableAnnotation_BottomUp_DeclaredClass_TraversersTest extends AnnotationTraversersTestBase<RepeatableAnnotation> {

    private final AnnotationTraverser<RepeatableAnnotation> bottomUpDeclaringClassTraverser = AnnotationTraversers.createAnnotationTraverser(
            RepeatableAnnotation.class,
            TraverseStrategy.BOTTOM_UP,
            TraverseDepth.DECLARED_CLASS  // << ONLY METHOD AND DECLARED/TOP LEVEL CLASSES
    );

    @Test
    public void traverse_on_class_hierarchy__should_return_only_top_level_class_annotations() throws Exception {
        // act / when
        bottomUpDeclaringClassTraverser.traverse(ClassHierarchy.class, testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=ClassHierarchy)"
        );
    }

    @Test
    public void traverse_on_method_within_class_hierarchy__should_return_first_declared_class_and_then_method_s_annotations() throws Exception {
        // act / when
        bottomUpDeclaringClassTraverser.traverse(resolveMethodFromClass("anyMethod", ClassHierarchy.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=ClassHierarchy)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=ClassHierarchy.anyMethod#1)",  //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=ClassHierarchy.anyMethod#2)"  //
        );
    }

    @Test
    public void traverse_on_inherited_method_within_class_hierarchy__should_return_first__declared_classes_annotations() throws Exception {
        // act / when
        bottomUpDeclaringClassTraverser.traverse(resolveMethodFromClass("inheritedMethod", ClassHierarchy.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=BaseClass#1)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=BaseClass#2)", //
                "@org.failearly.dataset.internal.annotation.repeatable.RepeatableAnnotation(name=BaseClass#inheritedMethod)" //
        );
    }
}
