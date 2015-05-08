/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.internal.resource;

import org.failearly.dataset.DataSet;
import org.failearly.dataset.DataStoreSetup;
import org.failearly.dataset.internal.util.IOUtils;
import org.failearly.dataset.resource.DataResourceValues;

import java.io.InputStream;

/**
 * StandardDataSetResource is responsible for opening an existing (standard) resource (no template engine).
 */
final class StandardDataResource extends DataResourceBase {

    private final Class<?> testClass;

    StandardDataResource(DataResourceValues dataResourceValues) {
        super(dataResourceValues);
        this.testClass = dataResourceValues.getTestClass();
    }

    @Override
    public InputStream open() {
        return IOUtils.autoClose(testClass.getResourceAsStream(getResource()));
    }
}
