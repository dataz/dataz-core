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

package org.failearly.common.internal.message.definition;

import org.failearly.common.internal.message.MessageParametersTestBase;
import org.failearly.common.test.annotations.TestsFor;
import org.failearly.common.test.ObjectCreator;

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
        return ObjectCreator.createInstanceByConstructor(TemplateParametersDefinition.class);
    }

    @Override
    protected String expectedErrorMessage(String parameterName) {
        return TemplateParametersDefinition.errorMessage(parameterName);
    }
}