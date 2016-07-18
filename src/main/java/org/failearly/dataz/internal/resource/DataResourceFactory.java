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

package org.failearly.dataz.internal.resource;

import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourceValues;

/**
 * DataResourceFactory provides utility methods for creating {@link DataResource}.
 */
public final class DataResourceFactory {
    private DataResourceFactory() {
    }

    public static DataResource createStandardInstance(DataResourceValues dataResourceValues) {
        return new StandardDataResource(dataResourceValues);
    }

    public static DataResource createTemplateInstance(DataResourceValues dataResourceValues) {
        return new TemplateDataResource(dataResourceValues);
    }

    public static DataResource createMissingResourceInstance(DataResourceValues dataResourceValues) {
        return new MissingDataResource(dataResourceValues);
    }

    public static DataResource createIgnoringInstance(DataResourceValues dataResourceValues) {
        return new IgnoringDataResource(dataResourceValues);
    }

}
