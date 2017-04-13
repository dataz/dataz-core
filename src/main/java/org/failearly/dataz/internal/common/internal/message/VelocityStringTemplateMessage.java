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

import org.apache.velocity.app.VelocityEngine;

import java.io.StringReader;
import java.io.Writer;

/**
 * VelocityStringTemplateMessage is responsible for ...
 */
final class VelocityStringTemplateMessage extends VelocityMessage {

    VelocityStringTemplateMessage(String template, MessageArgumentsImpl messageArguments) {
        super(template, messageArguments);
    }

    @Override
    protected void mergeTemplateAndArguments(VelocityEngine velocityEngine, String template, Writer targetWriter) {
        velocityEngine.evaluate(
            createContext(),
            targetWriter,
            "message",
            new StringReader(template)
        );
    }
}
