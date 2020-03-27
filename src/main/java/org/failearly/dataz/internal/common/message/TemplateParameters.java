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

import org.failearly.dataz.internal.common.internal.message.MessageParameters;
import org.failearly.dataz.internal.common.internal.message.definition.TemplateParametersDefinition;

import java.lang.annotation.*;

/**
 * Mark your message class (implementing {@link MessageBuilder} or extending {@link MessageBuilderBase}) with this
 * Annotation, if you want to check if all template parameters has been set.
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
 *          // dropped for brevity (the full example could be seen in {@link MessageBuilderBase})
 *      }
 * </pre>
 *
 * @see MessageBuilderBase
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented @Inherited
@Repeatable(TemplateParameters.List.class)
@MessageParameters.Definition(TemplateParametersDefinition.class)
public @interface TemplateParameters {
    /**
     * The name of parameters used within a template.
     * @return the parameters.
     */
    String[] value();

    /**
     * Containing Annotation Type.
     * <p>
     * Remark: This will be used by Java8 compiler.
     *
     * @see java.lang.annotation.Repeatable
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented @Inherited
    @interface List {
        TemplateParameters[] value();
    }
}
