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

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * PropertiesHelper provides utility methods to type safe values from the properties map.
 */
public final class PropertiesHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesHelper.class);
    private final Map<String,String> map;

    public PropertiesHelper(Properties properties) {
        this(toMap(properties));
    }

    private static Map<String,String> toMap(Properties properties) {
        final Map<String,String> map=new HashMap<>();
        for (Map.Entry<Object, Object> property : properties.entrySet()) {
            map.put(
                    Objects.toString(property.getKey(), "<unknown key>"),
                    Objects.toString(property.getValue(), "<unknown value>")
            );
        }
        return map;
    }

    public PropertiesHelper(Map<String, String> map) {
        this.map = Collections.unmodifiableMap(new HashMap<>(map));
    }


    public Map<String,String> getMap() {
        return this.map;
    }

    public String getStringValue(String key) {
        return doGetValue(key);
    }

    public String getStringValue(String key, String defaultValue) {
        return doGetValueWithDefault(key, defaultValue, Function.identity());
    }

    public <T> T getGenericValue(String key, Function<String,T> converter) {
        final String value=doGetValue(key);
        return converter.apply(value);
    }

    public <T> T getGenericValue(String key, T defaultValue, Converter<T> converter) {
        return doGetValueWithDefault(key, defaultValue, converter);
    }

    public boolean getBooleanValue(String key) {
        return getGenericValue(key, PropertiesHelper::toBoolean);
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        return doGetValueWithDefault(key, defaultValue, PropertiesHelper::toBoolean);
    }

    public int getIntValue(String key) {
        return getGenericValue(key, Integer::valueOf);
    }

    public int getIntValue(String key, int defaultValue) {
        return doGetValueWithDefault(key, defaultValue, Integer::valueOf);
    }

    public <E extends Enum<E>> E getEnumValue(String key, Class<E> enumType) {
        return getGenericValue(key, value -> Enum.valueOf(enumType, value));
    }

    public <E extends Enum<E>> E getEnumValue(String key, Class<E> enumType, E defaultValue) {
        return doGetValueWithDefault(key, defaultValue, value -> Enum.valueOf(enumType, value));
    }

    private static Boolean toBoolean(String value) {
        final Boolean booleanObject = BooleanUtils.toBooleanObject(value);
        if( booleanObject != null)
            return booleanObject;
        LOGGER.error("Unknown boolean value: {}!", value);
        throw new IllegalArgumentException("Unknown boolean value: " + value);
    }


    private String doGetValue(String key) {
        final String value = map.get(key);
        if( value!=null )
            return value;

        LOGGER.error("Unknown key ({}) or value is <null>!", key);
        throw new IllegalArgumentException("Unknown key '" + key + "' or value is <null>");
    }

    private <T> T doGetValueWithDefault(String key, T defaultValue, Function<String,T> converter) {
        try {
            return getGenericValue(key, converter);
        }
        catch(Exception e) {
            LOGGER.warn("Use default value '{}' for key '{}'", defaultValue, key);
        }

        return defaultValue;
    }


    @FunctionalInterface
    public interface Converter<T> extends Function<String,T> {}
}
