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

import org.failearly.dataz.internal.common.message.ClasspathMessageTemplate;
import org.failearly.dataz.internal.common.message.TemplateParameters;

import static org.failearly.dataz.internal.template.support.test.message.basic.AbstractTemplateObjectMessage.*;

/**
 * MissingTemplateObjectAnnotation is responsible for ...
 */
@ClasspathMessageTemplate("N_InitialStepsDone.txt.vm")
@TemplateParameters({ARG_TEMPLATE_OBJECT_ANNOTATION, ARG_TEMPLATE_OBJECT_FACTORY, ARG_TEST_FIXTURE, ARG_TEMPLATE_OBJECT})
final class InitialStepsDone extends AbstractTemplateObjectMessage<InitialStepsDone> {
    InitialStepsDone() {
        super(InitialStepsDone.class);
    }
}
