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

import org.failearly.dataz.template.TemplateObject
import spock.lang.Subject
import spock.lang.Unroll

import static org.failearly.dataz.internal.common.annotation.traverser.TraverseDepth.DECLARED_CLASS
import static org.failearly.dataz.internal.common.test.utils.ReflectionUtils.resolveMethodFromClass
import static org.failearly.dataz.internal.template.TemplateObjectDuplicateStrategy.IGNORE
import static org.failearly.dataz.internal.template.TemplateObjectDuplicateStrategy.STRICT

/**
 * TemplateObjectsSpec contains tests for {@link TemplateObjects}.
 *
 * {@code TemplateObjects} will be created by {@link TemplateObjectsResolver}, which collects all
 * {@link org.failearly.dataz.template.TemplateObject} instances.
 *
 * These are must be filtered and merged. After this the subset could be assembled/applied by a template engine.
 *
 * - {@link TemplateObjects#filterByDataSet(java.lang.String)}
 * - {@link TemplateObjects#filterGlobalScope()}
 * - {@link TemplateObjects#merge(org.failearly.dataz.internal.template.TemplateObjects)}
 * - {@link TemplateObjects#apply(java.util.function.Consumer)}
 */
@Subject(TemplateObjects)
class TemplateObjectsSpec extends TemplateObjectsSpecBase {
    @Unroll("How to filter TemplateObjects which are associated specific #dataset? - #method")
    def "How to filter TemplateObjects which are associated specific dataset?"() {
        given: "a Template objects resolver"
        def templateObjectResolver = TemplateObjectsResolver.createTemplateObjectsResolver(DECLARED_CLASS, IGNORE)

        and: "resolving template objects from #method"
        final TemplateObjects templateObjects = templateObjectResolver.resolveFromMethod(resolveMethodFromClass(method, TestFixtureForTemplateObjectSpec))

        when: "filter by #dataset"
        final TemplateObjects filtered = templateObjects.filterByDataSet(dataset)

        then: "should only the expected should be available"
        collectNames(filtered) == expectedNames

        and: "only the specified datasets should be available or empty"
        def dataSets = collectDataSets(filtered)
        dataSets.contains(expectedDataSet) || (expectedDataSet.isEmpty() && dataSets.isEmpty())

        where:
        method                    | dataset   || expectedNames | expectedDataSet
        "localScope"              | "DS1"     || ["LTO-1", "LTO-2", "LTO-0", "GTO-0"] | "DS1"
        "localScope"              | "DS2"     || ["LTO-1", "LTO-0", "GTO-0"] | "DS2"
        "localScope"              | "DS3"     || ["LTO-0", "GTO-0"] | "DS3"
        "localScope"              | "unknown" || [] | ""

        "defaultScope"            | "DS1"     || ["TO-5", "LTO-0", "GTO-0"] | "DS1"
        "defaultScope"            | "unknown" || [] | ""

        "globalScope"             | "DS1"     || ["GTO-1", "GTO-2", "LTO-0", "GTO-0"] | "DS1"
        "globalScope"             | "DS2"     || ["GTO-1", "LTO-0", "GTO-0"] | "DS2"
        "globalScope"             | "DS3"     || ["LTO-0", "GTO-0"] | "DS3"
        "globalScope"             | "unknown" || [] | ""

        "localScopeButNoDataSet"  | "DS3"     || ["LTO-NO-DS", "LTO-0", "GTO-0"] | "DS3"
        "localScopeButNoDataSet"  | "unknown" || ["LTO-NO-DS"] | ""

        "globalScopeButNoDataSet" | "DS3"     || ["GTO-NO-DS", "LTO-0", "GTO-0"] | "DS3"
        "globalScopeButNoDataSet" | "unknown" || ["GTO-NO-DS"] | ""
    }

    @Unroll("How to filter TemplateObjects with GLOBAL scope? - #method")
    def "How to filter TemplateObjects with GLOBAL scope?"() {
        given: "a Template objects resolver"
        def templateObjectResolver = TemplateObjectsResolver.createTemplateObjectsResolver(DECLARED_CLASS, IGNORE)

        and: "resolving template objects from #method"
        final TemplateObjects templateObjects = templateObjectResolver.resolveFromMethod(resolveMethodFromClass(method, TestFixtureForTemplateObjectSpec))

        when: "filter by GLOBAl scope"
        final TemplateObjects filtered = templateObjects.filterGlobalScope()

        then: "should only the expected should be available"
        collectNames(filtered) == expectedNames

        where:
        method                    || expectedNames
        "localScope"              || ["GTO-0"]
        "defaultScope"            || ["GTO-0"]  //  Default scope==LOCAL

        "globalScope"             || ["GTO-1", "GTO-2", "GTO-0"]
        "globalScopeButNoDataSet" || ["GTO-NO-DS", "GTO-0"]
    }

    @Unroll("How to merge datasets? - #method")
    def "How to merge datasets?"() {
        given: "a Template objects resolver (using STRICT)"
        def templateObjectResolver = TemplateObjectsResolver.createTemplateObjectsResolver(DECLARED_CLASS, STRICT)

        and: "resolving template objects from mergeFixture"
        final TemplateObjects templateObjects1 = templateObjectResolver.resolveFromMethod(resolveMethodFromClass("mergeFixture", TestFixtureForTemplateObjectSpec))

        and: "resolving template objects from #method"
        final TemplateObjects templateObjects2 = templateObjectResolver.resolveFromMethod(resolveMethodFromClass(method, TestFixtureForTemplateObjectSpec))

        when: "merge the two template objects"
        final TemplateObjects merged=templateObjects1.merge(templateObjects2)

        then: "should merged to expected"
        collectNames(merged) == expectedMergeResult

        where:
        method                    || expectedMergeResult
        "mergeFixture"            || ["MERGE-1", "MERGE-2", "LTO-0", "GTO-0"]
        "localScope"              || ["MERGE-1", "MERGE-2", "LTO-0", "GTO-0", "LTO-1", "LTO-2"]
        "localScopeButNoDataSet"  || ["MERGE-1", "MERGE-2", "LTO-0", "GTO-0", "LTO-NO-DS"]
        "globalScope"             || ["MERGE-1", "MERGE-2", "LTO-0", "GTO-0", "GTO-1", "GTO-2"]
        "globalScopeButNoDataSet" || ["MERGE-1", "MERGE-2", "LTO-0", "GTO-0", "GTO-NO-DS"]
    }

    def "How to iterate over all TemplateObjects?"() {
        given: "a Template objects resolver"
        def templateObjectResolver = TemplateObjectsResolver.createTemplateObjectsResolver(DECLARED_CLASS, IGNORE)

        and: "resolving template objects from #method"
        final TemplateObjects templateObjects = templateObjectResolver.resolveFromMethod(resolveMethodFromClass("localScope", TestFixtureForTemplateObjectSpec))

        when: "using process() on templateObjects (collecting the template object's name)"
        def templateObjectNames=[]
        templateObjects.apply({TemplateObject templateObject-> templateObjectNames.add(templateObject.name())})

        then: "should have same result"
        templateObjectNames==collectNames(templateObjects)
    }
}