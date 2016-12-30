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
