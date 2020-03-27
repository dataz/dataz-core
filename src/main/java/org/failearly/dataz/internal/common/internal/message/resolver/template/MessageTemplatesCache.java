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

import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotationHandlerBase;
import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotationTraverser;
import org.failearly.dataz.internal.common.annotation.traverser.TraverseDepth;
import org.failearly.dataz.internal.common.annotation.traverser.TraverseStrategy;
import org.failearly.dataz.internal.common.internal.message.MessageTemplate;
import org.failearly.dataz.internal.common.internal.message.MessageTemplate.Definition;
import org.failearly.dataz.internal.common.message.MessageBuilder;
import org.failearly.dataz.internal.common.classutils.PrototypeCache;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.failearly.dataz.internal.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;

/**
 * Resolves (and caches) the {@link MessageTemplate} for specified {@link MessageBuilder} class.
 */
final class MessageTemplatesCache implements MessageTemplatesResolver {
    private static final MetaAnnotationTraverser<Definition> messageTemplateDefinitionTraverser = metaAnnotationTraverser(Definition.class)
            .withTraverseStrategy(TraverseStrategy.BOTTOM_UP)
            .withTraverseDepth(TraverseDepth.CLASS_HIERARCHY)
            .build();
    private final ConcurrentHashMap<Class<? extends MessageBuilder>,MessageTemplate> messageTemplateCache=new
        ConcurrentHashMap<>();

    @Override
    public MessageTemplate resolveMessageTemplate(Class<? extends MessageBuilder> messageBuilderClass) {
        return messageTemplateCache.computeIfAbsent(messageBuilderClass, MessageTemplatesCache::createMessageTemplate);
    }

    private static MessageTemplate createMessageTemplate(Class<? extends MessageBuilder> messageBuilderClass) {
        final MessageTemplateCreator annotationHandler=new MessageTemplateCreator();
        messageTemplateDefinitionTraverser.traverse(messageBuilderClass, annotationHandler);
        return annotationHandler.getMessageTemplate();
    }

    private static class MessageTemplateCreator extends MetaAnnotationHandlerBase<Definition> {

        private final List<MessageTemplate> messageTemplates=new LinkedList<>();

        MessageTemplate getMessageTemplate() {
            checkForAtLeastOneMessageTemplate();

            checkForExactlyOneMessageTemplate();

            return messageTemplates.get(0);
        }

        private void checkForExactlyOneMessageTemplate() {
            if( messageTemplates.size()>1 ) {
                throw new IllegalArgumentException("Exactly one Message Template Annotation is permitted.");
            }
        }

        private void checkForAtLeastOneMessageTemplate() {
            if( messageTemplates.isEmpty() ) {
                throw new IllegalArgumentException("At least one Message Template Annotation must be assigned.");
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMetaClassAnnotation(Class<?> messageBuilderClass, Annotation annotation, Definition metaAnnotation) {
            messageTemplates.add(
                    createMessageTemplate(
                            metaAnnotation,
                            (Class<? extends MessageBuilder>) messageBuilderClass,
                            annotation
                    )
            );

        }

        private MessageTemplate createMessageTemplate(
            Definition messageTemplateDefinition,
            Class<? extends MessageBuilder> messageBuilderClass,
            Annotation annotation
        ) {
            return PrototypeCache.fetchPrototype(messageTemplateDefinition.value())
                .create(messageBuilderClass, annotation);
        }
    }
}