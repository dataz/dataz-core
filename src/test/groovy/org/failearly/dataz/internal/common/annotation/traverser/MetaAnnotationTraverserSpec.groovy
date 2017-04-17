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

import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotation as MA
import org.failearly.dataz.internal.common.annotation.traverser.OtherAnnotation as OA
import org.failearly.dataz.internal.common.annotation.traverser.StandardAnnotation as SA
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import static AnnotationTraverserBuilder.metaAnnotationTraverser
import static TraverseDepth.*
import static TraverseStrategy.BOTTOM_UP
import static TraverseStrategy.TOP_DOWN
import static org.failearly.dataz.common.test.utils.ReflectionUtils.resolveMethodFromClass
/**
 * MetaAnnotationTraverserSpec provides tests for {@link AnnotationTraverserBuilder#metaAnnotationTraverser(Class)}.
 */
@Subject([AnnotationTraverserBuilder, MetaAnnotation])
@Unroll
@Title("Application of metaAnnotationTraverser")
public class MetaAnnotationTraverserSpec extends AnnotationTraverserSpecBase<MA> {

    def "Which annotations should be collected using a #traverseDepth/#traverseStrategy traverser applied on class '#clazz.simpleName'?"() {
        given: "A meta impl traverser (strategy=#traverseStrategy, depth=#traverseDepth)"
        MetaAnnotationTraverser<MA> traverser = metaAnnotationTraverser(MA)
                .withTraverseStrategy(traverseStrategy)
                .withTraverseDepth(traverseDepth)
                .build()

        and: "a meta impl handler (collecting the impl and meta impl)"
        def collectedAnnotations = []
        def collectedMetaAnnotations = []
        def annotationHandler = createMetaAnnotationHandler(MA, collectedAnnotations, collectedMetaAnnotations)

        when: "traversing class '#clazz.simpleName'"
        traverser.traverse(clazz, annotationHandler)

        then: "the annotations should be collected in the expected order"
        collectedAnnotations == expectedAnnotations

        then: "the meta annotations should be collected in the expected order"
        collectedMetaAnnotations == expectedMetaAnnotations

        and: "have expected impl type"
        haveExpectedAnnotationTypes(collectedAnnotations, SA, OA)

        where:
        clazz                                                                     | traverseDepth   | traverseStrategy || expectedAnnotations                                            | expectedMetaAnnotations
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | HIERARCHY       | TOP_DOWN         || ann("TFC", "O-TFC", "MC", "O-BC", "BC", "IF1", "IF0", "O-IF0") | ma("SA", "OA", "SA", "OA") + ma("SA", 3) + ma("OA")
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | HIERARCHY       | BOTTOM_UP        || ann("IF0", "O-IF0", "IF1", "O-BC", "BC", "MC", "TFC", "O-TFC") | ma("SA", "OA", "SA", "OA") + ma("SA", 3) + ma("OA")
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | CLASS_HIERARCHY | TOP_DOWN         || ann("TFC", "O-TFC", "MC", "O-BC", "BC")                        | ma("SA", "OA", "SA", "OA", "SA")
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | CLASS_HIERARCHY | BOTTOM_UP        || ann("O-BC", "BC", "MC", "TFC", "O-TFC")                        | ma("OA") + ma("SA", 3) + ma("OA")
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | DECLARED_CLASS  | TOP_DOWN         || ann("TFC", "O-TFC")                                            | ma("SA", "OA")
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | DECLARED_CLASS  | BOTTOM_UP        || ann("TFC", "O-TFC")                                            | ma("SA", "OA")

        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | HIERARCHY       | TOP_DOWN         || ann("MC", "O-BC", "BC", "IF1", "IF0", "O-IF0")                 | ma("SA", "OA") + ma("SA", 3) + ma("OA")
        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | HIERARCHY       | BOTTOM_UP        || ann("IF0", "O-IF0", "IF1", "O-BC", "BC", "MC")                 | ma("SA", "OA", "SA", "OA") + ma("SA", 2)
        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | CLASS_HIERARCHY | TOP_DOWN         || ann("MC", "O-BC", "BC")                                        | ma("SA", "OA", "SA")
        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | CLASS_HIERARCHY | BOTTOM_UP        || ann("O-BC", "BC", "MC")                                        | ma("OA") + ma("SA", 2)
        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | DECLARED_CLASS  | TOP_DOWN         || ann("MC")                                                      | ma("SA")
        org.failearly.dataz.internal.common.annotation.traverser.MidClass         | DECLARED_CLASS  | BOTTOM_UP        || ann("MC")                                                      | ma("SA")

        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | HIERARCHY       | TOP_DOWN         || ann("O-BC", "BC", "IF1", "IF0", "O-IF0")                       | ma("OA") + ma("SA", 3) + ma("OA")
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | HIERARCHY       | BOTTOM_UP        || ann("IF0", "O-IF0", "IF1", "O-BC", "BC")                       | ma("SA", "OA", "SA", "OA") + ma("SA")
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | CLASS_HIERARCHY | TOP_DOWN         || ann("O-BC", "BC")                                              | ma("OA", "SA")
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | CLASS_HIERARCHY | BOTTOM_UP        || ann("O-BC", "BC")                                              | ma("OA", "SA")
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | DECLARED_CLASS  | TOP_DOWN         || ann("O-BC", "BC")                                              | ma("OA", "SA")
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | DECLARED_CLASS  | BOTTOM_UP        || ann("O-BC", "BC")                                              | ma("OA", "SA")

        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | HIERARCHY       | TOP_DOWN         || ann("IF1", "IF0", "O-IF0")                                     | ma("SA", 2) + ma("OA")
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | HIERARCHY       | BOTTOM_UP        || ann("IF0", "O-IF0", "IF1")                                     | ma("SA", "OA", "SA")
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | CLASS_HIERARCHY | TOP_DOWN         || ann("IF1")                                                     | ma("SA")
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | CLASS_HIERARCHY | BOTTOM_UP        || ann("IF1")                                                     | ma("SA")
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | DECLARED_CLASS  | TOP_DOWN         || ann("IF1")                                                     | ma("SA")
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | DECLARED_CLASS  | BOTTOM_UP        || ann("IF1")                                                     | ma("SA")
    }

    def "Which annotations should be collected using a #traverseDepth/#traverseStrategy traverser applied on method '#method'?"() {
        given: "A meta impl traverser (strategy=#traverseStrategy, depth=#traverseDepth)"
        MetaAnnotationTraverser<MA> traverser = metaAnnotationTraverser(MA)
                .withTraverseStrategy(traverseStrategy)
                .withTraverseDepth(traverseDepth)
                .build()

        and: "a meta impl handler (collecting the impl and meta impl)"
        def collectedAnnotations = []
        def collectedMetaAnnotations = []
        def annotationHandler = createMetaAnnotationHandler(MA, collectedAnnotations, collectedMetaAnnotations)

        when: "traversing method '#method'"
        traverser.traverse(resolveMethodFromClass(method, org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass), annotationHandler)

        then: "the annotations should be collected in the expected order"
        collectedAnnotations == expectedAnnotations

        and: "the meta annotations should be collected in the expected order"
        collectedMetaAnnotations == expectedMetaAnnotations

        and: "have expected impl type"
        haveExpectedAnnotationTypes(collectedAnnotations, StandardAnnotation, OtherAnnotation)

        and: "the collected annotations must have always the same size."
        collectedAnnotations.size() == collectedMetaAnnotations.size()

        where:
        method                    | traverseDepth   | traverseStrategy || expectedAnnotations                                                            | expectedMetaAnnotations
        "declaredInstanceMethod"  | HIERARCHY       | TOP_DOWN         || ann("O-dim", "dim", "TFC", "O-TFC", "MC", "O-BC", "BC", "IF1", "IF0", "O-IF0") | ma("OA", "SA", "SA", "OA", "SA", "OA") + ma("SA", 3) + ma("OA")
        "declaredInstanceMethod"  | HIERARCHY       | BOTTOM_UP        || ann("IF0", "O-IF0", "IF1", "O-BC", "BC", "MC", "TFC", "O-TFC", "O-dim", "dim") | ma("SA", "OA", "SA", "OA") + ma("SA", 3) + ma("OA", "OA", "SA")
        "declaredInstanceMethod"  | CLASS_HIERARCHY | TOP_DOWN         || ann("O-dim", "dim", "TFC", "O-TFC", "MC", "O-BC", "BC")                        | ma("OA", "SA", "SA", "OA", "SA", "OA", "SA")
        "declaredInstanceMethod"  | CLASS_HIERARCHY | BOTTOM_UP        || ann("O-BC", "BC", "MC", "TFC", "O-TFC", "O-dim", "dim")                        | ma("OA") + ma("SA", 3) + ma("OA", "OA", "SA")
        "declaredInstanceMethod"  | DECLARED_CLASS  | TOP_DOWN         || ann("O-dim", "dim", "TFC", "O-TFC")                                            | ma("OA", "SA", "SA", "OA")
        "declaredInstanceMethod"  | DECLARED_CLASS  | BOTTOM_UP        || ann("TFC", "O-TFC", "O-dim", "dim")                                            | ma("SA", "OA", "OA", "SA")
        "declaredInstanceMethod"  | METHOD_ONLY     | TOP_DOWN         || ann("O-dim", "dim")                                                            | ma("OA", "SA")
        "declaredInstanceMethod"  | METHOD_ONLY     | BOTTOM_UP        || ann("O-dim", "dim")                                                            | ma("OA", "SA")

        "inheritedMethod"         | HIERARCHY       | TOP_DOWN         || ann("im", "O-BC", "BC", "IF1", "IF0", "O-IF0")                                 | ma("SA", "OA") + ma("SA", 3) + ma("OA")
        "inheritedMethod"         | HIERARCHY       | BOTTOM_UP        || ann("IF0", "O-IF0", "IF1", "O-BC", "BC", "im")                                 | ma("SA", "OA", "SA", "OA") + ma("SA", 2)
        "inheritedMethod"         | CLASS_HIERARCHY | TOP_DOWN         || ann("im", "O-BC", "BC")                                                        | ma("SA", "OA", "SA")
        "inheritedMethod"         | CLASS_HIERARCHY | BOTTOM_UP        || ann("O-BC", "BC", "im")                                                        | ma("OA", "SA", "SA")
        "inheritedMethod"         | DECLARED_CLASS  | TOP_DOWN         || ann("im", "O-BC", "BC")                                                        | ma("SA", "OA", "SA")
        "inheritedMethod"         | DECLARED_CLASS  | BOTTOM_UP        || ann("O-BC", "BC", "im")                                                        | ma("OA", "SA", "SA")
        "inheritedMethod"         | METHOD_ONLY     | TOP_DOWN         || ann("im")                                                                      | ma("SA")
        "inheritedMethod"         | METHOD_ONLY     | BOTTOM_UP        || ann("im")                                                                      | ma("SA")

        "overriddenInstanceMethod"| HIERARCHY       | TOP_DOWN         || ann("oim", "MC", "O-BC", "BC", "IF1", "IF0", "O-IF0")                          | ma("SA", "SA", "OA") + ma("SA", 3) + ma("OA")
        "overriddenInstanceMethod"| HIERARCHY       | BOTTOM_UP        || ann("IF0", "O-IF0", "IF1", "O-BC", "BC", "MC", "oim")                          | ma("SA", "OA", "SA", "OA") + ma("SA", 3)
        "overriddenInstanceMethod"| CLASS_HIERARCHY | TOP_DOWN         || ann("oim", "MC", "O-BC", "BC")                                                 | ma("SA", "SA", "OA", "SA")
        "overriddenInstanceMethod"| CLASS_HIERARCHY | BOTTOM_UP        || ann("O-BC", "BC", "MC", "oim")                                                 | ma("OA") + ma("SA", 3)
        "overriddenInstanceMethod"| DECLARED_CLASS  | TOP_DOWN         || ann("oim", "MC")                                                               | ma("SA", 2)
        "overriddenInstanceMethod"| DECLARED_CLASS  | BOTTOM_UP        || ann("MC", "oim")                                                               | ma("SA", 2)
        "overriddenInstanceMethod"| METHOD_ONLY     | TOP_DOWN         || ann("oim")                                                                     | ma("SA")
        "overriddenInstanceMethod"| METHOD_ONLY     | BOTTOM_UP        || ann("oim")                                                                     | ma("SA")

        "classMethod"             | HIERARCHY       | TOP_DOWN         || ann("cm", "MC", "O-BC", "BC", "IF1", "IF0", "O-IF0")                           | ma("SA", "SA", "OA") + ma("SA", 3) + ma("OA")
        "classMethod"             | HIERARCHY       | BOTTOM_UP        || ann("IF0", "O-IF0", "IF1", "O-BC", "BC", "MC", "cm")                           | ma("SA", "OA", "SA", "OA") + ma("SA", 3)
        "classMethod"             | CLASS_HIERARCHY | TOP_DOWN         || ann("cm", "MC", "O-BC", "BC")                                                  | ma("SA", "SA", "OA", "SA")
        "classMethod"             | CLASS_HIERARCHY | BOTTOM_UP        || ann("O-BC", "BC", "MC", "cm")                                                  | ma("OA") + ma("SA", 3)
        "classMethod"             | DECLARED_CLASS  | TOP_DOWN         || ann("cm", "MC")                                                                | ma("SA", 2)
        "classMethod"             | DECLARED_CLASS  | BOTTOM_UP        || ann("MC", "cm")                                                                | ma("SA", 2)
        "classMethod"             | METHOD_ONLY     | TOP_DOWN         || ann("cm")                                                                      | ma("SA")
        "classMethod"             | METHOD_ONLY     | BOTTOM_UP        || ann("cm")                                                                      | ma("SA")

        "interfaceMethod"         | HIERARCHY       | TOP_DOWN         || ann("ifm", "IF1", "IF0", "O-IF0")                                              | ma("SA", "SA", "SA", "OA")
        "interfaceMethod"         | HIERARCHY       | BOTTOM_UP        || ann("IF0", "O-IF0", "IF1", "ifm")                                              | ma("SA", "OA", "SA", "SA")
        "interfaceMethod"         | CLASS_HIERARCHY | TOP_DOWN         || ann("ifm", "IF1")                                                              | ma("SA", 2)
        "interfaceMethod"         | CLASS_HIERARCHY | BOTTOM_UP        || ann("IF1", "ifm")                                                              | ma("SA", 2)
        "interfaceMethod"         | DECLARED_CLASS  | TOP_DOWN         || ann("ifm", "IF1")                                                              | ma("SA", 2)
        "interfaceMethod"         | DECLARED_CLASS  | BOTTOM_UP        || ann("IF1", "ifm")                                                              | ma("SA", 2)
        "interfaceMethod"         | METHOD_ONLY     | TOP_DOWN         || ann("ifm")                                                                     | ma("SA")
        "interfaceMethod"         | METHOD_ONLY     | BOTTOM_UP        || ann("ifm")                                                                     | ma("SA")
    }

    def "Which annotations should be collected using a METHOD_ONLY traverser applied on any class '#clazz.simpleName'?"() {
        given: "A meta impl traverser (strategy=#traverseStrategy, depth=#traverseDepth)"
        MetaAnnotationTraverser<MA> traverser = metaAnnotationTraverser(MA)
                .withTraverseStrategy(traverseStrategy)
                .withTraverseDepth(METHOD_ONLY)
                .build()

        and: "a meta impl handler (collecting the impl and meta impl)"
        def collectedAnnotations = []
        def collectedMetaAnnotations = []
        def annotationHandler = createMetaAnnotationHandler(MA, collectedAnnotations, collectedMetaAnnotations)

        when: "traversing on any class object #clazz.simpleName"
        traverser.traverse(clazz, annotationHandler)

        then: "there should be no collected annotations"
        collectedAnnotations.isEmpty()

        and: "there should be no collected meta annotations"
        collectedMetaAnnotations.isEmpty()

        where:
        clazz                                                                     | traverseStrategy
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | TOP_DOWN
        org.failearly.dataz.internal.common.annotation.traverser.TestFixtureClass | BOTTOM_UP
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | TOP_DOWN
        org.failearly.dataz.internal.common.annotation.traverser.BaseClass        | BOTTOM_UP
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | TOP_DOWN
        org.failearly.dataz.internal.common.annotation.traverser.Interface1       | BOTTOM_UP
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    @Unroll("How to check if an impl (#annotation) is available (starting with #method using #traverseDepth)?")
    def "How to check if an annotation is available?"() {
        given: "Two standard traversers (depth=#traverseDepth)"
        final topDownTraverser = metaAnnotationTraverser(annotation)
                .withTraverseStrategy(TOP_DOWN)
                .withTraverseDepth(traverseDepth)
                .build();
        final bottomUpTraverser = metaAnnotationTraverser(annotation)
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
        MA                 | "noAnnotations" | HIERARCHY       || true
        MA                 | "noAnnotations" | CLASS_HIERARCHY || true
        MA                 | "noAnnotations" | DECLARED_CLASS  || true
        MA                 | "noAnnotations" | METHOD_ONLY     || false

        // Annotation MA is annotated with MetaMetaAnnotation
        MetaMetaAnnotation | "noAnnotations" | HIERARCHY       || true
        MetaMetaAnnotation | "noAnnotations" | CLASS_HIERARCHY || true
        MetaMetaAnnotation | "noAnnotations" | DECLARED_CLASS  || true
        MetaMetaAnnotation | "noAnnotations" | METHOD_ONLY     || false

        // StandardAnnotation is not a MetaAnnotation
        SA                 | "noAnnotations" | HIERARCHY       || false
        SA                 | "noAnnotations" | METHOD_ONLY     || false

        NotUsedAnnotation  | "noAnnotations" | HIERARCHY       || false
        NotUsedAnnotation  | "noAnnotations" | METHOD_ONLY     || false
    }

    private static List repeat(String val, int num) {
        //noinspection GroovyAssignabilityCheck
        (1..num).inject([]) { List list, number ->
            list.add val
            list
        }
    }

    private static ann(String... ids) {
        ids.inject([]) { List list, id ->
            if( id.startsWith("O-") )
                list += oa(id)
            else
                list += sa(id)
            list
        }
    }

    private static sa(String... ids) {
        expected(SA, ids)
    }

    private static oa(String... ids) {
        expected(OA, ids)
    }

    private static ma(String id, int num) {
        expected(MA, repeat(id, num))
    }

    private static ma(String... ids) {
        expected(MA, ids)
    }

    @StandardAnnotation(id = "GC")
    private static class GroovyClass {}
}