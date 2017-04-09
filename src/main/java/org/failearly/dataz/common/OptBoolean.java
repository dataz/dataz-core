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

import java.util.Optional;
import java.util.function.Consumer;

/**
 * OptBoolean applies the selected boolean value, if not {@link #DEFAULT} is selected.
 *
 * This is meant to be used within annotations.
 */
public enum OptBoolean {
    TRUE() {
        public void apply(Consumer<Boolean> valueConsumer) {
            valueConsumer.accept(Boolean.TRUE);
        }

        public Optional<Boolean> getBoolean() {
            return Optional.of(Boolean.TRUE);
        }
    },
    FALSE() {
        public void apply(Consumer<Boolean> valueConsumer) {
            valueConsumer.accept(Boolean.FALSE);
        }

        public Optional<Boolean> getBoolean() {
            return Optional.of(Boolean.FALSE);
        }
    },
    DEFAULT;

    public Optional<Boolean> getBoolean() {
        return Optional.empty();
    }

    public void apply(Consumer<Boolean> valueConsumer) {
        /* DO NOTHING */
    }

}
