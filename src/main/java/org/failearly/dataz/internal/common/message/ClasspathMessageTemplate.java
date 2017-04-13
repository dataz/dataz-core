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
import org.failearly.dataz.internal.common.internal.message.definition.ClasspathMessageTemplateDefinition;

import java.lang.annotation.*;

/**
 * ClasspathMessageTemplate uses a template classpath resource.
 * <br><br>
 * Example:<br><br>
 * <pre>
 *     {@literal @ClasspathMessageTemplate(value="my-message.txt.vm")}
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
@MessageTemplate.Definition(ClasspathMessageTemplateDefinition.class)
@Documented
public @interface ClasspathMessageTemplate {
    String USE_DEFAULT="";

    /**
     * The message template as resource in current classpath. If not set the annotated class name will be used.
     *
     * @return the message template.
     */
    String value() default USE_DEFAULT;
}
