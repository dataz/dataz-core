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
package org.failearly.dataz.internal.template.support.test.message.basic;

import org.failearly.dataz.internal.common.message.Message;

/**
 * TemplateObjectErrorMessagesBase provides default implementations for {@link TemplateObjectErrorMessages}.
 */
public abstract class TemplateObjectErrorMessagesBase implements TemplateObjectErrorMessages {
    @Override
    public Message missingTemplateObjectAnnotation(TemplateObjectMessage.Initializer initializer) {
        return new MissingTemplateObjectAnnotation().buildLazyMessage(initializer);
    }

    @Override
    public Message missingTemplateObjectFactory(TemplateObjectMessage.Initializer initializer) {
        return new MissingTemplateObjectFactory().buildLazyMessage(initializer);
    }

    @Override
    public Message missingTemplateObject(TemplateObjectMessage.Initializer initializer) {
        return new MissingTemplateObject().buildLazyMessage(initializer);
    }

    @Override
    public Message missingTestFixture(TemplateObjectMessage.Initializer initializer) {
        return new MissingTestFixture().buildLazyMessage(initializer);
    }

    @Override
    public Message missingAnnotationOfTestFixture(TemplateObjectMessage.Initializer initializer) {
        return new MissingAnnotationOfTestFixture().buildLazyMessage(initializer);
    }

    @Override
    public Message initialStepsDone(TemplateObjectMessage.Initializer initializer) {
        return new InitialStepsDone().buildLazyMessage(initializer);
    }
}
