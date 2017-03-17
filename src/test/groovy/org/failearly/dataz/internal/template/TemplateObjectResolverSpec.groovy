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

import org.failearly.dataz.config.DataSetProperties
import org.failearly.dataz.test.SimpleTemplateObject
import spock.lang.Subject
import spock.lang.Unroll

import static org.failearly.common.annotation.traverser.TraverseDepth.*
import static org.failearly.common.test.utils.ReflectionUtils.resolveMethodFromClass
import static org.failearly.dataz.internal.template.TemplateObjectDuplicateStrategy.IGNORE
import static org.failearly.dataz.internal.template.TemplateObjectDuplicateStrategy.STRICT


/**
 * How does the {@link TemplateObjectsResolver} resolves {@link TemplateObjects}?
 */
@Subject([TemplateObjectsResolver, TemplateObjects])
@SuppressWarnings("GroovyUnusedDeclaration")
class TemplateObjectResolverSpec extends TemplateObjectsSpecBase {

    @Unroll("How to resolve template objects from a class object? - clazz=#clazz.simpleName and traverseDepth=#traverseDepth")
    def "How to resolve template objects from a class?"() {
        given: "a Template objects resolver"
        def templateObjectResolver = TemplateObjectsResolver.createTemplateObjectsResolver(traverseDepth, IGNORE)

        when: "resolving template objects from #clazz"
        final TemplateObjects templateObjects = templateObjectResolver.resolveFromClass(clazz)

        and: "collect the descriptions from all template objects"
        def descriptions = collectDescriptions(templateObjects)

        then: "only the expected descriptions should be collected"
        descriptions == expected

        where:
        clazz              | traverseDepth   || expected
        NoClassHierarchy   | DECLARED_CLASS  || ["NoClassHierarchy"]
        ClassHierarchy     | DECLARED_CLASS  || ["ClassHierarchy"]
        InterfaceHierarchy | DECLARED_CLASS  || ["InterfaceHierarchy"]

        NoClassHierarchy   | CLASS_HIERARCHY || ["NoClassHierarchy"]
        ClassHierarchy     | CLASS_HIERARCHY || ["ClassHierarchy", "BaseClass"]
        InterfaceHierarchy | CLASS_HIERARCHY || ["InterfaceHierarchy"]

        ClassHierarchy     | HIERARCHY       || ["ClassHierarchy", "BaseClass", "AnInterface"]
        InterfaceHierarchy | HIERARCHY       || ["InterfaceHierarchy", "BaseInterface"]

        // Special cases
        NoTemplateObjects  | HIERARCHY       || []
        ClassHierarchy     | METHOD_ONLY     || []
    }

    @Unroll("How to resolve template objects from a method? - method=#method and traverseDepth=#traverseDepth")
    def "How to resolve template objects from a method?"() {
        given: "a Template objects resolver"
        def templateObjectResolver = TemplateObjectsResolver.createTemplateObjectsResolver(traverseDepth, IGNORE)

        when: "resolving template objects from #method"
        final TemplateObjects templateObjects = templateObjectResolver.resolveFromMethod(resolveMethodFromClass(method, ClassHierarchy))

        and: "collect the descriptions from all template objects"
        def descriptions = collectDescriptions(templateObjects)

        then: "only the expected descriptions should be collected"
        descriptions == expected

        where:
        method                                           | traverseDepth   || expected
        "noTemplateObjectAnnotations"                    | METHOD_ONLY     || []
        "methodWithTemplateObjectAnnotations"            | METHOD_ONLY     || ["methodWithTemplateObjectAnnotations"]
        "methodWithTemplateObjectAnnotationsInBaseClass" | METHOD_ONLY     || ["methodWithTemplateObjectAnnotationsInBaseClass"]
        "methodWithTemplateObjectAnnotationsInInterface" | METHOD_ONLY     || ["Implements methodWithTemplateObjectAnnotationsInInterface"]

        "noTemplateObjectAnnotations"                    | DECLARED_CLASS  || ["ClassHierarchy"]
        "methodWithTemplateObjectAnnotations"            | DECLARED_CLASS  || ["methodWithTemplateObjectAnnotations", "ClassHierarchy"]
        "methodWithTemplateObjectAnnotationsInBaseClass" | DECLARED_CLASS  || ["methodWithTemplateObjectAnnotationsInBaseClass", "BaseClass"]
        "methodWithTemplateObjectAnnotationsInInterface" | DECLARED_CLASS  || ["Implements methodWithTemplateObjectAnnotationsInInterface", "BaseClass"]

        "noTemplateObjectAnnotations"                    | CLASS_HIERARCHY || ["ClassHierarchy", "BaseClass"]
        "methodWithTemplateObjectAnnotations"            | CLASS_HIERARCHY || ["methodWithTemplateObjectAnnotations", "ClassHierarchy", "BaseClass"]
        "methodWithTemplateObjectAnnotationsInBaseClass" | CLASS_HIERARCHY || ["methodWithTemplateObjectAnnotationsInBaseClass", "BaseClass"]
        "methodWithTemplateObjectAnnotationsInInterface" | CLASS_HIERARCHY || ["Implements methodWithTemplateObjectAnnotationsInInterface", "BaseClass"]

        "noTemplateObjectAnnotations"                    | HIERARCHY       || ["ClassHierarchy", "BaseClass", "AnInterface"]
        "methodWithTemplateObjectAnnotations"            | HIERARCHY       || ["methodWithTemplateObjectAnnotations", "ClassHierarchy", "BaseClass", "AnInterface"]
        "methodWithTemplateObjectAnnotationsInBaseClass" | HIERARCHY       || ["methodWithTemplateObjectAnnotationsInBaseClass", "BaseClass", "AnInterface"]
        "methodWithTemplateObjectAnnotationsInInterface" | HIERARCHY       || ["Implements methodWithTemplateObjectAnnotationsInInterface", "BaseClass", "AnInterface"]
    }

// Simple fixture classes
    private static class NoTemplateObjects {}

    @SimpleTemplateObject(name = "TO-1", description = "NoClassHierarchy")
    private static class NoClassHierarchy {}

// ClassHierarchy including interface (without duplication)
    @SimpleTemplateObject(name = "TO-0", description = "AnInterface")
    private interface AnInterface {
        @SimpleTemplateObject(name = "TO-0-1", description = "methodWithTemplateObjectAnnotationsInInterface")
        void methodWithTemplateObjectAnnotationsInInterface()
    }

    @SimpleTemplateObject(name = "TO-1", description = "BaseClass")
    private static class BaseClass implements AnInterface {
        @SimpleTemplateObject(name = "TO-1-1", description = "methodWithTemplateObjectAnnotationsInBaseClass")
        void methodWithTemplateObjectAnnotationsInBaseClass() {}

        @Override
        @SimpleTemplateObject(name = "TO-1-2", description = "Implements methodWithTemplateObjectAnnotationsInInterface")
        void methodWithTemplateObjectAnnotationsInInterface() {}
    }

    @SimpleTemplateObject(name = "TO-2", description = "ClassHierarchy")
    private static class ClassHierarchy extends BaseClass {
        void noTemplateObjectAnnotations() {}

        @SimpleTemplateObject(name = "TO-2-1", description = "methodWithTemplateObjectAnnotations")
        void methodWithTemplateObjectAnnotations() {}
    }

    @Unroll("How to resolve template objects from a method on interface? - method=#method(#clazz.simpleName) and traverseDepth=#traverseDepth\"")
    def "How to resolve template objects from a method on interfaces?"() {
        given: "a Template objects resolver"
        def templateObjectResolver = TemplateObjectsResolver.createTemplateObjectsResolver(traverseDepth, STRICT)

        when: "resolving template objects from #method"
        final TemplateObjects templateObjects = templateObjectResolver.resolveFromMethod(resolveMethodFromClass(method, clazz))

        and: "collect the descriptions from all template objects"
        def descriptions = collectDescriptions(templateObjects)

        then: "only the expected descriptions should be collected"
        descriptions == expected

        where:
        clazz              | method                       | traverseDepth   || expected
        InterfaceHierarchy | "methodOnBaseInterface"      | METHOD_ONLY     || ["Override methodOnBaseInterface"]
        BaseInterface      | "methodOnBaseInterface"      | METHOD_ONLY     || ["methodOnBaseInterface"]
        InterfaceHierarchy | "methodOnInterfaceHierarchy" | METHOD_ONLY     || ["methodOnInterfaceHierarchy"]

        InterfaceHierarchy | "methodOnBaseInterface"      | DECLARED_CLASS  || ["Override methodOnBaseInterface", "InterfaceHierarchy"]
        BaseInterface      | "methodOnBaseInterface"      | DECLARED_CLASS  || ["methodOnBaseInterface", "BaseInterface"]
        InterfaceHierarchy | "methodOnInterfaceHierarchy" | DECLARED_CLASS  || ["methodOnInterfaceHierarchy", "InterfaceHierarchy"]

        InterfaceHierarchy | "methodOnBaseInterface"      | CLASS_HIERARCHY || ["Override methodOnBaseInterface", "InterfaceHierarchy"]
        BaseInterface      | "methodOnBaseInterface"      | CLASS_HIERARCHY || ["methodOnBaseInterface", "BaseInterface"]
        InterfaceHierarchy | "methodOnInterfaceHierarchy" | CLASS_HIERARCHY || ["methodOnInterfaceHierarchy", "InterfaceHierarchy"]

        InterfaceHierarchy | "methodOnBaseInterface"      | HIERARCHY       || ["Override methodOnBaseInterface", "InterfaceHierarchy", "BaseInterface"]
        BaseInterface      | "methodOnBaseInterface"      | HIERARCHY       || ["methodOnBaseInterface", "BaseInterface"]
        InterfaceHierarchy | "methodOnInterfaceHierarchy" | HIERARCHY       || ["methodOnInterfaceHierarchy", "InterfaceHierarchy", "BaseInterface"]
    }

    // InterfaceHierarchy (without duplication)
    @SimpleTemplateObject(name = "TO-0", description = "BaseInterface")
    private interface BaseInterface {
        @SimpleTemplateObject(name = "TO-0-1", description = "methodOnBaseInterface")
        void methodOnBaseInterface()
    }

    @SimpleTemplateObject(name = "TO-1", description = "InterfaceHierarchy")
    private interface InterfaceHierarchy extends BaseInterface {
        @SimpleTemplateObject(name = "TO-1-1", description = "Override methodOnBaseInterface")
        void methodOnBaseInterface()

        @SimpleTemplateObject(name = "TO-1-2", description = "methodOnInterfaceHierarchy")
        void methodOnInterfaceHierarchy()
    }


    @Unroll("How to set standard settings for traverse depth for TemplateObjectResolver? - #traverseDepth")
    def "How to set standard settings for traverse depth for TemplateObjectResolver?"() {
        setup: "Set the standard traversing depth"
        System.setProperty(DataSetProperties.DATAZ_TEMPLATE_OBJECT_TRAVERSING_DEPTH, traverseDepth)
        System.setProperty(DataSetProperties.DATAZ_TEMPLATE_OBJECT_DUPLICATE_STRATEGY, "IGNORE")
        DataSetProperties.reload()

        and: "create a TemplateObjectsResolver with standard settings"
        def templateObjectResolver = TemplateObjectsResolver.withStandardSettings()

        when: "resolving template objects from method"
        final TemplateObjects templateObjects = templateObjectResolver.resolveFromMethod(resolveMethodFromClass("methodWithTemplateObjectAnnotations", ClassHierarchy))

        and: "collect the descriptions from all template objects"
        def descriptions = collectDescriptions(templateObjects)

        then: "only the expected descriptions should be collected"
        descriptions == expected

        cleanup:
        System.clearProperty(DataSetProperties.DATAZ_TEMPLATE_OBJECT_TRAVERSING_DEPTH)
        System.clearProperty(DataSetProperties.DATAZ_TEMPLATE_OBJECT_DUPLICATE_STRATEGY)
        DataSetProperties.reload()

        where:
        traverseDepth     || expected
        "METHOD_ONLY"     || ["methodWithTemplateObjectAnnotations"]
        "DECLARED_CLASS"  || ["methodWithTemplateObjectAnnotations", "ClassHierarchy"]
        "CLASS_HIERARCHY" || ["methodWithTemplateObjectAnnotations", "ClassHierarchy", "BaseClass"]
        "HIERARCHY"       || ["methodWithTemplateObjectAnnotations", "ClassHierarchy", "BaseClass", "AnInterface"]
    }

    @Unroll("How to set standard settings for handling duplicated template objects for TemplateObjects? - #duplicateStrategy")
    def "How to set standard settings for handling duplicated template objects for TemplateObjects?"() {
        setup: "Set the standard duplicate strategy"
        System.setProperty(DataSetProperties.DATAZ_TEMPLATE_OBJECT_TRAVERSING_DEPTH, "CLASS_HIERARCHY")
        System.setProperty(DataSetProperties.DATAZ_TEMPLATE_OBJECT_DUPLICATE_STRATEGY, duplicateStrategy)
        DataSetProperties.reload()

        and: "create a TemplateObjectsResolver with standard settings"
        def templateObjectResolver = TemplateObjectsResolver.withStandardSettings()

        and: "resolving template objects"
        final TemplateObjects templateObjects = templateObjectResolver.resolveFromMethod(
                resolveMethodFromClass("methodWithOneTO", TestFixtureForDuplicateStrategy)
        )

        when: "collect the descriptions from all template objects"
        def descriptions = collectDescriptions(templateObjects)

        then: "the duplicates should be overwritten"
        descriptions.toSet() == expected.toSet()

        cleanup:
        System.clearProperty(DataSetProperties.DATAZ_TEMPLATE_OBJECT_TRAVERSING_DEPTH)
        System.clearProperty(DataSetProperties.DATAZ_TEMPLATE_OBJECT_DUPLICATE_STRATEGY)
        DataSetProperties.reload()

        where:
        duplicateStrategy || expected
        "IGNORE"          || ["TO-2 (2nd)", "TO-1 (1st)"]
        "OVERWRITE"       || ["TO-2 (2nd)", "TO-1 (2nd)"]
        // "STRICT"       || []  // <<< This will cause an expected exception, so do only remove comment, if you
        // you like test it :-)
    }
}