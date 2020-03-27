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

package org.failearly.dataz.internal.common.internal.message.resolver.template;

/**
 * MessageTemplatesResolverFactory is a factory (utility) class for {@link MessageTemplatesResolver}.
 */
public final class MessageTemplatesResolverFactory {
    private MessageTemplatesResolverFactory() {
    }

    /**
     * Creates a {@link MessageTemplatesResolver}.
     * @return an instance of MessageTemplatesResolver.
     */
    public static MessageTemplatesResolver createMessageTemplatesResolver() {
        return new MessageTemplatesCache();
    }
}
