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

import org.failearly.dataz.common.test.ExceptionVerifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * PropertiesHelperTest contains tests for ... .
 */
public class PropertiesAccessor_getXxxValue_Test extends PropertiesAccessorTestBase {

    @Test
    public void on_valid_key_and_value__should_return_value_in_expected_type() throws Exception {
        assertThat(propertiesAccessor.getIntValue("int"), is(42));
        assertThat(propertiesAccessor.getBooleanValue("boolean"), is(false));
        assertThat(propertiesAccessor.getEnumValue("enum", AnyEnum.class), is(AnyEnum.VAL0));
        assertThat(propertiesAccessor.getGenericValue("float", Float::valueOf), is(3.14f));
    }

    @Test
    public void on_unknown_key__should_throw_exception() throws Exception {
        ExceptionVerifier.on(() -> propertiesAccessor.getStringValue("unknown"))
            .expect(IllegalArgumentException.class).expect("Unknown key 'unknown' or value is <null>").verify();
        ExceptionVerifier.on(() -> propertiesAccessor.getIntValue("unknown"))
            .expect(IllegalArgumentException.class).expect("Unknown key 'unknown' or value is <null>").verify();
        ExceptionVerifier.on(() -> propertiesAccessor.getBooleanValue("unknown"))
            .expect(IllegalArgumentException.class).expect("Unknown key 'unknown' or value is <null>").verify();
        ExceptionVerifier.on(() -> propertiesAccessor.getEnumValue("unknown", AnyEnum.class))
            .expect(IllegalArgumentException.class).expect("Unknown key 'unknown' or value is <null>").verify();
        ExceptionVerifier.on(() -> propertiesAccessor.getGenericValue("unknown", Float::valueOf))
            .expect(IllegalArgumentException.class).expect("Unknown key 'unknown' or value is <null>").verify();
    }

    @Test
    public void on_null_value__should_throw_exception() throws Exception {
        // arrange / given
        final String key="key";
        final PropertiesAccessor propertiesAccessor =new PropertiesAccessor(map(key, null));

        // assert / then
        ExceptionVerifier.on(() -> propertiesAccessor.getStringValue(key))
            .expect(IllegalArgumentException.class).expect("Unknown key 'key' or value is <null>").verify();
        ExceptionVerifier.on(() -> propertiesAccessor.getIntValue(key))
            .expect(IllegalArgumentException.class).expect("Unknown key 'key' or value is <null>").verify();
        ExceptionVerifier.on(() -> propertiesAccessor.getBooleanValue(key))
            .expect(IllegalArgumentException.class)
            .expect("Unknown key 'key' or value is <null>").verify();
        ExceptionVerifier.on(() -> propertiesAccessor.getEnumValue(key, AnyEnum.class))
            .expect(IllegalArgumentException.class).expect("Unknown key 'key' or value is <null>").verify();
        ExceptionVerifier.on(() -> propertiesAccessor.getGenericValue(key, Float::valueOf)).expect(IllegalArgumentException.class).expect("Unknown key 'key' or value is <null>").verify();
    }

    @Test
    public void on_invalid_value__should_throw_exception__but_not_for_for_getValue() throws Exception {
        // arrange / given
        final String key="key";
        final PropertiesAccessor propertiesAccessor =new PropertiesAccessor(map(key, "<invalid value>"));

        // assert / then
        ExceptionVerifier.on(() -> propertiesAccessor.getIntValue(key)).expect(NumberFormatException.class).verify();
        ExceptionVerifier.on(() -> propertiesAccessor.getBooleanValue(key)).expect(IllegalArgumentException.class).expect("Unknown boolean value: <invalid value>").verify();
        ExceptionVerifier.on(() -> propertiesAccessor.getEnumValue(key, AnyEnum.class)).expect(IllegalArgumentException.class).verify();
        ExceptionVerifier.on(() -> propertiesAccessor.getGenericValue(key, Float::valueOf)).expect(NumberFormatException.class).verify();
        // but not ..
        assertThat(propertiesAccessor.getStringValue(key), is("<invalid value>"));
    }
}