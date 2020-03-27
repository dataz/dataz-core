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

package org.failearly.dataz.internal.common.builder;

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
