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

package org.failearly.dataz.resource;

import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.internal.template.TemplateObjectsTestHelper;
import org.junit.Test;

import static org.failearly.common.test.ExceptionVerifier.on;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * DataResourceBuilderTest contains tests for {@link DataResourceBuilder} .
 */
public class DataResourceBuilderTest {

    private static TemplateObjects NO_TEMPLATE_OBJECTS = TemplateObjectsTestHelper.noTemplateObjects();

    @Test
    public void all_mandatory_fields_set__should_build_an_instance() throws Exception {
        // act / when
        final DataResource dataResource = DataResourceBuilder.createBuilder(ATestClass.class) //
                .optional()                                                             //
                .withTemplateObjects(NO_TEMPLATE_OBJECTS)                           //
                .withDataSetName("DS")                                                  //
                .withResourceName("/any-resource-name.setup")                           //
                .build();

        // assert / then
        assertThat("return an (not null) instance?", dataResource, is(notNullValue()));
    }

    @Test
    public void optional_and_not_existing__should_return_NOOP_data_resource() throws Exception {
        // act / when
        final DataResource dataResource = DataResourceBuilder.createBuilder(ATestClass.class) //
                .optional()                                                             //
                .withTemplateObjects(NO_TEMPLATE_OBJECTS)                                      //
                .withDataSetName("DS")                                                  //
                .withResourceName("/not-existing-resource.setup")                           //
                .build();

        // assert / then
        assertThat("returns ignoring DataResource?", dataResource.getClass().getSimpleName(), is("IgnoringDataResource"));
    }

    @Test
    public void mandatory_and_not_existing__should_return_missing_resource_data_resource() throws Exception {
        // act / when
        final DataResource dataResource = DataResourceBuilder.createBuilder(ATestClass.class) //
                .mandatory()                                                             //
                .withTemplateObjects(NO_TEMPLATE_OBJECTS)                                      //
                .withDataSetName("DS")                                                  //
                .withResourceName("/not-existing-resource.setup")                           //
                .build();

        // assert / then
        assertThat("returns MissingResource DataResource?", dataResource.getClass().getSimpleName(), is("MissingResourceDataResource"));
    }

    @Test
    public void existing_simple_file__should_return_standard_data_resource() throws Exception {
        // act / when
        final DataResource dataResource = DataResourceBuilder.createBuilder(ATestClass.class) //
                .optional()                                                             //
                .withTemplateObjects(NO_TEMPLATE_OBJECTS)                                      //
                .withDataSetName("DS")                                                  //
                .withResourceName("/any-existing-resource.setup")                        //
                .build();

        // assert / then
        assertThat("returns Standard DataResource?", dataResource.getClass().getSimpleName(), is("StandardDataResource"));
    }

    @Test
    public void existing_template_file__should_return_template_data_resource() throws Exception {
        // act / when
        final DataResource dataResource = DataResourceBuilder.createBuilder(ATestClass.class) //
                .optional()                                                             //
                .withTemplateObjects(NO_TEMPLATE_OBJECTS)                           //
                .withDataSetName("DS")                                                  //
                .withResourceName("/any-existing-resource.setup.vm")                    //
                .build();

        // assert / then
        assertThat("returns Template DataResource?", dataResource.getClass().getSimpleName(), is("TemplateDataResource"));
    }


    @Test
    public void any_mandatory_field_missing__should_throw_exception() throws Exception {
        // assert / then
        on(() -> DataResourceBuilder.createBuilder(ATestClass.class).build())                                      //
                .expect(IllegalStateException.class)                                                               //
                .expect("DataResourceBuilder: Mandatory field 'mandatory/optional' missing (must not be null)!")   //
                .verify();

        on(() -> DataResourceBuilder.createBuilder(ATestClass.class).optional().build())                           //
                .expect(IllegalStateException.class)                                                               //
                .expect("DataResourceBuilder: Mandatory field 'templateObjects' missing (must not be null)!")      //
                .verify();

        on(() -> DataResourceBuilder.createBuilder(ATestClass.class).mandatory().build())
                .expect(IllegalStateException.class)
                .expect("DataResourceBuilder: Mandatory field 'templateObjects' missing (must not be null)!")
                .verify();

        on(() -> DataResourceBuilder.createBuilder(ATestClass.class).optional().withTemplateObjects(NO_TEMPLATE_OBJECTS).build()) //
                .expect(IllegalStateException.class)                                                                              //
                .expect("Builder: Mandatory field 'dataSetName' missing (must not be null)!")                                     //
                .verify();

        on(() -> DataResourceBuilder.createBuilder(ATestClass.class)                      //
                                    .optional()                                           //
                                    .withTemplateObjects(NO_TEMPLATE_OBJECTS)             //
                                    .withDataSetName("DataSetName")                       //
                                    .build())                                                   //
                .expect(IllegalStateException.class)                                            //
                .expect("Builder: Mandatory field 'resourceName' missing (must not be null)!")  //
                .verify();
    }

    // Just for satisfy the interfaces
    public static class ATestClass {
        @Test
        public void anyTestMethod() {
        }
    }

}