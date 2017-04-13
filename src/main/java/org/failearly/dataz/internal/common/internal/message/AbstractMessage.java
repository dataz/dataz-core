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

package org.failearly.dataz.internal.common.internal.message;

import org.failearly.dataz.internal.common.message.Message;

/**
 * AbstractMessage does provides the base implementation for {@link #generate()}. The message will be generated only
 * once.
 */
abstract class AbstractMessage implements Message {
    private String generatedMessage=null;

    @Override
    public final String generate() {
        if (generatedMessage == null) {
            generatedMessage=doGenerate();
        }
        return generatedMessage;
    }

    /**
     * The actually implementation of {@link #generate()} must be provides by the implementation.
     * @return the generated message (as string).
     */
    protected abstract String doGenerate();

    /**
     * Alias for {@link #generate()}.
     * @return the generated message (as string)
     */
    @Override
    public final String toString() {
        return generate();
    }
}
