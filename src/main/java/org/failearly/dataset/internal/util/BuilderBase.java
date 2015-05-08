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
package org.failearly.dataset.internal.util;

/**
 * Base class for building new instances of {@code T}.
 */
public abstract class BuilderBase<T> {
    /**
     * Finally creates a instance of T or if a mandatory field is missing throws {@link java.lang.IllegalStateException}.
     *
     * @return the instance of T
     *
     * @see #checkMandatoryFields
     * @see #checkMandatoryField(Object, String)
     */
    public final T build() {
        checkMandatoryFields();
        return doBuild();
    }

    /**
     * Checks the field for {@code not null} value. If the {@code field} is {@code null} a {@link java.lang.IllegalStateException} will be thrown.
     * @param field the field object
     * @param fieldName the field name
     */
    protected final void checkMandatoryField(Object field, String fieldName) {
        if( field==null ) {
            throw new IllegalStateException(this.getClass().getSimpleName() + ": Mandatory field '" + fieldName + "' missing (must not be null)!");
        }
    }

    /**
     * @return the created instance of T.
     */
    protected abstract T doBuild();

    /**
     * Hook for checking mandatory fields. The default implementation does nothing.
     *
     * @see #checkMandatoryField(Object, String)
     */
    protected void checkMandatoryFields() {
        // NO OP
    }
}
