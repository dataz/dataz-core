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

package org.failearly.dataz.common;

import org.failearly.dataz.internal.common.proputils.PropertiesAccessor;
import org.failearly.dataz.common.test.ExceptionVerifier;
import org.failearly.dataz.common.test.annotations.Subject;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * PropertyUtilityTest contains tests for ... .
 */
@Subject({PropertyUtility.class, Property.class})
public class PropertyUtilityTest {

    @Test
    public void how_to_access_annotation_properties() throws Exception {
        // arrange / given

        // act / when
        final List<PropertiesAnnotation> propertyAnnotations = resolvePropertiesAnnotations();

        // assert / then
        assertThat("Got annotations?", propertyAnnotations.size(), is(2));
    }

    @Test
    public void how_to_convert_to_standard_properties() throws Exception {
        // arrange / given
        final PropertiesAnnotation propertyAnnotation = getPropertiesAnnotation(0);

        // act / when
        final Properties standardProperties = PropertyUtility.toProperties(propertyAnnotation.properties());

        // assert / then
        assertThat("Contains all property names?",  standardProperties.keySet(), is(Matchers.containsInAnyOrder("p0", "p1")));
        assertThat("Contains all property values?", standardProperties.values(), is(Matchers.containsInAnyOrder("v0", "v1")));
        assertThat("Has correct assignment for p0?",  standardProperties.getProperty("p0"), is("v0"));
        assertThat("Has correct assignment for p1?",  standardProperties.getProperty("p1"), is("v1"));
    }

    @Test
    public void what_happens_with_duplicated_values_converting_to_standard_properties() throws Exception {
        // arrange / given
        final PropertiesAnnotation propertyAnnotation = getPropertiesAnnotation(1);

        // act / when
        // assert / then
        ExceptionVerifier.on(()->PropertyUtility.toProperties(propertyAnnotation.properties()))
                .expect(IllegalArgumentException.class)
                .expect("Duplicated properties: " + DUP_KEY_1 + ", " + DUP_KEY_2)
                .verify();
    }

    @Test
    public void how_to_convert_to_property_accessor() throws Exception {
        final PropertiesAnnotation propertyAnnotation = getPropertiesAnnotation(0);

        // act / when
        final PropertiesAccessor propertyAccessor = PropertyUtility.toPropertyAccessor(propertyAnnotation.properties());

        // assert / then
        assertThat("Has correct assignment for p0?",  propertyAccessor.getStringValue("p0"), is("v0"));
        assertThat("Has correct assignment for p1?",  propertyAccessor.getStringValue("p1"), is("v1"));
    }

    @Test
    public void what_happens_with_duplicated_values_converting_to_properties_accessor() throws Exception {
        // arrange / given
        final PropertiesAnnotation propertyAnnotation = getPropertiesAnnotation(1);

        // act / when
        // assert / then
        ExceptionVerifier.on(()->PropertyUtility.toPropertyAccessor(propertyAnnotation.properties()))
                .expect(IllegalArgumentException.class)
                .expect("Duplicated properties: " + DUP_KEY_1 + ", " + DUP_KEY_2)
                .verify();
    }

    private static PropertiesAnnotation getPropertiesAnnotation(int index) {
        final List<PropertiesAnnotation> propertyAnnotations = resolvePropertiesAnnotations();
        return propertyAnnotations.get(index);
    }

    private static List<PropertiesAnnotation> resolvePropertiesAnnotations() {
        final PropertiesAnnotation[] annotations = AnnotatedClass.class.getDeclaredAnnotationsByType(PropertiesAnnotation.class);
        return Arrays.asList(annotations);
    }

    private static final String DONT_CARE = "don't care";
    private static final String DUP_KEY_1="Duplicated-key-1";
    private static final String DUP_KEY_2="Duplicated-key-2";

    @PropertiesAnnotation(properties = {@Property(k="p0", v="v0"), @Property(k="p1", v="v1")})
    @PropertiesAnnotation(properties = {
            @Property(k=DUP_KEY_1, v= DONT_CARE), @Property(k=DUP_KEY_2, v=DONT_CARE),
            @Property(k=DUP_KEY_2, v=DONT_CARE), @Property(k=DUP_KEY_1, v=DONT_CARE)
        })
    private static class AnnotatedClass {}

    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(PropertiesAnnotation.List.class)
    // Must be public!
    public @interface PropertiesAnnotation {
        Property[] properties() default {};

        @Retention(RetentionPolicy.RUNTIME)
        @interface List {
            PropertiesAnnotation[] value();
        }

    }
}