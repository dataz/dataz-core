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

package org.failearly.dataz.internal.common.proputils;

import org.failearly.dataz.internal.common.test.ExceptionVerifier;
import org.failearly.dataz.internal.common.test.annotations.Subject;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * ExtendedPropertiesTest contains tests for ExtendedProperties
 */
@Subject(ExtendedProperties.class)
public class ExtendedPropertiesTest {
    private final ExtendedProperties extendedProperties = new ExtendedProperties();


    @Test
    public void what_happens_if_origin_properties_set_after_construction() throws Exception {
        // arrange / given
        final Properties properties=new Properties();
        properties.setProperty("var0", "value0");

        // act / when
        final ExtendedProperties extendedProperties = new ExtendedProperties(properties);
        properties.setProperty("var1", "value1");


        // assert / then
        assertThat("Copied all properties?", extendedProperties.getProperty("var0"), is("value0"));
        assertThat("But keep no reference?", extendedProperties.getProperty("var1"), is(nullValue()));
    }

    @Test
    public void what_happens_if_origin_properties_set_after_merge() throws Exception {
        // arrange / given
        final Properties properties=new Properties();
        properties.setProperty("var0", "value0");

        // act / when
        this.extendedProperties.merge(properties);
        properties.setProperty("var1", "value1");


        // assert / then
        assertThat("Copied all properties?", extendedProperties.getProperty("var0"), is("value0"));
        assertThat("But keep no reference?", extendedProperties.getProperty("var1"), is(nullValue()));
    }

    @Test
    public void how_to_act_with_normal_properties() throws Exception {
        // arrange / given
        extendedProperties.setProperty("var0","value0");
        extendedProperties.setProperty("var1","value1");

        // act / when

        // assert / then
        assertThat("Value of var1?", extendedProperties.getProperty("var1"), is("value1"));
    }

     @Test
    public void what_will_be_resolved_if_the_reference_var_is_not_set() throws Exception {
        // arrange / given
        extendedProperties.setProperty("var1","${unknown.var}");

        // act / when

        // assert / then
        assertThat("Value of var1?", extendedProperties.getProperty("var1"), is("?{unknown.var}"));
    }

    @Test
    public void how_to_resolve_single_reference() throws Exception {
        // arrange / given
        extendedProperties.setProperty("var0","value0");
        extendedProperties.setProperty("var1","${var0}");

        // act / when

        // assert / then
        assertThat("Value of var1?", extendedProperties.getProperty("var1"), is("value0"));
    }

    @Test
    public void how_to_resolve_multiple_references() throws Exception {
        // arrange / given
        extendedProperties.setProperty("var0","value0");
        extendedProperties.setProperty("var1","${var0},${var0}");

        // act / when

        // assert / then
        assertThat("Value of var1?", extendedProperties.getProperty("var1"), is("value0,value0"));
    }

    @Test
    public void how_to_resolve_cascading_references() throws Exception {
        // arrange / given
        extendedProperties.setProperty("var0","value0");
        extendedProperties.setProperty("var1","${var0}");
        extendedProperties.setProperty("var2","${var1}");
        extendedProperties.setProperty("var3","${var2}");

        // act / when

        // assert / then
        assertThat("Value of var3?", extendedProperties.getProperty("var3"), is("value0"));
    }

    @Test
    public void what_is_the_result_of_a_complex_settings() throws Exception {
        // arrange / given
        extendedProperties.setProperty("var0","value0");
        extendedProperties.setProperty("var1","${var0}");
        extendedProperties.setProperty("var2","${var1}");
        extendedProperties.setProperty("var3","v2=${var2},v1=${var1},v4=${var4}");

        // act / when

        // assert / then
        assertThat("Value of var3?", extendedProperties.getProperty("var3"), is("v2=value0,v1=value0,v4=?{var4}"));
        assertThat("Value of var2?", extendedProperties.getProperty("var2"), is("value0"));
        assertThat("Value of var1?", extendedProperties.getProperty("var1"), is("value0"));
        assertThat("Value of var0?", extendedProperties.getProperty("var0"), is("value0"));
    }


    @Test
    public void what_to_do_with_a_self_reference() throws Exception {
        // arrange / given
        extendedProperties.setProperty("var0","${var0}");

        // act / when
        assertEndlessDetection("var0");
    }

    @Test
    public void what_to_do_with_a_looped_references() throws Exception {
        // arrange / given
        extendedProperties.setProperty("var0","${var1}");
        extendedProperties.setProperty("var1","${var0}");

        // act / when
        assertEndlessDetection("var0");
        assertEndlessDetection("var1");
    }

    @Test
    public void what_to_do_with_a_more_complicated_looped_references() throws Exception {
        // arrange / given
        extendedProperties.setProperty("var0","${var2}");
        extendedProperties.setProperty("var1","${var0}");
        extendedProperties.setProperty("var2","${var1}");

        // act / when
        assertEndlessDetection("var2");
    }

    @Test
    public void how_to_react_if_accessing_a_valid_property_but_the_other_property_is_invalid() throws Exception {
        // arrange / given
        extendedProperties.setProperty("var0","value0");
        extendedProperties.setProperty("var1","${var1}");

        // act / when
        assertEndlessDetection("var0");
    }

    public void assertEndlessDetection(String key) {

        ExceptionVerifier.on(()->extendedProperties.getProperty(key))
                .expect(IllegalArgumentException.class)
                .expect(CoreMatchers.allOf(startsWith("Recursion on property key '"), endsWith("' detected.")))
                .verify();

    }

    @Test
    public void how_to_act_with_special_property_values() throws Exception {
        // arrange / given
        extendedProperties.setProperty("existing","  existing-value  ");
        extendedProperties.setProperty("existing-but-empty","");
        extendedProperties.setProperty("existing-but-blank","   \n\r\t ");
        extendedProperties.setProperty("existing-but-use-empty","(empty)");
        extendedProperties.setProperty("existing-but-use-null","(null)");


        // assert / then
        assertThat("Existing property?", extendedProperties.getProperty("existing"), is("existing-value"));
        assertThat("Existing but empty?", extendedProperties.getProperty("existing-but-empty"), is(""));
        assertThat("Existing but blank?", extendedProperties.getProperty("existing-but-blank"), is(""));
        assertThat("Existing with using (empty)?", extendedProperties.getProperty("existing-but-use-empty"), is(equalTo("")));
        assertThat("Existing with using (null)?", extendedProperties.getProperty("existing-but-use-null"), nullValue());
        assertThat("Not existing?", extendedProperties.getProperty("not-existing"), nullValue());
    }
}
