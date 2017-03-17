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

import org.failearly.common.message.TemplateParameters;
import org.failearly.dataz.internal.template.support.test.message.basic.AbstractTemplateObjectMessage;
import org.failearly.dataz.template.support.test.TemplateObjectTestBase;

import java.util.Objects;

import static org.failearly.dataz.internal.template.support.test.message.basic.AbstractTemplateObjectMessage.ARG_TEST_CLASS;

/**
 * AbstractTemplateObjectMessage is responsible for ...
 */
@SuppressWarnings("WeakerAccess")
@TemplateParameters({ARG_TEST_CLASS})
public abstract class AbstractGeneratorMessage<T extends AbstractGeneratorMessage<T>>
        extends AbstractTemplateObjectMessage<T> {

    private static final String _ARG_TEST_BASE_CLASS = "tbc";
    private static final String _ARG_TEST_BASE_SIMPLE_NAME = "stbc";
    private static final String _ARG_LIMITED_DEV_TEST_BASE = "dltbc";
    private static final String _ARG_UNLIMITED_DEV_TEST_BASE = "dutbc";
    private static final String _ARG_NORM_TEST_BASE  = "ntbc";

    protected AbstractGeneratorMessage(Class<T> messageBuilderClass) {
        super(messageBuilderClass);
    }

    @Override
    public final T withTestClass(TemplateObjectTestBase testObject) {
        Objects.requireNonNull(testObject, "null is not permitted");
        super.withTestClass(testObject);
        final String ntbc = testObject.getClass().getSuperclass().getSimpleName()
                        .replaceFirst("Development", "")
                        .replaceFirst("Unlimited", "")
                        .replaceFirst("Limited", "");

        return  this.with(_ARG_NORM_TEST_BASE, ntbc)
                    .with(_ARG_LIMITED_DEV_TEST_BASE, "DevelopmentLimited"+ ntbc)
                    .with(_ARG_UNLIMITED_DEV_TEST_BASE, "DevelopmentUnlimited"+ ntbc);
    }
}
