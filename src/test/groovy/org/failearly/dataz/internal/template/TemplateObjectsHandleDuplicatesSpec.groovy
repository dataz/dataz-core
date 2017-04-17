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
package org.failearly.dataz.internal.template

import spock.lang.Subject
import spock.lang.Unroll

import static org.failearly.dataz.internal.common.annotation.traverser.TraverseDepth.CLASS_HIERARCHY
import static org.failearly.dataz.common.test.utils.ReflectionUtils.resolveMethodFromClass
import static org.failearly.dataz.internal.template.TemplateObjectDuplicateStrategy.*
/**
 * How does {@link TemplateObjects} handles duplicated {@link org.failearly.dataz.template.TemplateObject}.
 *
 * A {@link org.failearly.dataz.template.TemplateObject} must be unique by it's
 * {@link org.failearly.dataz.template.TemplateObject#name() name}.
 */
@Subject(TemplateObjects)
class TemplateObjectsHandleDuplicatesSpec extends TemplateObjectsSpecBase {
    @Unroll("How to handle duplicated template objects using duplicate strategy #duplicateStrategy? - #method")
    def "How does OVERWRITE/IGNORE strategies behave?"() {
        given: "a Template objects resolver"
        def templateObjectResolver = TemplateObjectsResolver.createTemplateObjectsResolver(CLASS_HIERARCHY, duplicateStrategy)

        and: "resolving template objects from #method"
        final TemplateObjects templateObjects = templateObjectResolver.resolveFromMethod(resolveMethodFromClass(method, TestFixtureForDuplicateStrategy))

        when: "collect the descriptions from all template objects"
        def descriptions = collectDescriptions(templateObjects)

        then: "the duplicates should be overwritten"
        descriptions.toSet() == expected.toSet()


        where:
        method                 | duplicateStrategy || expected
        "methodWithoutTOs"     | OVERWRITE         || ["TO-1 (2nd)", "TO-2 (1st)"]
        "methodWithoutTOs"     | IGNORE            || ["TO-1 (1st)", "TO-2 (1st)"]

        "methodWithOneTO"      | OVERWRITE         || ["TO-1 (2nd)", "TO-2 (2nd)"]
        "methodWithOneTO"      | IGNORE            || ["TO-1 (1st)", "TO-2 (2nd)"]

        "methodWithDuplicates" | OVERWRITE         || ["TO-3 (2nd)", "TO-1 (2nd)", "TO-2 (1st)"]
        "methodWithDuplicates" | IGNORE            || ["TO-3 (1st)", "TO-1 (1st)", "TO-2 (1st)"]
    }

    @Unroll("How to handle duplicated template objects using duplicate strategy STRICT? - #method")
    def "How does STRICT strategies behave?"() {
        given: "a Template objects resolver (with strategy STRICT)"
        def templateObjectResolver = TemplateObjectsResolver.createTemplateObjectsResolver(CLASS_HIERARCHY, STRICT)

        and: "resolving template objects from #method"
        final TemplateObjects templateObjects = templateObjectResolver.resolveFromMethod(resolveMethodFromClass(method, TestFixtureForDuplicateStrategy))

        when: "access template objects"
        templateObjects.collectTemplateObjectInstances()

        then: "the first duplicate found causes an exception"
        def exception = thrown(DuplicateTemplateObjectException)

        and: "the exception's message shows the source of trouble"
        exception.message == expectedExceptionMessage

        where:
        method                 || expectedExceptionMessage
        "methodWithoutTOs"     || "Duplicate template object found: @org.failearly.dataz.test.SimpleTemplateObject(description=TO-1 (2nd), datasets=[], name=TO-1)"
        "methodWithOneTO"      || "Duplicate template object found: @org.failearly.dataz.test.SimpleTemplateObject(description=TO-2 (1st), datasets=[], name=TO-2)"
        "methodWithDuplicates" || "Duplicate template object found: @org.failearly.dataz.test.SimpleTemplateObject(description=TO-3 (2nd), datasets=[], name=TO-3)"
    }
}