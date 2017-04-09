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

package org.failearly.dataz.internal.common.message;

import org.failearly.dataz.internal.common.internal.message.MessageTemplate;
import org.failearly.dataz.internal.common.internal.message.definition.InlineMessageTemplateDefinition;

import java.lang.annotation.*;

/**
 * InlineMessageTemplate uses a inline fixed template.
 * <br><br>
 * Example:<br><br>
 * <pre>
 *     {@literal @InlineMessageTemplate(templates="This is a message to $user!")}
 *      public class MyMessage
 *          extends {@link MessageBuilderBase}&lt;MyMessage&gt;
 *      {
 *          public MyMessage() {
 *              super(MyMessage.class);
 *          }
 *
 *          public MyMessage withUser(String username) {
 *              return with("user", username);
 *          }
 *      }
 * </pre>
 *
 * @see MessageBuilderBase
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@MessageTemplate.Definition(InlineMessageTemplateDefinition.class)
@Documented
public @interface InlineMessageTemplate {
    String DEFAULT_SEPARATOR="\n";

    /**
     * The message templates. They will concatenated with {@link #separator()}.
     * @return the message templates.
     */
    String[] value();

    /**
     * The (custom or default) separator to be used while concatenation of {@link #value()}.
     * @return the separator.
     */
    String   separator() default DEFAULT_SEPARATOR;
}
