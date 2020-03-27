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

import org.failearly.dataz.internal.common.builder.BuilderBase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * PropertiesAccessorTestBase is responsible for ...
 */
public abstract class PropertiesAccessorTestBase {
    PropertiesAccessor propertiesAccessor;

    private static PropertyMapBuilder mapBuilder() {
        return new PropertyMapBuilder();
    }

    static Map<String, String> map(String key, Object value) {
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
                .put(null, "unknown-key")
                .build();
        propertiesAccessor = new PropertiesAccessor(propertyMap);
    }

    @Test
    public void what_are_the_string_values() throws Exception {
        assertThat(propertiesAccessor.getStringValue("string"), is("string-value"));
        assertThat(propertiesAccessor.getStringValue("int"), is("42"));
        assertThat(propertiesAccessor.getStringValue("boolean"), is("false"));
        assertThat(propertiesAccessor.getStringValue("enum"), is("VAL0"));
        assertThat(propertiesAccessor.getStringValue(null), is("unknown-key"));
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
