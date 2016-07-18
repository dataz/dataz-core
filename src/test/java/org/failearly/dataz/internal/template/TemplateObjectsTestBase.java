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

package org.failearly.dataz.internal.template;

import org.failearly.common.annotation.traverser.TraverseDepth;
import org.failearly.common.test.utils.ReflectionUtils;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.test.SimpleTemplateObject;
import org.junit.Before;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

/**
 * TemplateObjectsTestBase is base class for all {@link TemplateObjects} or {@link TemplateObjectsResolver} related tests.
 */
abstract class TemplateObjectsTestBase {
    static final String GLOBAL_TEMPLATE_OBJECT_DATA_SET = "<don't care>";

    private TemplateObjectsResolver.Builder templateObjectsResolverBuilder;

    @Before
    public void createTemplateObjectResolverBuilder() throws Exception {
        templateObjectsResolverBuilder = TemplateObjectsResolver.builder();
    }

    protected static Method getTestMethod(String name) throws NoSuchMethodException {
        return ReflectionUtils.resolveMethodFromClass(name, AClass.class);
    }

    protected static List<String> extractTemplateObjectInstances(TemplateObjects templateObjects) {
        return templateObjects.collectTemplateObjectInstances().stream().map(TemplateObject::toString).collect(Collectors.toList());
    }

    protected static Set<String> extractDataSetNames(TemplateObjects templateObjects) {
        return templateObjects.collectDataSets();
    }

    protected static Method withTemplateObjects() throws NoSuchMethodException {
        return getTestMethod("withTemplateObjects");
    }

    protected static Method withoutTemplateObjects() throws NoSuchMethodException {
        return getTestMethod("withoutTemplateObjects");
    }

    protected static Method withDuplicatedTemplateObjects() throws NoSuchMethodException {
        return getTestMethod("withDuplicatedTemplateObjects");
    }

    protected static void assertTemplateObjects(TemplateObjects templateObjects, String... expectedAnnotations) {
        assertThat(extractTemplateObjectInstances(templateObjects), containsInAnyOrder(expectedAnnotations));
    }

    protected static void assertEmptyTemplateObjects(TemplateObjects templateObjects) {
        assertThat(extractTemplateObjectInstances(templateObjects), is(empty()));
    }


    protected TemplateObjectsResolver buildTemplateObjectsResolver(TraverseDepth traverseDepth) {
        return templateObjectsResolverBuilder                                                     //
                .withTemplateObjectDuplicateStrategy(TemplateObjectDuplicateStrategy.IGNORE)  //
                .withTraverseDepth(traverseDepth)                                             //
                .build();
    }

    protected TemplateObjectsResolver buildTemplateObjectResolverNoDuplicateHandler(TemplateObjectDuplicateStrategy templateObjectDuplicateStrategy) {
        return templateObjectsResolverBuilder                                           //
                .withTraverseDepth(TraverseDepth.METHOD_ONLY)                        //
                .withTemplateObjectDuplicateStrategy(templateObjectDuplicateStrategy)   //
                .build();
    }

    protected TemplateObjectsResolver buildTemplateObjectResolver(TemplateObjectDuplicateStrategy templateObjectDuplicateStrategy, DuplicateHandler duplicateHandler) {
        return templateObjectsResolverBuilder                                               //
                .withTraverseDepth(TraverseDepth.METHOD_ONLY)                        //
                .withTemplateObjectDuplicateStrategy(templateObjectDuplicateStrategy)   //
                .withDuplicateHandler(duplicateHandler)                                 //
                .build();
    }

    // Test Fixtures
    @SimpleTemplateObject(name = "G1", dataset = "D0", description = "(0) on class BaseClass")
    private static class BaseClass {
    }

    @SuppressWarnings("UnusedDeclaration")
    @SimpleTemplateObject(name = "GLOBAL", dataset = GLOBAL_TEMPLATE_OBJECT_DATA_SET, description = "(0) on class AClass", scope = Scope.GLOBAL)
    @SimpleTemplateObject(name = "G1", dataset = "D1", description = "(1) on class AClass")
    @SimpleTemplateObject(name = "G2", dataset = "D2", description = "(2) on class AClass")
    static class AClass extends BaseClass {

        @SimpleTemplateObject(name = "G2", dataset = "D3", description = "(3) On method withTemplateObjects")
        @SimpleTemplateObject(name = "G3", dataset = "D3", description = "(4) On method withTemplateObjects")
        public void withTemplateObjects() {
        }

        public void withoutTemplateObjects() {
        }


        @SimpleTemplateObject(name = "G0", dataset = "SAME-DATASET", description = "(5) On method withDuplicatedTemplateObjects")
        @SimpleTemplateObject(name = "G0", dataset = "OTHER-DATASET", description = "(6) On method withDuplicatedTemplateObjects")
        @SimpleTemplateObject(name = "G0", dataset = "SAME-DATASET", description = "(7) On method withDuplicatedTemplateObjects. OVERWRITE G0")
        public void withDuplicatedTemplateObjects() {
        }
    }
}
