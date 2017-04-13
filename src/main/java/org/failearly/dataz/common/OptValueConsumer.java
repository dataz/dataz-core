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
package org.failearly.dataz.common;

import java.util.function.Consumer;

/**
 * OptValueConsumer is responsible for ...
 */
public interface OptValueConsumer<T> {
    /**
     * Apply value or not.
     *
     * @param valueConsumer Consumer of value.
     */
    default void apply(Consumer<T> valueConsumer, T value) {
        /* DO NOTHING */
    }
}