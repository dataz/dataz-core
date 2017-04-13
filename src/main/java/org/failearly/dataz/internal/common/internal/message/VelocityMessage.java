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

package org.failearly.dataz.internal.common.internal.message;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.io.Writer;

/**
 * VelocityMessage is responsible for ...
 */
abstract class VelocityMessage extends MessageWithArguments {
    private static final VelocityEngine velocityEngine=new VelocityEngine() {{
        this.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        this.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        this.init();
    }};
    private final String template;

    protected VelocityMessage(String template, MessageArgumentsImpl messageArguments) {
        super(messageArguments);
        this.template=template;
    }

    protected Context createContext() {
        return new VelocityContext(getMessageArgumentsAsMap());
    }

    @Override
    protected final String doGenerate() {
        final StringWriter generatedMessage=new StringWriter();
        mergeTemplateAndArguments(velocityEngine, this.template, generatedMessage);
        return generatedMessage.toString();
    }

    protected abstract void mergeTemplateAndArguments(VelocityEngine velocityEngine, String template, Writer targetWriter);
}
