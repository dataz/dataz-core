/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.internal.common.proputils;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * PropertiesHelperTest contains tests for ... .
 */
public class PropertiesAccessor_getXxxValue_with_default_Test extends PropertiesAccessorTestBase {

    @Test
    public void on_valid_key_and_value__should_return_value_in_expected_type() throws Exception {
        assertThat(propertiesAccessor.getStringValue("string", "default"), is("string-value"));
        assertThat(propertiesAccessor.getIntValue("int",0), is(42));
        assertThat(propertiesAccessor.getBooleanValue("boolean", true), is(false));
        assertThat(propertiesAccessor.getEnumValue("enum", AnyEnum.class, AnyEnum.VAL1), is(AnyEnum.VAL0));
        assertThat(propertiesAccessor.getGenericValue("float", 2.8f, Float::valueOf), is(3.14f));
    }

    @Test
    public void on_unknown_key__should_return_default_value() throws Exception {
        final String unknownKey = "unknown";
        assertThat(propertiesAccessor.getStringValue(unknownKey, "default"), is("default"));
        assertThat(propertiesAccessor.getIntValue(unknownKey, 0), is(0));
        assertThat(propertiesAccessor.getBooleanValue(unknownKey, true), is(true));
        assertThat(propertiesAccessor.getEnumValue(unknownKey, AnyEnum.class, AnyEnum.VAL1), is(AnyEnum.VAL1));
        assertThat(propertiesAccessor.getGenericValue(unknownKey, 2.8f, Float::valueOf), is(2.8f));
    }

    @Test
    public void on_null_value__should_return_default_value() throws Exception {
        // arrange / given
        final String key="key";
        final PropertiesAccessor propertiesAccessor = new PropertiesAccessor(map(key, null));

        // assert / then
        assertThat(propertiesAccessor.getStringValue(key, "default"), is("default"));
        assertThat(propertiesAccessor.getIntValue(key, 0), is(0));
        assertThat(propertiesAccessor.getBooleanValue(key, true), is(true));
        assertThat(propertiesAccessor.getEnumValue(key, AnyEnum.class, AnyEnum.VAL1), is(AnyEnum.VAL1));
        assertThat(propertiesAccessor.getGenericValue(key, 2.8f, Float::valueOf), is(2.8f));
    }

    @Test
    public void on_invalid_value__should_return_default_value__but_not_for_getStringValue() throws Exception {
        // arrange / given
        final String key="key";
        final PropertiesAccessor propertiesAccessor = new PropertiesAccessor(map(key, "<invalid value>"));

        // assert / then
        assertThat(propertiesAccessor.getIntValue(key, 0), is(0));
        assertThat(propertiesAccessor.getBooleanValue(key, true), is(true));
        assertThat(propertiesAccessor.getEnumValue(key, AnyEnum.class, AnyEnum.VAL1), is(AnyEnum.VAL1));
        assertThat(propertiesAccessor.getGenericValue(key, 2.8f, Float::valueOf), is(2.8f));
        // but not ..
        assertThat(propertiesAccessor.getStringValue(key,"default-value"), is("<invalid value>"));
    }
}