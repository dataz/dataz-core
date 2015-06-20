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

package org.failearly.dataset.util;

import org.junit.Test;

import static org.failearly.dataset.test.AssertException.assertException;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * PropertiesHelperTest contains tests for ... .
 */
public class PropertiesHelper_getXxxValue_Test extends PropertiesHelperTestBase {

    @Test
    public void on_valid_key_and_value__should_return_value_in_expected_type() throws Exception {
        assertThat(propertiesHelper.getIntValue("int"), is(42));
        assertThat(propertiesHelper.getBooleanValue("boolean"), is(false));
        assertThat(propertiesHelper.getEnumValue("enum", AnyEnum.class), is(AnyEnum.VAL0));
        assertThat(propertiesHelper.getGenericValue("float", Float::valueOf), is(3.14f));
    }

    @Test
    public void on_unknown_key__should_throw_exception() throws Exception {
        assertException(IllegalArgumentException.class, "Unknown key 'unknown' or value is <null>", () -> propertiesHelper.getStringValue("unknown"));
        assertException(IllegalArgumentException.class, "Unknown key 'unknown' or value is <null>", () -> propertiesHelper.getIntValue("unknown"));
        assertException(IllegalArgumentException.class, "Unknown key 'unknown' or value is <null>", () -> propertiesHelper.getBooleanValue("unknown"));
        assertException(IllegalArgumentException.class, "Unknown key 'unknown' or value is <null>", () -> propertiesHelper.getEnumValue("unknown", AnyEnum.class));
        assertException(IllegalArgumentException.class, "Unknown key 'unknown' or value is <null>", () -> propertiesHelper.getGenericValue("unknown", Float::valueOf));
    }

    @Test
    public void on_null_value__should_throw_exception() throws Exception {
        // arrange / given
        final String key="key";
        final PropertiesHelper propertiesHelper = new PropertiesHelper(map(key, null));

        // assert / then
        assertException(IllegalArgumentException.class, "Unknown key 'key' or value is <null>", () -> propertiesHelper.getStringValue(key));
        assertException(IllegalArgumentException.class, "Unknown key 'key' or value is <null>", () -> propertiesHelper.getIntValue(key));
        assertException(IllegalArgumentException.class, "Unknown key 'key' or value is <null>", () -> propertiesHelper.getBooleanValue(key));
        assertException(IllegalArgumentException.class, "Unknown key 'key' or value is <null>", () -> propertiesHelper.getEnumValue(key, AnyEnum.class));
        assertException(IllegalArgumentException.class, "Unknown key 'key' or value is <null>", () -> propertiesHelper.getGenericValue(key, Float::valueOf));
    }

    @Test
    public void on_invalid_value__should_throw_exception__but_not_for_for_getValue() throws Exception {
        // arrange / given
        final String key="key";
        final PropertiesHelper propertiesHelper = new PropertiesHelper(map(key, "<invalid value>"));

        // assert / then
        assertException(NumberFormatException.class, () -> propertiesHelper.getIntValue(key));
        assertException(IllegalArgumentException.class, "Unknown boolean value: <invalid value>", () -> propertiesHelper.getBooleanValue(key));
        assertException(IllegalArgumentException.class, () -> propertiesHelper.getEnumValue(key, AnyEnum.class));
        assertException(NumberFormatException.class, () -> propertiesHelper.getGenericValue(key, Float::valueOf));
        // but not ..
        assertThat(propertiesHelper.getStringValue(key), is("<invalid value>"));
    }
}