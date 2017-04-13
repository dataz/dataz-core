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
import org.failearly.dataz.template.support.test.TemplateObjectTestBase;

/**
 * TemplateObjectErrorMessages is an abstract factory creating Error {@link Message} objects. Provides error messages
 * which helps creating the basic setup for a template object implementation.
 *
 * @see TemplateObjectTestBase#getTemplateObjectErrorMessages()
 */
public interface TemplateObjectErrorMessages {
    /**
     * Message in case of a missing Template Object Annotation (TOA).
     *
     * @param initializer message initializer object
     *
     * @return the message object
     */
    Message missingTemplateObjectAnnotation(TemplateObjectMessage.Initializer initializer);

    /**
     * Message in case of a missing Template Object Factory (TOF).
     *
     * @param initializer message initializer object
     *
     * @return the message object
     *
     * @see org.failearly.dataz.template.TemplateObjectFactory
     */
    Message missingTemplateObjectFactory(TemplateObjectMessage.Initializer initializer);

    /**
     * Message in case of a missing Template Object (TO).
     *
     * @param initializer message initializer object
     *
     * @return the message object
     *
     * @see org.failearly.dataz.template.TemplateObject
     */
    Message missingTemplateObject(TemplateObjectMessage.Initializer initializer);

    /**
     * Message in case of a missing Test Fixture Class.
     *
     * @param initializer message initializer object
     *
     * @return the message object
     */
    Message missingTestFixture(TemplateObjectMessage.Initializer initializer);

    /**
     * Message in case of a missing TOAs on the Test Fixture Class.
     *
     * @param initializer message initializer object
     *
     * @return the message object
     */
    Message missingAnnotationOfTestFixture(TemplateObjectMessage.Initializer initializer);

    /**
     * Message that the basic steps for creating a custom Template Object has been done.
     *
     * @param initializer message initializer object
     *
     * @return the message object
     */
    Message initialStepsDone(TemplateObjectMessage.Initializer initializer);
}
