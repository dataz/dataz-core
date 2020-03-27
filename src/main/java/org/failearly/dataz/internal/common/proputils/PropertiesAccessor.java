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

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * PropertiesAccessor provides utility methods to type safe values from the properties map.
 */
@SuppressWarnings("WeakerAccess")
public final class PropertiesAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesAccessor.class);
    private static final ImmutableMap<String, Boolean> BOOLEAN_VALUES;
    private final Map<String,String> map;

    static {
        BOOLEAN_VALUES = ImmutableMap.<String,Boolean>builder() //
            .put("true", true)
            .put("false", false)
            .put("on", true)
            .put("off", false)
            .put("yes", true)
            .put("no", false)
            .build();

    }

    public PropertiesAccessor(Properties properties) {
        this(toMap(properties));
    }

    private static Map<String,String> toMap(Properties properties) {
        final Map<String,String> map=new HashMap<>();
        for (Map.Entry<Object, Object> property : properties.entrySet()) {
            map.put(
                    Objects.toString(property.getKey(), "<unknown key>"),
                    Objects.toString(property.getValue(), "<unknown value for " + property.getKey() + ">")
            );
        }
        return map;
    }

    public PropertiesAccessor(Map<String, String> map) {
        this.map = new HashMap<>(map);
    }


    public Map<String,String> getMap() {
        return Collections.unmodifiableMap(this.map);
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
        return getGenericValue(key, PropertiesAccessor::toBoolean);
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        return doGetValueWithDefault(key, defaultValue, PropertiesAccessor::toBoolean);
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
        final Boolean boolVal = BOOLEAN_VALUES.get(value.toLowerCase());
        if( boolVal==null ) {
            LOGGER.error("Unknown boolean value: {}!", value);
            throw new IllegalArgumentException("Unknown boolean value: " + value);
        }
        return boolVal;
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

    /**
     * Return the value of the key or throw {@link MissingPropertyException}, if the property is unknown or the value is
     * {@code null}, empty or blank.
     *
     * @param key the property key.
     * @return the value.
     *
     * @throws MissingPropertyException in case of unknown property key or empty or {@code null} value.
     */
    public String getMandatoryProperty(String key) throws MissingPropertyException {
        final String value = doGetValue(key);
        if( null==value ) {
            throw new MissingPropertyException(key);
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertiesAccessor)) return false;
        PropertiesAccessor that = (PropertiesAccessor) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }



    @FunctionalInterface
    public interface Converter<T> extends Function<String,T> {}

    @Override
    public String toString() {
        return "PropertiesAccessor{" +
                "map=" + map +
                '}';
    }
}
