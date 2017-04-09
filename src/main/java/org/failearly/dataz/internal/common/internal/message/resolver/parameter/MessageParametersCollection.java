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

import org.failearly.dataz.internal.common.internal.message.ErrorMessageCollector;
import org.failearly.dataz.internal.common.internal.message.MessageParameters;
import org.failearly.dataz.internal.common.internal.message.MessageArgumentsImpl;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 * MessageParametersCollection is a collection of {@link MessageParameters}.
 */
final class MessageParametersCollection implements MessageParameters {
    private final List<MessageParameters> messageParameters=new LinkedList<>();

    MessageParametersCollection() {
    }

    MessageParametersCollection addMessageParameters(MessageParameters messageParameters) {
        this.messageParameters.add(messageParameters);
        return this;
    }

    @Override
    public MessageParameters create(Annotation annotation) {
        throw new UnsupportedOperationException("Internal usage. Should not be created by MessageParameters" +
            ".Definition");
    }

    /**
     * Returns true, if none of contained {@link MessageParameters} validates to {@code false}.
     * @param messageArguments the message arguments.
     * @param errorMessageCollector collects the error messages.
     * @return {@code true} if all {@code true}, otherwise {@code false}.
     */
    @Override
    public boolean messageArgumentsAreValid(MessageArgumentsImpl messageArguments, ErrorMessageCollector errorMessageCollector) {
        return messageParameters.stream()                                                                  //
            .filter((validator)->!validator.messageArgumentsAreValid(messageArguments, errorMessageCollector))      //
            .count()==0;                                                                            //
    }
}
