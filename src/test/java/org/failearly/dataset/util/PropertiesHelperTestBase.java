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

import org.failearly.dataset.internal.util.BuilderBase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * PropertiesHelperTestBase is responsible for ...
 */
public class PropertiesHelperTestBase {
    protected PropertiesHelper propertiesHelper;

    private static PropertyMapBuilder mapBuilder() {
        return new PropertyMapBuilder();
    }

    protected static Map<String, String> map(String key, Object value) {
        return mapBuilder().put(key, value).build();
    }

    @Before
    public void setUp() throws Exception {
        final Map<String, String> propertyMap = mapBuilder()
                .put("string", "string-value")
                .put("int", 42)
                .put("boolean", false)
                .put("enum", AnyEnum.VAL0)
                .put("float", 3.14f)
                .build();
        propertiesHelper = new PropertiesHelper(propertyMap);
    }

    @Test
    public void MapBuilder__should_create_the_correct_string_values() throws Exception {
        assertThat(propertiesHelper.getStringValue("string"), is("string-value"));
        assertThat(propertiesHelper.getStringValue("int"), is("42"));
        assertThat(propertiesHelper.getStringValue("boolean"), is("false"));
        assertThat(propertiesHelper.getStringValue("enum"), is("VAL0"));
        assertThat(propertiesHelper.getStringValue("float"), is("3.14"));
    }

    protected enum AnyEnum {
        VAL0,
        VAL1
    }

    private static class PropertyMapBuilder extends BuilderBase<Map<String, String>> {

        private final Map<String, String> map = new HashMap<>();

        PropertyMapBuilder put(String key, Object value) {
            map.put(key, Objects.toString(value, null));
            return this;
        }

        @Override
        protected Map<String, String> doBuild() {
            return map;
        }
    }
}
