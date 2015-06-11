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

package org.failearly.dataset.internal.resource.factory.use;

import org.failearly.dataset.DataSet;
import org.failearly.dataset.Use;
import org.failearly.dataset.internal.resource.factory.DataResourcesFactoryTestBase;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.template.Scope;
import org.failearly.dataset.test.MyTemplateObjectAnnotation;
import org.junit.Test;

import java.util.List;

import static org.failearly.dataset.test.DataResourceMatchers.isDefaultDataResource;

/**
 * UseCleanupResourcesFactoryTest contains tests for {@link UseCleanupResourcesFactory} and it's associated annotation {@link Use}.
 */
@SuppressWarnings("unchecked")
public final class UseCleanupResourcesFactoryTest extends DataResourcesFactoryTestBase<Use, UseCleanupResourcesFactory> {

    private static final String LOCAL_TEMPLATE_OBJECT_CONTENT = "Uses local template objects!";
    private static final String GLOBAL_TEMPLATE_OBJECT_CONTENT = "Uses global template objects!";
    private static final String TO_BE_IGNORED = "$to-be-ignored.description\n";
    private static final String EXPECTED_TEMPLATE_OBJECT_CONTENT = LOCAL_TEMPLATE_OBJECT_CONTENT + "\n" + GLOBAL_TEMPLATE_OBJECT_CONTENT + "\n" + TO_BE_IGNORED;



    public UseCleanupResourcesFactoryTest() {
        super(Use.class, new UseCleanupResourcesFactory(), AnyClass.class);
    }

    /**
     * Basic test for @Use with static datasets. A (test) method uses @Use with at least two interfaces, which uses @DataSet one with default
     * settings ({@link UseDefaultDataSet}) and one with explicit resource ({@link UsesExplicitResource}) should resolve the specified resources.
     */
    @Test
    public void reusable_dataset_interfaces_with_static_data_sets__should_resolve_the_resources_in_reverse_order() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("staticDataSets");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                        //
                isDefaultDataResource("/any-existing-resource.cleanup"),                                                  //
                isDefaultDataResource("/org/failearly/dataset/internal/resource/factory/use/UseDefaultDataSet.cleanup")   //
        );
    }


    /**
     * Test for {@link Use.ReusableDataSet}s which uses {@link Use} itself.
     */
    @Test
    public void reusable_dataset_interface_with_double_use__should_resolve_the_resources_in_reverse_order() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("doubleUse");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                       //
                isDefaultDataResource("/any-existing-resource.cleanup"),                                                  //
                isDefaultDataResource("/org/failearly/dataset/internal/resource/factory/use/UseDefaultDataSet.cleanup")   //
        );
    }

    @Test
    public void reusable_dataset_class__should_resolve_in_reverse_order__top_first_base_last() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("reusableDatasetClassHierarchy");

        // assert / then
        assertResolvedDataResources(dataResources,                                                                     //
                isDefaultDataResource("/org/failearly/dataset/internal/resource/factory/use/ReusableClass.cleanup"),  //
                isDefaultDataResource("/org/failearly/dataset/internal/resource/factory/use/ReusableBaseClass.cleanup") //
        );
    }

    @Test
    public void reusable_dataset_with_template__should_use_template_objects() throws Exception {
        // act / when
        final List<DataResource> dataResources = createDataResourcesFromMethod("reuseWithTemplateObjects");

        // assert / then
        assertDataResourcesContent(dataResources.get(0), EXPECTED_TEMPLATE_OBJECT_CONTENT);
    }

    private static class AnyClass {
        @Use({UseDefaultDataSet.class, UsesExplicitResource.class})
        public void staticDataSets() {
        }

        @Use(ReusableDataSetUsingUse.class)
        public void doubleUse() {
        }

        @Use(ReusableClass.class)
        public void reusableDatasetClassHierarchy() {
        }

        @MyTemplateObjectAnnotation(scope = Scope.GLOBAL, dataset = "<don't care>", name = "global", description = GLOBAL_TEMPLATE_OBJECT_CONTENT)
        @MyTemplateObjectAnnotation(scope = Scope.LOCAL, name = "to-be-ignored", description = "<to be ignored>")
        @Use(ReusableTemplateDataSet.class)
        public void reuseWithTemplateObjects() {
        }
    }

    @DataSet
    private interface UseDefaultDataSet extends Use.ReusableDataSet {
    }

    @DataSet(cleanup = "/any-existing-resource.cleanup")
    private interface UsesExplicitResource extends Use.ReusableDataSet {
    }

    @Use({UseDefaultDataSet.class,UsesExplicitResource.class})
    private interface ReusableDataSetUsingUse extends Use.ReusableDataSet {
    }


    @DataSet
    private static class ReusableBaseClass implements Use.ReusableDataSet {
    }

    @DataSet
    private static class ReusableClass extends ReusableBaseClass {
    }

    @DataSet(cleanup = "template.vm")
    @MyTemplateObjectAnnotation(name = "to", description = LOCAL_TEMPLATE_OBJECT_CONTENT)
    private interface ReusableTemplateDataSet extends Use.ReusableDataSet {
    }
}