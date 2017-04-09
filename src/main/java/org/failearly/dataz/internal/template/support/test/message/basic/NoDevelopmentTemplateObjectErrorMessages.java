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
import org.failearly.dataz.template.support.test.DevelopmentTemplateObjectTestBase;
import org.failearly.dataz.template.support.test.TemplateObjectTestBase;

/**
 * Provides messages in case of extending {@link TemplateObjectTestBase} instead of {@link DevelopmentTemplateObjectTestBase}.
 *
 * @see TemplateObjectTestBase
 * @see DevelopmentTemplateObjectTestBase
 */
public class NoDevelopmentTemplateObjectErrorMessages implements TemplateObjectErrorMessages {

    @Override
    public Message missingTemplateObjectAnnotation(TemplateObjectMessage.Initializer initializer) {
        return useDevelopmentTestBaseClass(initializer);
    }

    @Override
    public Message missingTemplateObjectFactory(TemplateObjectMessage.Initializer initializer) {
        return useDevelopmentTestBaseClass(initializer);
    }

    @Override
    public Message missingTemplateObject(TemplateObjectMessage.Initializer initializer) {
        return useDevelopmentTestBaseClass(initializer);
    }

    @Override
    public Message missingTestFixture(TemplateObjectMessage.Initializer initializer) {
        return useDevelopmentTestBaseClass(initializer);
    }

    @Override
    public Message missingAnnotationOfTestFixture(TemplateObjectMessage.Initializer initializer) {
        return useDevelopmentTestBaseClass(initializer);
    }

    @Override
    public Message initialStepsDone(TemplateObjectMessage.Initializer initializer) {
        return useDevelopmentTestBaseClass(initializer);
    }

    protected Message useDevelopmentTestBaseClass(TemplateObjectMessage.Initializer initializer) {
        return new UseDevelopmentTestBaseClass().buildLazyMessage(initializer);
    }
}
