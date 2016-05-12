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

package org.failearly.common.internal.message.resolver.parameter;

import org.failearly.common.internal.message.MessageParameters;
import org.failearly.common.internal.message.MessageParametersTestBase;
import org.failearly.common.internal.message.definition.TemplateParametersDefinition;
import org.failearly.common.test.annotations.TestsFor;

import static org.failearly.common.test.ObjectCreator.createInstanceByConstructor;

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