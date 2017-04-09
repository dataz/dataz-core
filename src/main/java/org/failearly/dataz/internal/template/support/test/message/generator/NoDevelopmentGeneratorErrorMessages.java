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
package org.failearly.dataz.internal.template.support.test.message.generator;

import org.failearly.dataz.internal.common.message.Message;
import org.failearly.dataz.internal.template.support.test.message.basic.NoDevelopmentTemplateObjectErrorMessages;
import org.failearly.dataz.internal.template.support.test.message.basic.TemplateObjectMessage;

/**
 * NoDevelopmentGeneratorErrorMessages is responsible for ...
 */
public final class NoDevelopmentGeneratorErrorMessages extends NoDevelopmentTemplateObjectErrorMessages {

    @Override
    protected Message useDevelopmentTestBaseClass(TemplateObjectMessage.Initializer initializer) {
        return new UseDevelopmentTestBaseClass().buildLazyMessage(initializer);
    }
}
