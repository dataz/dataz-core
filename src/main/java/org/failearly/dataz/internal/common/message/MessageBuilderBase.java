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

package org.failearly.dataz.internal.common.message;

import org.failearly.dataz.internal.common.internal.message.MessageArgumentsImpl;
import org.failearly.dataz.internal.common.internal.message.MessageParameters;
import org.failearly.dataz.internal.common.internal.message.MessageTemplate;
import org.failearly.dataz.internal.common.internal.message.Messages;
import org.failearly.dataz.internal.common.internal.message.resolver.parameter.MessageParametersResolver;
import org.failearly.dataz.internal.common.internal.message.resolver.parameter.MessageParametersResolverFactory;
import org.failearly.dataz.internal.common.internal.message.resolver.template.MessageTemplatesResolver;
import org.failearly.dataz.internal.common.internal.message.resolver.template.MessageTemplatesResolverFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * MessageBuilderBase must be used for all Message classes.
 * <br><br>
 * Example:<br><br>
 * <pre>
 *     {@literal @}{@link InlineMessageTemplate}(templates="This is a message to $user!")}
 *     {@literal @}{@link TemplateParameters}(UserMessage.USER)}
 *      public final class UserMessage
 *          extends {@link MessageBuilderBase}&lt;UserMessage&gt;
 *      {
 *          static final String USER="user";
 *
 *          private UserMessage() {
 *              super(UserMessage.class);
 *          }
 *
 *          // None lazy version.
 *          public static UserMessage create() {
 *              return new UserMessage();
 *          }
 *
 *          // Lazy version
 *          public static Message create(Initializer&lt;UserMessage&gt;  messageInitializer) {
 *              return create().buildLazyMessage(messageInitializer);
 *          }
 *
 *          // Builder method(s) for the parameters.
 *          public UserMessage withUser(String username) {
 *              return with(USER, username);
 *          }
 *      }
 * </pre>
 */
public abstract class MessageBuilderBase<T extends MessageBuilderBase<T>> implements MessageBuilder<T> {
    private static final MessageTemplatesResolver messageTemplateCache=MessageTemplatesResolverFactory
        .createMessageTemplatesResolver();
    private static final MessageParametersResolver messageParametersResolver=MessageParametersResolverFactory
        .createMessageParametersResolver();

    private final Class<T> messageBuilderClass;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private MessageArgumentsImpl messageArguments;

    protected MessageBuilderBase(Class<T> messageBuilderClass) {
        this.messageBuilderClass=messageBuilderClass;
    }

    /**
     * Adds an argument to the message builder. This arguments will be used while generating the message.
     *
     * @param parameterName the template argument's name (parameter)
     * @param argumentValue the argument value
     * @return this
     *
     * @see Message#generate()
     */
    public final T with(String parameterName, Object argumentValue) {
        applyStandardArgumentsHookOnce();
        messageArguments.addMandatoryArgument(parameterName, argumentValue);
        return messageBuilderClass.cast(this);
    }

    public final Message build() {
        final MessageTemplate messageTemplate=messageTemplateCache.resolveMessageTemplate(messageBuilderClass);
        applyStandardArgumentsHookOnce();
        validateMessageArguments(messageArguments, messageBuilderClass);

        return resetMessageBuilder(messageTemplate.createMessage(messageArguments));
    }

    /**
     * Hook method for adding standard arguments.
     * @param messageArguments the (standard) message arguments to be set.
     */
    protected void standardArgumentsHook(MessageArguments messageArguments) {
        // It's a hook (so only the actually message builder should use it).
    }

    /**
     * Builds a lazy message.
     *
     * @param initializer the (lazy) executed initializer.
     *
     * @return a lazy message.
     */
    @Override
    public final Message buildLazyMessage(Initializer<T> initializer) {
        return Messages.createLazyMessage(messageBuilderClass.cast(this), initializer);
    }

    private static void validateMessageArguments(
            MessageArgumentsImpl messageArguments,
            Class<? extends MessageBuilder> messageBuilderClass
        ) {
        final MessageParameters messageParameters=messageParametersResolver.resolveMessageParameters(messageBuilderClass);
        final Set<String> errorMessages=new HashSet<>();
        if (!messageParameters.messageArgumentsAreValid(messageArguments, errorMessages::add)) {
            throw new IllegalArgumentException(createErrorMessage(errorMessages));
        }
    }

    private static String createErrorMessage(Set<String> errorMessages) {
        return "Missing template parameter(s): \n\n" + String.join("\n", errorMessages);
    }

    private void applyStandardArgumentsHookOnce() {
        if( messageArguments==null ) {
            messageArguments=new MessageArgumentsImpl();
            this.standardArgumentsHook(messageArguments);
        }
    }


    private Message resetMessageBuilder(Message message) {
        this.messageArguments = null;
        return message;
    }
}
