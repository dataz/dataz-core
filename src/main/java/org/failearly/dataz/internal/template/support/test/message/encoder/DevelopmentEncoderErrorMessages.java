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
