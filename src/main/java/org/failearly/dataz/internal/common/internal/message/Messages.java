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

import org.apache.velocity.context.Context;
import org.failearly.dataz.internal.common.message.*;

import java.io.Writer;

/**
 * Messages is responsible for ...
 */
public final class Messages {

    private Messages() {
    }

    /**
     * Creates a lazy message object.
     *
     * @param messageBuilder the message builder instance
     * @param initializer    the message builder initializer
     * @param <T>            The message builder class
     *
     * @return a lazy message object.
     */
    public static <T extends MessageBuilderBase> Message createLazyMessage(T messageBuilder, MessageBuilder.Initializer<T>
        initializer) {
        return new LazyMessage<>(messageBuilder, initializer);
    }


    /**
     * Creates a message object based on
     * {@link org.apache.velocity.app.VelocityEngine#evaluate(Context, Writer, String, String)}
     *
     * @param template         the template as string.
     * @param messageArguments the message arguments
     *
     * @return the message object
     */
    public static Message createStringTemplateMessage(String template, MessageArgumentsImpl messageArguments) {
        return new VelocityStringTemplateMessage(template, messageArguments);
    }

    /**
     * Creates a message object based on
     * {@link org.apache.velocity.app.VelocityEngine#getTemplate(String)}
     *
     * @param resource            the template as resource.
     * @param messageArguments    the message arguments
     *
     * @return the message object
     */
    public static Message createClasspathTemplateMessage(
        String resource,
        MessageArgumentsImpl messageArguments
    ) {
        return new VelocityResourceTemplateMessage(resource, messageArguments);
    }
}
