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
@Title("Application of annotationTraverser on repeatable impl")
public class RepeatableAnnotationTraverserSpec extends AnnotationTraverserSpecBase<RepeatableAnnotation> {


    @Unroll("Which annotations should be collected using (#traverseStrategy, #traverseDepth), applied on class '#traverseeClass.simpleName'?")
    def "Which annotations should be collected on class objects?"() {
        given: "A traverser for repeatable annotations (use of Java8 @Repeatable)"
        def traverser = annotationTraverser(RepeatableAnnotation)
                .withTraverseStrategy(traverseStrategy)
                .withTraverseDepth(traverseDepth)
                .build()

        and: "the test impl handler"
        def collectedAnnotations = []
        def annotationHandler = createAnnotationHandler(collectedAnnotations)

        when: "traversing #traverseeClass.simpleName"
        //noinspection GroovyAssignabilityCheck
        traverser.traverse(traverseeClass, annotationHandler)

        then: "the collected annotations should in the expected order"
        collectedAnnotations == expected(RepeatableAnnotation, expectedAnnotationIds)

        and: "have expected impl type"
        haveExpectedAnnotationTypes(collectedAnnotations, RepeatableAnnotation)

        where:
        traverseeClass                                                            | traverseStrategy | traverseDepth   || expectedAnnotationIds // list of @RepeatableAnnotation(id=???)
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | TOP_DOWN         | HIERARCHY       || ["TFC1", "TFC2", "MC1", "BC1", "BC2", "IF11", "IF12", "IF01", "IF02"]
        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | TOP_DOWN         | HIERARCHY       || ["MC1", "BC1", "BC2", "IF11", "IF12", "IF01", "IF02"]
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | TOP_DOWN         | HIERARCHY       || ["BC1", "BC2", "IF11", "IF12", "IF01", "IF02"]
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | TOP_DOWN         | HIERARCHY       || ["IF11", "IF12", "IF01", "IF02"]

        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | TOP_DOWN         | CLASS_HIERARCHY || ["TFC1", "TFC2", "MC1", "BC1", "BC2"]
        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | TOP_DOWN         | CLASS_HIERARCHY || ["MC1", "BC1", "BC2"]
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | TOP_DOWN         | CLASS_HIERARCHY || ["BC1", "BC2"]
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | TOP_DOWN         | CLASS_HIERARCHY || ["IF11", "IF12"]

        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | TOP_DOWN         | DECLARED_CLASS  || ["TFC1", "TFC2"]
        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | TOP_DOWN         | DECLARED_CLASS  || ["MC1"]
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | TOP_DOWN         | DECLARED_CLASS  || ["BC1", "BC2"]
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | TOP_DOWN         | DECLARED_CLASS  || ["IF11", "IF12"]

        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | TOP_DOWN         | METHOD_ONLY     || NO_ANNOTATIONS

        GroovyClass                                                               | TOP_DOWN         | CLASS_HIERARCHY || ["GC1", "GC2", "BC1", "BC2"]

        // Just for testing the BOTTOM_UP strategy. See the first test.
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | BOTTOM_UP        | HIERARCHY       || ["IF01", "IF02", "IF11", "IF12", "BC1", "BC2", "MC1", "TFC1", "TFC2"]
    }


    @Unroll("Which annotations should be collected using #traverseDepth strategy, applied on method '#method' (#clazz.simpleName)?")
    def "Which annotations should be collected on method objects?"() {
        given: "Building a BOTTOM_UP traverser for repeatable annotations"
        AnnotationTraverser<RepeatableAnnotation> traverser = annotationTraverser(RepeatableAnnotation)
                .withTraverseStrategy(BOTTOM_UP)
                .withTraverseDepth(traverseDepth)
                .build()

        and: "creating a collection impl handler"
        def collectedAnnotations = []
        def annotationHandler = createAnnotationHandler(collectedAnnotations)

        when: "traversing starting with given #method.name"
        //noinspection GroovyAssignabilityCheck
        traverser.traverse(resolveMethodFromClass(method, clazz), annotationHandler)

        then: "the collected annotations should in the expected order"
        collectedAnnotations == expected(RepeatableAnnotation, expectedAnnotationIds)

        and: "have expected impl type"
        haveExpectedAnnotationTypes(collectedAnnotations, RepeatableAnnotation)

        where:
        clazz                                                                     | method                     | traverseDepth   || expectedAnnotationIds // list of @RepeatableAnnotation(id=???)
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "declaredInstanceMethod"   | HIERARCHY       || ["IF01", "IF02", "IF11", "IF12", "BC1", "BC2", "MC1", "TFC1", "TFC2", "dim1", "dim2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "declaredInstanceMethod"   | CLASS_HIERARCHY || ["BC1", "BC2", "MC1", "TFC1", "TFC2", "dim1", "dim2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "declaredInstanceMethod"   | DECLARED_CLASS  || ["TFC1", "TFC2", "dim1", "dim2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "declaredInstanceMethod"   | METHOD_ONLY     || ["dim1", "dim2"]

        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "inheritedMethod"          | HIERARCHY       || ["IF01", "IF02", "IF11", "IF12", "BC1", "BC2", "im1", "im2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "inheritedMethod"          | CLASS_HIERARCHY || ["BC1", "BC2", "im1", "im2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "inheritedMethod"          | DECLARED_CLASS  || ["BC1", "BC2", "im1", "im2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "inheritedMethod"          | METHOD_ONLY     || ["im1", "im2"]

        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "overriddenInstanceMethod" | HIERARCHY       || ["IF01", "IF02", "IF11", "IF12", "BC1", "BC2", "MC1", "oim1", "oim2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "overriddenInstanceMethod" | CLASS_HIERARCHY || ["BC1", "BC2", "MC1", "oim1", "oim2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "overriddenInstanceMethod" | DECLARED_CLASS  || ["MC1", "oim1", "oim2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "overriddenInstanceMethod" | METHOD_ONLY     || ["oim1", "oim2"]

        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "classMethod"              | HIERARCHY       || ["IF01", "IF02", "IF11", "IF12", "BC1", "BC2", "MC1", "cm1", "cm2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "classMethod"              | CLASS_HIERARCHY || ["BC1", "BC2", "MC1", "cm1", "cm2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "classMethod"              | DECLARED_CLASS  || ["MC1", "cm1", "cm2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "classMethod"              | METHOD_ONLY     || ["cm1", "cm2"]

        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "interfaceMethod"          | HIERARCHY       || ["IF01", "IF02", "IF11", "IF12", "ifm1", "ifm2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "interfaceMethod"          | CLASS_HIERARCHY || ["IF11", "IF12", "ifm1", "ifm2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "interfaceMethod"          | DECLARED_CLASS  || ["IF11", "IF12", "ifm1", "ifm2"]
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | "interfaceMethod"          | METHOD_ONLY     || ["ifm1", "ifm2"]

        GroovyClass                                                               | "any groovy method"        | HIERARCHY       || ["IF01", "IF02", "IF11", "IF12", "BC1", "BC2", "GC1", "GC2", "gm1", "gm2"]
        GroovyClass                                                               | "any groovy method"        | CLASS_HIERARCHY || ["BC1", "BC2", "GC1", "GC2", "gm1", "gm2"]
        GroovyClass                                                               | "any groovy method"        | DECLARED_CLASS  || ["GC1", "GC2", "gm1", "gm2"]
        GroovyClass                                                               | "any groovy method"        | METHOD_ONLY     || ["gm1", "gm2"]
    }

    @RepeatableAnnotation.List([@RepeatableAnnotation(id = "GC1"), @RepeatableAnnotation(id = "GC2")])
    private static class GroovyClass extends BaseClass {
        @RepeatableAnnotation.List([@RepeatableAnnotation(id = "gm1"), @RepeatableAnnotation(id = "gm2")])
        def "any groovy method"() {}
    }
}