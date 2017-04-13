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

package org.failearly.dataz.internal.common.internal.message.resolver.parameter;

import org.failearly.dataz.internal.common.annotation.traverser.*;
import org.failearly.dataz.internal.common.internal.message.MessageParameters;
import org.failearly.dataz.internal.common.internal.message.MessageParameters.Definition;
import org.failearly.dataz.internal.common.message.MessageBuilder;
import org.failearly.dataz.internal.common.classutils.PrototypeCache;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;

import static org.failearly.dataz.internal.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;

/**
 * Resolves (and caches) the {@link MessageParameters} for specified {@link MessageBuilder} class.
 */
final class MessageParametersCache implements MessageParametersResolver {
    private static final MetaAnnotationTraverser<Definition> messageParametersDefinitionTraverser= metaAnnotationTraverser(Definition.class)
            .withTraverseDepth(TraverseDepth.HIERARCHY)
            .withTraverseStrategy(TraverseStrategy.TOP_DOWN)
            .build();

    private final ConcurrentHashMap<Class<?>,MessageParameters>
        messageParametersCache=new ConcurrentHashMap<>();

    @Override
    public MessageParameters resolveMessageParameters(Class<? extends MessageBuilder> messageBuilderClass) {
        return messageParametersCache.computeIfAbsent(messageBuilderClass, MessageParametersCache::createMessageParameters);
    }

    private static MessageParameters createMessageParameters(Class<?> messageGeneratorClass) {
        final MessageParametersCreator annotationHandler=new MessageParametersCreator();
        messageParametersDefinitionTraverser.traverse(messageGeneratorClass, annotationHandler);
        return annotationHandler.getMessageParameters();
    }

    private static class MessageParametersCreator extends MetaAnnotationHandlerBase<Definition> {

        private final MessageParametersCollection messageParameters=new MessageParametersCollection();

        MessageParameters getMessageParameters() {
            return messageParameters;
        }

        @Override
        public void handleMetaClassAnnotation(Class<?> clazz, Annotation annotation, Definition metaAnnotation) {
            messageParameters.addMessageParameters(createMessageParameters(annotation, metaAnnotation));
        }

        private MessageParameters createMessageParameters(
                Annotation annotation, Definition definition) {
            return PrototypeCache.fetchPrototype(definition.value()).create(annotation);
        }
    }
}