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

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import java.io.Writer;

/**
 * VelocityStringTemplateMessage is responsible for ...
 */
final class VelocityResourceTemplateMessage extends VelocityMessage {

    VelocityResourceTemplateMessage(String templateResource, MessageArgumentsImpl messageArguments) {
        super(templateResource, messageArguments);
    }

    @Override
    protected void mergeTemplateAndArguments(VelocityEngine velocityEngine, String templateResource, Writer targetWriter) {
        final Template template=velocityEngine.getTemplate(templateResource);
        template.merge(createContext(), targetWriter);
    }
}
