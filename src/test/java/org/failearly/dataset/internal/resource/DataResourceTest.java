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

package org.failearly.dataset.internal.resource;
import org.failearly.dataset.DataSet;
import org.failearly.dataset.template.generator.Limit;
import org.failearly.dataset.template.generator.ConstantGenerator;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.DataResourceBuilder;
import org.failearly.dataset.test.TestUtils;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.failearly.dataset.test.TestUtils.inputStreamToString;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * DataSetResourceTest contains tests for {@link DataResource#open()} for different DataResource implementations.
 */
public class DataResourceTest {

    @Test
    public void open_mandatory_none_existing_resource__should_throw_exception() throws Exception {
        // arrange / given
        final DataResource dataResource = createMandatoryDataResource("testMethodWithMissingResource", "noneExisting.setup");

        // assert / then
        assertThat("Resource?", dataResource.getResource(), is("/org/failearly/dataset/internal/resource/noneExisting.setup"));
        TestUtils.assertException(
                MissingDataResourceException.class,
                "Can't open resource '/org/failearly/dataset/internal/resource/noneExisting.setup'. Did you create the resource within classpath?",
                dataResource::open
            );
    }

    @Test
    public void open_optional_none_existing_resource__should_return_empty_content() throws Exception {
        // arrange / given
        final DataResource dataResource = createOptionalDataResource("testMethodWithMissingResource", "noneExisting.setup");

        // assert / then
        assertThat("Resource Name?", dataResource.getResource(), is("/org/failearly/dataset/internal/resource/noneExisting.setup"));
        assertThat("Empty Content?", inputStreamToString(dataResource.open()), is(""));
    }

    @Test
    public void open_existing_resource__should_return_content() throws Exception {
        // arrange / given
        final DataResource dataResource = createMandatoryDataResource("testMethodWithExistingResource", "existing.setup");

        // assert / then
        assertThat("Resource Name?", dataResource.getResource(), is("/org/failearly/dataset/internal/resource/existing.setup"));
        assertThat("Content?", inputStreamToString(dataResource.open()), is("Hello to 'existing.setup',\nAnother line.\n"));
    }

    @Test
    public void open_existing_template_resource__should_return_generated_content() throws Exception {
        // arrange / given
        final DataResource dataResource = createMandatoryDataResource("testMethodWithTemplate", "existing.setup.vm");

        // assert / then
        assertThat("Resource Name?", dataResource.getResource(), is("/org/failearly/dataset/internal/resource/existing.setup.vm"));
        assertThat("Generated Content?",
                        inputStreamToString(dataResource.open()), is("Hello to 'existing.setup.vm'\nHello 'a constant value'\nHello 'a constant value'\n"));
    }

    private static DataResource createMandatoryDataResource(String methodName, String resourceName) throws NoSuchMethodException {
        final DataSet dataSet = resolveDataSet(methodName);
        return DataResourceBuilder.createBuilder(TestClass.class)
                .mandatory()
                .withDataSetName(dataSet.name())
                .withDataStoreId(dataSet.datastore())
                .withResourceName(resourceName)
                .withTemplateObjects(TestUtils.resolveTemplateObjects(methodName, TestClass.class))
                .build();
    }

    private static DataResource createOptionalDataResource(String methodName, String resourceName) throws NoSuchMethodException {
        final DataSet dataSet = resolveDataSet(methodName);
        return DataResourceBuilder.createBuilder(TestClass.class)
                .optional()
                .withDataSetName(dataSet.name())
                .withDataStoreId(dataSet.datastore())
                .withResourceName(resourceName)
                .withTemplateObjects(TestUtils.resolveTemplateObjects(methodName, TestClass.class))
                .build();
    }

    private static Method getTestMethod(String methodName) throws NoSuchMethodException {
        return TestClass.class.getDeclaredMethod(methodName);
    }

    private static DataSet resolveDataSet(String methodName) throws NoSuchMethodException {
        return getTestMethod(methodName).getAnnotation(DataSet.class);
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class TestClass {
        @DataSet(name="DS1", setup = "noneExisting.setup")
        public void testMethodWithMissingResource() {
        }
        @DataSet(name="DS2", setup = "existing.setup")
        public void testMethodWithExistingResource() {
        }
        @DataSet(name="DS3", setup = "existing.setup.vm")
        @ConstantGenerator(name="const", dataset = "DS3", constant = "a constant value", count = 2, limit = Limit.LIMITED)
        @ConstantGenerator(name="const", dataset = "DS2", constant = "not used otherwise exception", limit = Limit.UNLIMITED)
        public void testMethodWithTemplate() {
        }
    }


}
