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
import org.failearly.dataz.internal.common.message.MessageBuilder;

import java.lang.annotation.*;

/**
 * MessageTemplate is responsible for ...
 */
public interface MessageTemplate {
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Definition {
        Class<? extends MessageTemplate> value();
    }

    /**
     * Factory method for creating a message template instance.
     *
     * @param messageBuilderClass
     * @param annotation the impl
     * @return a new instance (using the
     */
    MessageTemplate create(Class<? extends MessageBuilder> messageBuilderClass, Annotation annotation);


    /**
     * Create the message object using the message arguments.
     *
     * @param messageArguments the message arguments
     *
     * @return the message object.
     */
    Message createMessage(MessageArgumentsImpl messageArguments);
}
