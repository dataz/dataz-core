/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.template.generator;


/**
 * Limit Type for generator impl {@code Limit limit()}.
 */
public enum Limit {
    /**
     * The generator could be used within Velocities foreach loop.
     */
    LIMITED(true),
    /**
     * The generator MUST NOT be used within Velocities foreach loop - causes an exception.
     * Use {@link Generator#next()} instead.
     */
    UNLIMITED(false);

    private final boolean limited;

    Limit(boolean limited) {
        this.limited = limited;
    }


    /**
     * @return  {@code true} if the generator is {@link #LIMITED} otherwise {@code false}.
     */
    public boolean isLimited() {
        return this.limited;
    }
}
