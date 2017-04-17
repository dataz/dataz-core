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


package org.failearly.dataz.internal.common.annotation.traverser

import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import static AnnotationTraverserBuilder.annotationTraverser
import static TraverseDepth.*
import static TraverseStrategy.BOTTOM_UP
import static TraverseStrategy.TOP_DOWN
import static org.failearly.dataz.common.test.utils.ReflectionUtils.resolveMethodFromClass
/**
 * RepeatableAnnotationTraverserSpec provides tests for {@link AnnotationTraverserBuilder}.
 */
@Subject(AnnotationTraverserBuilder)
@Title("Application of annotationTraverser on (standard) impl")
public class StandardAnnotationTraverserSpec extends AnnotationTraverserSpecBase<StandardAnnotation> {
    @Unroll("Which annotations should be collected using (#traverseStrategy, #traverseDepth), applied on class '#traverseeClass.simpleName'?")
    def "Which annotations should be collected on class objects?"() {
        given: "Building a traverser for standard annotations"
        AnnotationTraverser<StandardAnnotation> traverser = annotationTraverser(StandardAnnotation)
                .withTraverseStrategy(traverseStrategy)
                .withTraverseDepth(traverseDepth)
                .build()

        and: "creating a collection impl handler"
        def collectedAnnotations = []
        def annotationHandler = createAnnotationHandler(collectedAnnotations)

        when: "traversing starting with #traverseeClass"
        //noinspection GroovyAssignabilityCheck
        traverser.traverse(traverseeClass, annotationHandler)

        then: "the collected annotations should in the expected order"
        collectedAnnotations == expected(StandardAnnotation, expectedAnnotationIds)

        and: "have expected impl type"
        haveExpectedAnnotationTypes(collectedAnnotations, StandardAnnotation)


        where:
        traverseeClass                                                            | traverseStrategy | traverseDepth   || expectedAnnotationIds // list of @StandardAnnotation(id=???)
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | BOTTOM_UP        | HIERARCHY       || ["IF0", "IF1", "BC", "MC", "TFC"]
        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | BOTTOM_UP        | HIERARCHY       || ["IF0", "IF1", "BC", "MC"]
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | BOTTOM_UP        | HIERARCHY       || ["IF0", "IF1", "BC"]
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | BOTTOM_UP        | HIERARCHY       || ["IF0", "IF1"]

        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | BOTTOM_UP        | CLASS_HIERARCHY || ["BC", "MC", "TFC"]
        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | BOTTOM_UP        | CLASS_HIERARCHY || ["BC", "MC"]
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | BOTTOM_UP        | CLASS_HIERARCHY || ["BC"]
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | BOTTOM_UP        | CLASS_HIERARCHY || ["IF1"]

        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | BOTTOM_UP        | DECLARED_CLASS  || ["TFC"]
        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | BOTTOM_UP        | DECLARED_CLASS  || ["MC"]
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | BOTTOM_UP        | DECLARED_CLASS  || ["BC"]
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | BOTTOM_UP        | DECLARED_CLASS  || ["IF1"]

        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | BOTTOM_UP        | METHOD_ONLY     || NO_ANNOTATIONS

        GroovyClass                                                               | BOTTOM_UP        | CLASS_HIERARCHY || ["GC"]

        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | TOP_DOWN         | HIERARCHY       || ["IF0", "IF1", "BC", "MC", "TFC"].reverse()
    }

    @Unroll("Which annotations should be collected using (#traverseStrategy, #traverseDepth), applied on method '#method'?")
    def "Which annotations should be collected on method objects?"() {
        given: "Building a traverser for standard annotations"
        def traverser = annotationTraverser(StandardAnnotation)
                .withTraverseStrategy(traverseStrategy)
                .withTraverseDepth(traverseDepth)
                .build()

        and: "creating a collection impl handler"
        def collectedAnnotations = []
        def annotationHandler = createAnnotationHandler(collectedAnnotations)

        when: "traversing starting with given #method"
        //noinspection GroovyAssignabilityCheck
        traverser.traverse(resolveMethodFromClass(method, org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass), annotationHandler)

        then: "the collected annotations should in the expected order"
        collectedAnnotations == expected(StandardAnnotation, expectedAnnotationIds)

        and: "have expected impl type"
        haveExpectedAnnotationTypes(collectedAnnotations, StandardAnnotation)

        where:
        method                     | traverseStrategy | traverseDepth   || expectedAnnotationIds // list of @StandardAnnotation(id=???)
        "declaredInstanceMethod"   | TOP_DOWN         | HIERARCHY       || ["dim", "TFC", "MC", "BC", "IF1", "IF0"]
        "declaredInstanceMethod"   | TOP_DOWN         | CLASS_HIERARCHY || ["dim", "TFC", "MC", "BC"]
        "declaredInstanceMethod"   | TOP_DOWN         | DECLARED_CLASS  || ["dim", "TFC"]
        "declaredInstanceMethod"   | TOP_DOWN         | METHOD_ONLY     || ["dim"]

        "inheritedMethod"          | TOP_DOWN         | HIERARCHY       || ["im", "BC", "IF1", "IF0"]
        "inheritedMethod"          | TOP_DOWN         | CLASS_HIERARCHY || ["im", "BC"]
        "inheritedMethod"          | TOP_DOWN         | DECLARED_CLASS  || ["im", "BC"]
        "inheritedMethod"          | TOP_DOWN         | METHOD_ONLY     || ["im"]

        "overriddenInstanceMethod" | TOP_DOWN         | HIERARCHY       || ["oim", "MC", "BC", "IF1", "IF0"]
        "overriddenInstanceMethod" | TOP_DOWN         | CLASS_HIERARCHY || ["oim", "MC", "BC"]
        "overriddenInstanceMethod" | TOP_DOWN         | DECLARED_CLASS  || ["oim", "MC"]
        "overriddenInstanceMethod" | TOP_DOWN         | METHOD_ONLY     || ["oim"]

        "classMethod"              | TOP_DOWN         | HIERARCHY       || ["cm", "MC", "BC", "IF1", "IF0"]
        "classMethod"              | TOP_DOWN         | CLASS_HIERARCHY || ["cm", "MC", "BC"]
        "classMethod"              | TOP_DOWN         | DECLARED_CLASS  || ["cm", "MC"]
        "classMethod"              | TOP_DOWN         | METHOD_ONLY     || ["cm"]

        "interfaceMethod"          | TOP_DOWN         | HIERARCHY       || ["ifm", "IF1", "IF0"]
        "interfaceMethod"          | TOP_DOWN         | CLASS_HIERARCHY || ["ifm", "IF1"]
        "interfaceMethod"          | TOP_DOWN         | DECLARED_CLASS  || ["ifm", "IF1"]
        "interfaceMethod"          | TOP_DOWN         | METHOD_ONLY     || ["ifm"]

        "declaredInstanceMethod"   | BOTTOM_UP        | HIERARCHY       || ["dim", "TFC", "MC", "BC", "IF1", "IF0"].reverse()
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    @Unroll("How to check if an impl (#annotation) is available (starting with #method)?")
    def "How to check if an annotation is available?"() {
        given: "Two standard traversers (depth=#traverseDepth)"
        final topDownTraverser = annotationTraverser(annotation)
                .withTraverseStrategy(TOP_DOWN)
                .withTraverseDepth(traverseDepth)
                .build();
        final bottomUpTraverser = annotationTraverser(annotation)
                .withTraverseStrategy(BOTTOM_UP)
                .withTraverseDepth(traverseDepth)
                .build();

        and: "a method object (#method)"
        final methodObject = resolveMethodFromClass(method, org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass)

        when: "checking with both traversers"
        final topDownAvailable=topDownTraverser.anyAnnotationAvailable(methodObject)
        final bottomUpAvailable=bottomUpTraverser.anyAnnotationAvailable(methodObject)


        then: "it should be available or not (depending on depth and impl)"
        topDownAvailable == expected

        and: "it should be independent from the strategy (BOTTOM_UP/TOP_DOWN)"
        topDownAvailable == bottomUpAvailable

        where:
        annotation         | method          | traverseDepth   || expected
        StandardAnnotation | "noAnnotations" | HIERARCHY       || true
        StandardAnnotation | "noAnnotations" | CLASS_HIERARCHY || true
        StandardAnnotation | "noAnnotations" | DECLARED_CLASS  || true
        StandardAnnotation | "noAnnotations" | METHOD_ONLY     || false
        NotUsedAnnotation  | "noAnnotations" | HIERARCHY       || false
        NotUsedAnnotation  | "noAnnotations" | METHOD_ONLY     || false
    }

    @StandardAnnotation(id = "GC")
    private static class GroovyClass {}


}