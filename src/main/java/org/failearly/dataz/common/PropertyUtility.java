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

package org.failearly.dataz.common;

import org.failearly.dataz.internal.common.proputils.PropertiesAccessor;

import java.util.*;

/**
 * PropertyUtility provides a factory method for creating a {@link PropertiesAccessor} instance from {@link Property} array.
 */
public final class PropertyUtility {

    private PropertyUtility() {
    }

    public static Properties toProperties(Property[] propertyList) {
        final Properties properties = new Properties();
        properties.putAll(toMap(propertyList));
        return properties;
    }
    public static PropertiesAccessor toPropertyAccessor(Property[] propertyList) {
        return new PropertiesAccessor(toMap(propertyList));
    }

    private static Map<String,String> toMap(Property[] propertyList) {
        final Map<String,String> map=new HashMap<>();
        final Set<String> duplicated=new TreeSet<>();
        for (Property property : propertyList) {
            if( null!=map.put(property.k(), property.v()) ) {
                duplicated.add(property.k());
            }
        }

        if( ! duplicated.isEmpty() ) {
            throw new IllegalArgumentException("Duplicated properties: " + String.join(", ", duplicated));
        }

        return map;
    }

}
