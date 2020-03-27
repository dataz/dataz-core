/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.internal.common.internal.message.definition;

import org.failearly.dataz.internal.common.classutils.ObjectCreatorUtil;
import org.failearly.dataz.internal.common.internal.message.MessageParametersTestBase;
import org.failearly.dataz.common.test.annotations.TestsFor;

/**
 * TemplateParametersDefinitionTest contains tests for TemplateParametersDefinition.
 */
@TestsFor({TemplateParametersDefinition.class})
public class TemplateParametersDefinitionTest extends MessageParametersTestBase<TemplateParametersDefinition> {
    @Override
    protected TemplateParametersDefinition createMessageParameters() {
        final TemplateParametersDefinition messageParameters=createEmptyMessageParameters();
        messageParameters.addParameters(createParameters(FIRST_PARAMETER, SECOND_PARAMETER));
        return messageParameters;
    }

    @Override
    protected TemplateParametersDefinition createEmptyMessageParameters() {
        return ObjectCreatorUtil.createInstanceByConstructor(TemplateParametersDefinition.class);
    }

    @Override
    protected String expectedErrorMessage(String parameterName) {
        return TemplateParametersDefinition.errorMessage(parameterName);
    }
}