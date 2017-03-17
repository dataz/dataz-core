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

import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.support.test.TemplateObjectTestBase;

import java.lang.annotation.Annotation;

/**
 * TemplateObjectMessage provides methods for creating {@code TemplateObjectMessage}.
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
