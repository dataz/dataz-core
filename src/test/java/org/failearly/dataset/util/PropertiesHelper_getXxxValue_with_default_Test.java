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
public class PropertiesHelper_getXxxValue_with_default_Test extends PropertiesHelperTestBase {

    @Test
    public void on_valid_key_and_value__should_return_value_in_expected_type() throws Exception {
        assertThat(propertiesHelper.getStringValue("string", "default"), is("string-value"));
        assertThat(propertiesHelper.getIntValue("int",0), is(42));
        assertThat(propertiesHelper.getBooleanValue("boolean", true), is(false));
        assertThat(propertiesHelper.getEnumValue("enum", AnyEnum.class, AnyEnum.VAL1), is(AnyEnum.VAL0));
        assertThat(propertiesHelper.getGenericValue("float", 2.8f, Float::valueOf), is(3.14f));
    }

    @Test
    public void on_unknown_key__should_return_default_value() throws Exception {
        final String unknownKey = "unknown";
        assertThat(propertiesHelper.getStringValue(unknownKey, "default"), is("default"));
        assertThat(propertiesHelper.getIntValue(unknownKey, 0), is(0));
        assertThat(propertiesHelper.getBooleanValue(unknownKey, true), is(true));
        assertThat(propertiesHelper.getEnumValue(unknownKey, AnyEnum.class, AnyEnum.VAL1), is(AnyEnum.VAL1));
        assertThat(propertiesHelper.getGenericValue(unknownKey, 2.8f, Float::valueOf), is(2.8f));
    }

    @Test
    public void on_null_value__should_return_default_value() throws Exception {
        // arrange / given
        final String key="key";
        final PropertiesHelper propertiesHelper = new PropertiesHelper(map(key, null));

        // assert / then
        assertThat(propertiesHelper.getStringValue(key, "default"), is("default"));
        assertThat(propertiesHelper.getIntValue(key, 0), is(0));
        assertThat(propertiesHelper.getBooleanValue(key, true), is(true));
        assertThat(propertiesHelper.getEnumValue(key, AnyEnum.class, AnyEnum.VAL1), is(AnyEnum.VAL1));
        assertThat(propertiesHelper.getGenericValue(key, 2.8f, Float::valueOf), is(2.8f));
    }

    @Test
    public void on_invalid_value__should_return_default_value__but_not_for_getStringValue() throws Exception {
        // arrange / given
        final String key="key";
        final PropertiesHelper propertiesHelper = new PropertiesHelper(map(key, "<invalid value>"));

        // assert / then
        assertThat(propertiesHelper.getIntValue(key, 0), is(0));
        assertThat(propertiesHelper.getBooleanValue(key, true), is(true));
        assertThat(propertiesHelper.getEnumValue(key, AnyEnum.class, AnyEnum.VAL1), is(AnyEnum.VAL1));
        assertThat(propertiesHelper.getGenericValue(key, 2.8f, Float::valueOf), is(2.8f));
        // but not ..
        assertThat(propertiesHelper.getStringValue(key,"default-value"), is("<invalid value>"));
    }
}