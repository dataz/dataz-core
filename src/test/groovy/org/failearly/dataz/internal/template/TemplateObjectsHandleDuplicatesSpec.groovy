/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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
package org.failearly.dataz.internal.template

import spock.lang.Subject
import spock.lang.Unroll

import static org.failearly.common.annotation.traverser.TraverseDepth.CLASS_HIERARCHY
import static org.failearly.common.test.utils.ReflectionUtils.resolveMethodFromClass
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