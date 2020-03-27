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
import org.failearly.dataz.internal.common.resource.ResourcePathUtils;

import java.util.Optional;

/**
 * ClasspathMessageTemplateDefinition is the implementation of {@link ClasspathMessageTemplate}.
 */
public final class ClasspathMessageTemplateDefinition extends MessageTemplateBase<ClasspathMessageTemplate> {

    private static final String DEFAULT_SUFFIX=".txt.vm";
    private final Class<? extends MessageBuilder> messageBuilderClass;
    private final ClasspathMessageTemplate annotation;

    @SuppressWarnings("unused")
    private ClasspathMessageTemplateDefinition() {
        this(null, null);
    }

    private ClasspathMessageTemplateDefinition(
        Class<? extends MessageBuilder> messageBuilderClass,
        ClasspathMessageTemplate annotation
    ) {
        super(ClasspathMessageTemplate.class);
        this.messageBuilderClass=messageBuilderClass;
        this.annotation=annotation;
    }

    @Override
    protected MessageTemplate doCreate(Class<? extends MessageBuilder> messageBuilderClass, ClasspathMessageTemplate annotation) {
        return new ClasspathMessageTemplateDefinition(messageBuilderClass, annotation);
    }

    @Override
    public Message createMessage(MessageArgumentsImpl messageArguments) {
        return Messages.createClasspathTemplateMessage(
            resolveResourcePath(annotation.value()),
            messageArguments
        );
    }

    private String resolveResourcePath(String resourceName) {
        return Optional.ofNullable(resourceName)
            .map(String::trim)
            .filter((resource) -> !resource.isEmpty())
            .map(this::absolutePath)
            .orElseGet(() -> absolutePath(messageBuilderClass.getSimpleName() + DEFAULT_SUFFIX));
    }


    private String absolutePath(String resource) {
        return ResourcePathUtils.resourcePath(resource, messageBuilderClass);
    }
}
