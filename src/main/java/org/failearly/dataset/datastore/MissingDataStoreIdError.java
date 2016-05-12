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

package org.failearly.dataset.datastore;

import org.failearly.common.annotation.utils.AnnotationUtils;

import java.lang.annotation.Annotation;

/**
 * MissingDataStoreIdError thrown by {@link DataStores} if a custom {@link org.failearly.dataset.datastore.DataStore}
 * annotation does not provide an {@code String id()} element. This is not a configuration issue, therefore it's an error
 * which could only fixed by the {@link DataStore} annotation provider.
 */
final class MissingDataStoreIdError extends Error {
    public MissingDataStoreIdError(Annotation annotation) {
        super("Missing element id() on annotation " + AnnotationUtils.getAnnotationClass(annotation));
    }
}
