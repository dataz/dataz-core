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

package org.failearly.dataz.internal.template.support.test.message.generator.limited;

import org.failearly.dataz.internal.common.message.Message;
import org.failearly.dataz.internal.template.support.test.message.basic.TemplateObjectMessage;
import org.failearly.dataz.internal.template.support.test.message.generator.DevelopmentGeneratorErrorMessages;

/**
 * DevelopmentEncoderErrorMessages is responsible for ...
 */
public class DevelopmentLimitedGeneratorErrorMessages extends DevelopmentGeneratorErrorMessages {

    @Override
    public Message missingTemplateObjectAnnotation(TemplateObjectMessage.Initializer initializer) {
        return new MissingGeneratorAnnotation().buildLazyMessage(initializer);
    }

    @Override
    public Message missingTemplateObjectFactory(TemplateObjectMessage.Initializer initializer) {
        return new MissingGeneratorFactory().buildLazyMessage(initializer);
    }

    @Override
    public Message missingTemplateObject(TemplateObjectMessage.Initializer initializer) {
        return new MissingGenerator().buildLazyMessage(initializer);
    }
}
