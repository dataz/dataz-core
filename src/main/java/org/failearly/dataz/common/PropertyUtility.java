/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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

package org.failearly.dataz.common;

import org.failearly.common.proputils.PropertiesAccessor;

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
