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

import static org.failearly.dataset.test.TestUtils.resolveMethodFromClass;

/**
 * Contains tests for {@link AnnotationTraversers#createMetaAnnotationTraverser(Class, TraverseStrategy, TraverseDepth)}.
 */
public class MetaAnnotation_TopDown_MethodOnly_TraversersTest extends AnnotationTraversersTestBase<Annotation> {

    private final AnnotationTraverser<Annotation> topDownMethodOnlyTraverser = AnnotationTraversers.createMetaAnnotationTraverser(
            MetaAnnotation.class,
            TraverseStrategy.TOP_DOWN,
            TraverseDepth.METHOD_ONLY  // <<< METHOD ONLY, NO CLASSES AT ALL
    );

    @Test
    public void traverse_on_class_hierarchy__should_return_no_annotations() throws Exception {
        // act / when
        topDownMethodOnlyTraverser.traverse(ClassHierarchy.class, testAnnotationHandler());

        // assert / then
        assertNoAnnotations();
    }

    @Test
    public void traverse_on_method_within_class_hierarchy__should_return_only_method_annotations() throws Exception {
        // act / when
        topDownMethodOnlyTraverser.traverse(resolveMethodFromClass("anyMethod", ClassHierarchy.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=ClassHierarchy.anyMethod#1)",  //
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=ClassHierarchy.anyMethod#2)"
        );
    }

    @Test
    public void traverse_on_inherited_method_within_class_hierarchy__should_return_only_method_annotation() throws Exception {
        // act / when
        topDownMethodOnlyTraverser.traverse(resolveMethodFromClass("inheritedMethod", ClassHierarchy.class), testAnnotationHandler());

        // assert / then
        assertAnnotations(
                "@org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation(name=BaseClass#inheritedMethod)"
        );
    }
}
