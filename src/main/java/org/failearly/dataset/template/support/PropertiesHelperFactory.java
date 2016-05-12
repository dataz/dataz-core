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

package org.failearly.dataset.template.support;

import org.failearly.dataset.Property;
import org.failearly.common.test.PropertiesHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * PropertiesHelperFactory provides a factory method for creating a {@link PropertiesHelper} instance from {@link Property} array.
 */
public final class PropertiesHelperFactory {

    private PropertiesHelperFactory() {
    }

    public static PropertiesHelper createFromPropertyList(Property[] propertyList) {
        return new PropertiesHelper(toMap(propertyList));
    }

    private static Map<String,String> toMap(Property[] propertyList) {
        final Map<String,String> map=new HashMap<>();
        for (Property property : propertyList) {
            map.put(property.k(), property.v());
        }

        return map;
    }

}
