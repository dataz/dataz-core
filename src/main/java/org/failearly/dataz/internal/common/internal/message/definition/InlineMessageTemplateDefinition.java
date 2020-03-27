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

package org.failearly.dataz.internal.common.internal.message.definition;

import org.failearly.dataz.internal.common.internal.message.MessageArgumentsImpl;
import org.failearly.dataz.internal.common.internal.message.MessageTemplate;
import org.failearly.dataz.internal.common.internal.message.MessageTemplateBase;
import org.failearly.dataz.internal.common.internal.message.Messages;
import org.failearly.dataz.internal.common.message.*;

/**
 * InlineMessageTemplateDefinition is responsible for ...
 */
public final class InlineMessageTemplateDefinition extends MessageTemplateBase<InlineMessageTemplate> {
    private final String template;

    // Used by PrototypeCache
    @SuppressWarnings("unused")
    private InlineMessageTemplateDefinition() {
        this("%should be never seen%");
    }

    private InlineMessageTemplateDefinition(String separator, String... templates) {
        super(InlineMessageTemplate.class);
        this.template=String.join(separator,templates);
    }

    @Override
    protected MessageTemplate doCreate(Class<? extends MessageBuilder> messageBuilderClass, InlineMessageTemplate annotation) {
        return new InlineMessageTemplateDefinition(annotation.separator(), annotation.value());
    }

    @Override
    public Message createMessage(MessageArgumentsImpl messageArguments) {
        return Messages.createStringTemplateMessage(template, messageArguments);
    }

}
