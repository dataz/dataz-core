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

package org.failearly.dataz.template.support.test.message;

import org.failearly.common.message.ClasspathMessageTemplate;
import org.failearly.common.message.Message;
import org.failearly.common.message.TemplateParameters;

import static org.failearly.dataz.template.support.test.message.AbstractTemplateObjectMessage.*;

/**
 * MissingTemplateObjectAnnotation is responsible for ...
 */
@ClasspathMessageTemplate
@TemplateParameters({ARG_TEMPLATE_OBJECT_ANNOTATION, ARG_TEMPLATE_OBJECT_FACTORY, ARG_TEMPLATE_OBJECT, ARG_TEST_FIXTURE})
final class MissingAnnotationOfTestFixture extends AbstractTemplateObjectMessage<MissingAnnotationOfTestFixture> {
    MissingAnnotationOfTestFixture() {
        super(MissingAnnotationOfTestFixture.class);
    }

    public static Message create(TemplateObjectMessage.Initializer initializer) {
        return new MissingAnnotationOfTestFixture().buildLazyMessage(initializer);
    }

}