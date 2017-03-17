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

package org.failearly.dataz.internal.template.support.test.message.encoder;

import org.failearly.common.message.Message;
import org.failearly.dataz.internal.template.support.test.message.basic.TemplateObjectErrorMessagesBase;
import org.failearly.dataz.internal.template.support.test.message.basic.TemplateObjectMessage;

/**
 * DevelopmentEncoderErrorMessages is responsible for ...
 */
public final class DevelopmentEncoderErrorMessages extends TemplateObjectErrorMessagesBase {

    @Override
    public Message missingTemplateObjectFactory(TemplateObjectMessage.Initializer initializer) {
        return new MissingEncoderFactory().buildLazyMessage(initializer);
    }

    @Override
    public Message missingTemplateObject(TemplateObjectMessage.Initializer initializer) {
        return new MissingEncoder().buildLazyMessage(initializer);
    }

    @Override
    public Message missingTestFixture(TemplateObjectMessage.Initializer initializer) {
        return new MissingTestFixture().buildLazyMessage(initializer);
    }

    @Override
    public Message initialStepsDone(TemplateObjectMessage.Initializer initializer) {
        return new InitialStepsDone().buildLazyMessage(initializer);
    }

}
