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

package org.failearly.dataset.template.support.test.message;

import org.failearly.dataset.template.TemplateObject;
import org.failearly.dataset.template.TemplateObjectFactory;
import org.failearly.dataset.template.support.test.TemplateObjectTestBase;

import java.lang.annotation.Annotation;

/**
 * TemplateObjectMessage is responsible for ...
 */
public interface TemplateObjectMessage {

    TemplateObjectMessage withTestClass(TemplateObjectTestBase testObject);

    TemplateObjectMessage withTemplateObjectAnnotationClass(Class<? extends Annotation> annotationClass);

    TemplateObjectMessage withTemplateObjectFactoryClass(Class<? extends TemplateObjectFactory> templateObjectFactoryClass);

    TemplateObjectMessage withTemplateObjectClass(Class<? extends TemplateObject> templateObjectClass);

    TemplateObjectMessage withTestFixtureClass(Class<?> testFixtureClass);

    /**
     * Initializer initializes the TemplateObjectMessage.
     */
    interface Initializer {
        void init(TemplateObjectMessage templateObjectMessage);
    }
}
