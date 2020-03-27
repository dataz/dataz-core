/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */


package org.failearly.dataz.internal.common.annotation.traverser

import org.failearly.dataz.internal.common.annotation.traverser.ccs.*
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static org.failearly.dataz.internal.common.annotation.traverser.TraverseDepth.*
import static org.failearly.dataz.internal.common.annotation.traverser.TraverseStrategy.BOTTOM_UP
import static org.failearly.dataz.internal.common.annotation.traverser.TraverseStrategy.TOP_DOWN
import static org.failearly.dataz.common.test.utils.ReflectionUtils.resolveMethodFromClass


/**
 * ClassesCollectorSpec contains specification for ClassesCollector.
 */
@Subject(ClassesCollector)
class ClassesCollectorSpec extends Specification {
    @Unroll("""Which class objects will be collected on class '#testFixtureClass.simpleName' -
            using #traverseDepth and #traverseStrategy strategy?""")
    def "Which class objects will be collected on class objects?"() {
        given: "A class collector"
        final classesCollector = new ClassesCollector(traverseStrategy, traverseDepth)

        when: "applying the class collector on class object"
        final collectedClasses = classesCollector.collect(testFixtureClass)

        then: "the collected classes should be in the complete and in the correct order"
        collectedClasses == expectedClasses

        and: "never contains java.lang.Object"
        !collectedClasses.contains(Object)

        where:
        testFixtureClass | traverseStrategy | traverseDepth   || expectedClasses
        FixtureClass     | TOP_DOWN         | METHOD_ONLY     || []
        FixtureClass     | TOP_DOWN         | DECLARED_CLASS  || [FixtureClass]
        FixtureClass     | TOP_DOWN         | CLASS_HIERARCHY || [FixtureClass, Base0]
        FixtureClass     | BOTTOM_UP        | CLASS_HIERARCHY || [FixtureClass, Base0].reverse()
        FixtureClass     | TOP_DOWN         | HIERARCHY       || [FixtureClass, Base0, IFace2, IFace0, IFace1]
        FixtureClass     | BOTTOM_UP        | HIERARCHY       || [FixtureClass, Base0, IFace2, IFace0, IFace1].reverse()

        Base0            | TOP_DOWN         | HIERARCHY       || [Base0, IFace1, IFace0]
        Base0            | TOP_DOWN         | CLASS_HIERARCHY || [Base0]
        IFace1           | TOP_DOWN         | HIERARCHY       || [IFace1, IFace0]
        IFace1           | TOP_DOWN         | CLASS_HIERARCHY || [IFace1]
    }


    @Unroll("""Which class objects will be collected on method '#method' on class object '#testFixtureClass.simpleName' -
            using #traverseDepth strategy?""")
    def "Which class objects will be collected on method objects?"() {
        given: "A class collector (using always BOTTOM_UP strategy)"
        final classesCollector = new ClassesCollector(BOTTOM_UP, traverseDepth)

        when: "applying the class collector on class object"
        final collectedClasses = classesCollector.collect(resolveMethodFromClass(method, testFixtureClass))

        then: "the collected classes should be in the complete and in the correct order"
        collectedClasses == expectedClasses

        where:
        testFixtureClass | method            | traverseDepth   || expectedClasses
        FixtureClass     | "interfaceMethod" | METHOD_ONLY     || []
        FixtureClass     | "instanceMethod"  | METHOD_ONLY     || []
        FixtureClass     | "notOverridden"   | METHOD_ONLY     || []
        FixtureClass     | "classMethod"     | METHOD_ONLY     || []

        FixtureClass     | "interfaceMethod" | DECLARED_CLASS  || [FixtureClass]
        FixtureClass     | "instanceMethod"  | DECLARED_CLASS  || [FixtureClass]
        FixtureClass     | "notOverridden"   | DECLARED_CLASS  || [Base0]
        FixtureClass     | "classMethod"     | DECLARED_CLASS  || [Base0]

        FixtureClass     | "interfaceMethod" | CLASS_HIERARCHY || [Base0, FixtureClass]
        FixtureClass     | "instanceMethod"  | CLASS_HIERARCHY || [Base0, FixtureClass]
        FixtureClass     | "notOverridden"   | CLASS_HIERARCHY || [Base0]
        FixtureClass     | "classMethod"     | CLASS_HIERARCHY || [Base0]

        FixtureClass     | "interfaceMethod" | HIERARCHY       || [IFace1, IFace0, IFace2, Base0, FixtureClass]
        FixtureClass     | "instanceMethod"  | HIERARCHY       || [IFace1, IFace0, IFace2, Base0, FixtureClass]
        FixtureClass     | "notOverridden"   | HIERARCHY       || [IFace0, IFace1, Base0]
        FixtureClass     | "classMethod"     | HIERARCHY       || [IFace0, IFace1, Base0]

        FixtureClass     | "instanceMethod"  | DECLARED_CLASS  || [FixtureClass]
        FixtureClass     | "instanceMethod"  | CLASS_HIERARCHY || [Base0, FixtureClass]
        FixtureClass     | "instanceMethod"  | HIERARCHY       || [IFace1, IFace0, IFace2, Base0, FixtureClass]

        IFace1           | "interfaceMethod" | HIERARCHY       || [IFace0]
        IFace1           | "interfaceMethod" | CLASS_HIERARCHY || [IFace0]
        Base0            | "interfaceMethod" | CLASS_HIERARCHY || [IFace0]
    }
}