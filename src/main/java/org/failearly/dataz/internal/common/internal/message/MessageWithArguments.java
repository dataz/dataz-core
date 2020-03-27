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

package org.failearly.dataz.internal.common.internal.message;

import java.util.HashMap;
import java.util.Map;

/**
 * MessageWithArguments is responsible for ...
 */
abstract class MessageWithArguments extends AbstractMessage  {
    private final MessageArgumentsImpl messageArguments;

    MessageWithArguments(MessageArgumentsImpl messageArguments) {
        this.messageArguments=messageArguments;
    }

    final Map<String,?> getMessageArgumentsAsMap() {
        return new HashMap<>(messageArguments.asMap());
    }
}
