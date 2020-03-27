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

package org.failearly.dataz.internal.common.internal.message.resolver.parameter;

import org.failearly.dataz.internal.common.internal.message.MessageParameters;
import org.failearly.dataz.internal.common.internal.message.MessageParametersTestBase;
import org.failearly.dataz.internal.common.internal.message.definition.TemplateParametersDefinition;
import org.failearly.dataz.common.test.annotations.TestsFor;

import static org.failearly.dataz.internal.common.classutils.ObjectCreatorUtil.createInstanceByConstructor;

/**
 * TemplateParametersDefinitionTest contains tests for TemplateParametersDefinition.
 */
@TestsFor({MessageParametersCollection.class})
public class MessageParametersCollectionsTest extends MessageParametersTestBase<MessageParametersCollection> {


    @Override
    protected MessageParametersCollection createMessageParameters() {
        return createEmptyMessageParameters()
            .addMessageParameters(createMessageParametersInstance())
            .addMessageParameters(createMessageParametersInstance(FIRST_PARAMETER))
            .addMessageParameters(createMessageParametersInstance(SECOND_PARAMETER))
            .addMessageParameters(createMessageParametersInstance(FIRST_PARAMETER, SECOND_PARAMETER));
    }

    private static MessageParameters createMessageParametersInstance(String... parameters) {
        return createInstanceByConstructor(TemplateParametersDefinition.class).addParameters(parameters);
    }

    @Override
    protected MessageParametersCollection createEmptyMessageParameters() {
        return new MessageParametersCollection();
    }

    @Override
    protected String expectedErrorMessage(String parameterName) {
        return TemplateParametersDefinition.errorMessage(parameterName);
    }
}