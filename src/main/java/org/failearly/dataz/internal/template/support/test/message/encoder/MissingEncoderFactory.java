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

package org.failearly.dataz.internal.template.support.test.message.encoder;

import org.failearly.common.message.ClasspathMessageTemplate;
import org.failearly.common.message.TemplateParameters;
import org.failearly.dataz.internal.template.support.test.message.basic.AbstractTemplateObjectMessage;

/**
 * MissingTemplateObjectAnnotation is responsible for ...
 */
@ClasspathMessageTemplate("1_MissingEncoderFactory.txt.vm")
@TemplateParameters({AbstractTemplateObjectMessage.ARG_TEMPLATE_OBJECT_ANNOTATION})
final class MissingEncoderFactory extends AbstractTemplateObjectMessage<MissingEncoderFactory> {
    MissingEncoderFactory() {
        super(MissingEncoderFactory.class);
    }
}
