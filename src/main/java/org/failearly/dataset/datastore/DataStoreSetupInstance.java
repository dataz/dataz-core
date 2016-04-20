/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */
package org.failearly.dataset.datastore;

import org.failearly.dataset.DataStoreSetup;

/**
 * Used for collecting the {@link org.failearly.dataset.DataStoreSetup} annotations along with
 * the associated class.
 *
 * @see org.failearly.dataset.datastore.DataStores
 * @see org.failearly.dataset.datastore.DataStore
 */
final class DataStoreSetupInstance {
    private final Class<?> associatedClass;
    private final DataStoreSetup annotation;

    DataStoreSetupInstance(Class<?> associatedClass, DataStoreSetup annotation) {
        this.associatedClass = associatedClass;
        this.annotation = annotation;
    }

    Class<?> getAssociatedClass() {
        return associatedClass;
    }

    DataStoreSetup getAnnotation() {
        return annotation;
    }

    boolean belongsToDataStore(String id) {
        return annotation.datastore().equals(id);
    }

    String[] setup() {
        return annotation.setup();
    }

    String[] cleanup() {
        return annotation.cleanup();
    }
}
