/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.failearly.dataz.internal.template.support.test.message.basic;

import org.failearly.common.message.Message;
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
