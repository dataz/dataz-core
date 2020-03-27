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

import java.util.function.Consumer;

/**
 * OptValueConsumer is the base for the OptXxx enum types.
 */
public interface OptValueConsumer<T> {
    /**
     * Apply value or not.
     *
     * @param valueConsumer Consumer of value.
     * @param value the value to consume
     */
    default void apply(Consumer<T> valueConsumer, T value) {
        /* DO NOTHING */
    }
}
