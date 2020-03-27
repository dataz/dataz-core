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

import com.google.common.base.CharMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ExtendedProperties extends properties with the possibility of using references like {@code ${any.var}} or {@code ${user.home}}.
 */
public final class ExtendedProperties {

    /**
     * Used for null values.
     */
    public static final String NULL_VALUE = "(null)";

    /**
     * Used for empty values.
     */
    public static final String EMPTY_VALUE = "(empty)";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtendedProperties.class);

    private final Pattern pattern = Pattern.compile(".*\\$\\{([\\w.]+)\\}.*");

    private final Properties properties = new Properties();

    private Runnable applyResolveReference=this::resolveReferences;

    public ExtendedProperties() {
    }

    public ExtendedProperties(Properties defaults) {
        merge(defaults);
    }

    /**
     * Creates an PropertiesAccessor instance.
     *
     * @return the accessor
     */
    public final PropertiesAccessor toAccessor() {
        return new PropertiesAccessor(toStandardProperties());
    }

    /**
     * Convert to Standard properties.
     * @return  Java Properties
     */
    public final Properties toStandardProperties() {
        this.applyResolveReference.run();
        final Properties copy = new Properties();
        for (String key : this.stringPropertyNames()) {
            final String value = getPropertyValue(key);
            if (value != null)
                copy.setProperty(key, value);
        }
        return copy;
    }

    private Set<String> stringPropertyNames() {
        return this.properties.stringPropertyNames();
    }

    /**
     * Merge all properties and then resolve references.
     *
     * @param propertiesList the property list
     */
    public void merge(Properties... propertiesList) {
        for (Properties properties : propertiesList) {
            this.properties.putAll(properties);
        }

        resetResolveReferences();
    }


    public void clear() {
        this.properties.clear();
    }

    /**
     * Resolve all references within values. Replace {@code ${my.property.key}} with the assigned value.
     */
    private void resolveReferences() {
        final Map<String, String> propertyValues = new HashMap<>();
        for (Map.Entry<Object, Object> entry : this.properties.entrySet()) {
            doResolvePropertyReference("" + entry.getKey(), "" + entry.getValue(), propertyValues);
        }
        updateProperties(propertyValues);
        this.applyResolveReference = this::noResolveReferences;
    }


    private void resetResolveReferences() {
        this.applyResolveReference = this::resolveReferences;
    }

    private void noResolveReferences() {
        // do nothing.
    }

    /**
     * Set property value. If the value is {@code null} iit will be replaced with {@value #NULL_VALUE}. {@link #getProperty(String)} replaces this value with
     * {@code null}.
     *
     * @param key   property key ({@code not null}.
     * @param value property value ({@code null} is supported).
     * @return see {@link Properties#setProperty(String, String)}
     */
    public Object setProperty(String key, String value) {
        Objects.requireNonNull(key, "Property key must not be null");
        resetResolveReferences();
        return setProperty0(key, value);
    }

    private Object setProperty0(String key, String value) {
        if (value == null) {
            return this.properties.setProperty(key, NULL_VALUE);
        }
        if (isEmptyOrBlanks(value)) {
            return this.properties.setProperty(key, EMPTY_VALUE);
        }
        return this.properties.setProperty(key, value);
    }

    private static boolean isEmptyOrBlanks(String value) {
        return trimWhitespace(value).isEmpty();
    }


    /**
     * # If the property exists the value will be trimmed (not leading/trailing whitespaces).
     *
     * So an existing property is only if the value is not empty and not blank.
     *
     * @param key the property key.
     * @return the trimmed value or {@code null}.
     */
    public String getProperty(String key) {
        this.applyResolveReference.run();
        final String value = getPropertyValue(key);
        LOGGER.debug("Access property key '{}' = '{}'", key, value);
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        final String value = getProperty(key);
        return value == null ? defaultValue : value;
    }

    private String getPropertyValue(String key) {
        return transform(this.properties.getProperty(key));
    }

    private static String transform(String input) {
        if( input==null ) {
            return null;
        }
        if (NULL_VALUE.equals(input)) {
            return null;
        }
        if (EMPTY_VALUE.equals(input)) {
            return "";
        }
        return trimWhitespace(input);
    }

    private static String trimWhitespace(String input) {
        return CharMatcher.whitespace().trimFrom(input);
    }

    private void updateProperties(Map<String, String> propertyValues) {
        for (Map.Entry<String, String> entry : propertyValues.entrySet()) {
            setProperty0(entry.getKey(), entry.getValue());
        }
    }

    private void doResolvePropertyReference(String key, String value, Map<String, String> propertyValues) {
        if (value != null) {
            final Matcher matcher = matcher(value);
            if (matcher != null) {
                while (matcher.matches()) {
                    final String referencedKey = referencedPropertyKey(matcher);
                    final String referencedValue = resolveValueOfReferencedKey(referencedKey);
                    checkForEndlessLoop(key, referencedKey, referencedValue);

                    value = value.replace(propertyKey(referencedKey), referencedValue);

                    matcher.reset(value);
                }
            }

            propertyValues.put(key, value);
        } else {
            propertyValues.put(key, null);
        }
    }

    private void checkForEndlessLoop(String key, String nextKey, String nextValue) {
        final Matcher matcher = matcher(nextValue);
        if (matcher != null) {
            final Set<String> keys = new HashSet<>();
            keys.add(key);
            keys.add(nextKey);

            while (matcher.matches()) {
                final String referencedKey = referencedPropertyKey(matcher);
                if (keys.contains(referencedKey)) {
                    throwEndlessRecursionDetected(key);
                }

                keys.add(referencedKey);
                final String referencedValue = resolveValueOfReferencedKey(referencedKey);
                matcher.reset(referencedValue);
            }
        }
    }

    private void throwEndlessRecursionDetected(String key) {
        final String message = "Recursion on property key '" + key + "' detected.";
        LOGGER.error(message);
        throw new IllegalArgumentException(message);
    }

    private String resolveValueOfReferencedKey(String referencedKey) {
        final String value = getPropertyValue(referencedKey);
        return value == null ? markMissingProperty(referencedKey) : value;
    }

    private String markMissingProperty(String referencedKey) {
        return "?{"+referencedKey+"}";
    }

    private static String propertyKey(String propertyKey) {
        return "${" + propertyKey + "}";
    }

    private static String referencedPropertyKey(Matcher matcher) {
        return matcher.group(1);
    }

    private Matcher matcher(String inputValue) {
        final Matcher matcher = pattern.matcher(inputValue);

        return matcher.matches() ? matcher : null;
    }

    public void load(InputStream resource) throws IOException {
        final Properties newProps=new Properties();
        newProps.load(resource);
        merge(newProps);
    }
}
