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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ExtendedProperties extends properties with the possibility of using references like {@code ${any.var}} or {@code ${user.home}}.
 */
public class ExtendedProperties extends Properties {

    /**
     * Used for null values.
     */
    public static final String NULL_VALUE = "(null)";

    /**
     * Used for empty values.
     */
    public static final String EMPTY_VALUE = "(empty)";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtendedProperties.class);

    private final Pattern pattern=Pattern.compile(".*\\$\\{([\\w.]+)\\}.*");

    public ExtendedProperties() {
    }

    public ExtendedProperties(Properties defaults) {
        super(defaults);
    }


    /**
     * Resolve all references within values. Replace {@code ${my.property.key}} with the assigned value.
     */
    public final void resolveReferences() {
        final Map<String,String> propertyValues=new HashMap<>();
        for (Map.Entry<Object, Object> entry : this.entrySet()) {
            doResolvePropertyReference("" + entry.getKey(), "" + entry.getValue(), propertyValues);
        }
        updateProperties(propertyValues);
    }

    /**
     * Resolve reference(s) within value of {@code key}.
     *
     * @param key property key.
     */
    public final void resolveReferences(String key) {
        final Map<String,String> propertyValues=new HashMap<>();
        doResolvePropertyReference(key, this.getProperty(key), propertyValues);
        updateProperties(propertyValues);
    }

    /**
     * Set property value. If the value is {@code null} iit will be replaced with {@value #NULL_VALUE}. {@link #getProperty(String)} replaces this value with
     * {@code null}.
     * @param key property key ({@code not null}.
     * @param value property value ({@code null} is supported).
     * @return see {@link Properties#setProperty(String, String)}
     */
    @Override
    public Object setProperty(String key, String value) {
        Objects.requireNonNull(key,"Property key must not be null");
        if( value==null ) {
            return super.setProperty(key, NULL_VALUE);
        }
        value=StringUtils.trimToEmpty(value);
        if( "".equals(value) ) {
            return super.setProperty(key, EMPTY_VALUE);
        }
        return super.setProperty(key, value);
    }

    /**
     * Return the value of the key or throw {@link org.failearly.dataset.util.MissingPropertyException}, if the property is unknown or the value is
     * {@code null}, empty or blank.
     *
     * @param key the property key.
     * @return the value.
     *
     * @throws org.failearly.dataset.util.MissingPropertyException in case of unknown property key or empty or {@code null} value.
     */
    public String getMandatoryProperty(String key) throws MissingPropertyException {
        final String value = getProperty(key);
        if( null==value ) {
            throw new MissingPropertyException(key);
        }
        return value;
    }


    /**
     * If the property exists the value will be trimmed by {@link org.apache.commons.lang.StringUtils#trimToNull(String)}.
     * So an existing property is only if the value is not empty and not blank.
     * @param key the property key.
     * @return the trimmed value or {@code null}.
     */
    @Override
    public String getProperty(String key) {
        final String value = getPropertyValue(key);
        LOGGER.debug("Access property key '{}' = '{}'", key, value);
        return value;
    }

    private String getPropertyValue(String key) {
        final String value = StringUtils.trimToNull(getProperty0(key));
        if( NULL_VALUE.equals(value) ) {
            return null;
        }
        if( EMPTY_VALUE.equals(value) ) {
            return "";
        }
        return value;
    }

    private String getProperty0(String key) {
        return super.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        final String value = getProperty(key);
        return value==null ? defaultValue : value;
    }

    private void updateProperties(Map<String, String> propertyValues) {
        for (Map.Entry<String, String> entry : propertyValues.entrySet()) {
            setProperty(entry.getKey(), entry.getValue());
        }
    }

    private void doResolvePropertyReference(String key, String value, Map<String, String> propertyValues) {
        if( value != null ) {
            final Matcher matcher = matcher(value);
            if( matcher!=null ) {
                while (matcher.matches()) {
                    final String referencedKey = referencedPropertyKey(matcher);
                    final String referencedValue = resolveValueOfReferencedKey(referencedKey);
                    checkForEndlessLoop(key, referencedKey, referencedValue);

                    value = value.replace(propertyKey(referencedKey), referencedValue);

                    matcher.reset(value);
                }
            }

            propertyValues.put(key, value);
        }
        else {
            propertyValues.put(key, null);
        }
    }

    private void checkForEndlessLoop(String key, String nextKey, String nextValue) {
        final Matcher matcher=matcher(nextValue);
        if( matcher != null ) {
            final Set<String> keys=new HashSet<>();
            keys.add(key);
            keys.add(nextKey);

            while( matcher.matches() ) {
                final String referencedKey = referencedPropertyKey(matcher);
                if( keys.contains(referencedKey) ) {
                    throwEndlessRecursionDetected(key, referencedKey);
                }

                keys.add(referencedKey);
                final String referencedValue = resolveValueOfReferencedKey(referencedKey);
                matcher.reset(referencedValue);
            }
        }
    }

    private void throwEndlessRecursionDetected(String key, String referencedKey) {
        final String message = "Recursion on property '" + key + "' detected. Variable '" + referencedKey + "' causes endless recursion";
        LOGGER.error(message);
        throw new RuntimeException(message);
    }

    private String resolveValueOfReferencedKey(String referencedKey) {
        return this.getProperty(referencedKey, referencedKey);
    }

    private static String propertyKey(String propertyKey) {
        return "${" + propertyKey + "}";
    }

    private static String referencedPropertyKey(Matcher matcher) {
        return matcher.group(1);
    }

    private Matcher matcher(String inputValue) {
        final Matcher matcher=pattern.matcher(inputValue);

        return matcher.matches() ? matcher : null;
    }

    public final Properties toStandardProperties() {
        final Properties copy=new Properties();
        for (String key : this.stringPropertyNames() ) {
            final String value = getPropertyValue(key);
            if( value!=null )
                copy.setProperty(key, value);
        }
        return copy;
    }
}
