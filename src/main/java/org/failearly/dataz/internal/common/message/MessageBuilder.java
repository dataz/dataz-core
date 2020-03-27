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

package org.failearly.dataz.internal.common.message;

/**
 * MessageBuilder is responsible for creating {@link Message} objects.
 */
public interface MessageBuilder<T extends MessageBuilder<T>> {

    /**
     * Builds the actually message object, but not the message string.
     *
     * @return the message object.
     *
     * @see Message#generate()
     */
    Message build();

    /**
     * Creates a lazy message. The {@code initializer} will be called when the message will be generated.
     *
     * @param initializer the message builder initializer
     * @return a lazy message
     *
     * @see Message#generate()
     */
    Message buildLazyMessage(Initializer<T> initializer);

    /**
     * Initializer initializes the message builder. For use with
     * {@link MessageBuilderBase#buildLazyMessage(Initializer)}.
     */
    @FunctionalInterface
    interface Initializer<T> {
        T applyOn(T messageBuilder);
    }
}
