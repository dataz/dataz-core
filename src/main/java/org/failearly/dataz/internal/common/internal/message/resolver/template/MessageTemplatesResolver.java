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

import org.failearly.dataz.internal.common.internal.message.MessageTemplate;
import org.failearly.dataz.internal.common.message.MessageBuilder;
import org.failearly.dataz.internal.common.message.MessageBuilderBase;

/**
 * MessageTemplatesResolver resolves the {@link MessageTemplate} from specified {@link MessageBuilder} class.
 *
 * @see MessageBuilderBase
 */
public interface MessageTemplatesResolver {
    /**
     * Resolve the {@link MessageTemplate}.
     * @param messageBuilderClass the message builder class.
     * @return the message template
     */
    MessageTemplate resolveMessageTemplate(Class<? extends MessageBuilder> messageBuilderClass);
}
