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

package org.failearly.dataz.internal.common.internal.message.definition;

import org.failearly.dataz.common.Tests;
import org.failearly.dataz.internal.common.internal.message.ErrorMessageCollector;
import org.failearly.dataz.internal.common.internal.message.MessageParameters;
import org.failearly.dataz.internal.common.internal.message.MessageParametersBase;
import org.failearly.dataz.internal.common.internal.message.MessageArgumentsImpl;
import org.failearly.dataz.internal.common.message.TemplateParameters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * TemplateParametersDefinition is a simple implementation of MessageParameters, which checks only the names of the
 * parameters for existence.
 */
@Tests("TemplateParametersDefinitionTest")
public final class TemplateParametersDefinition extends MessageParametersBase<TemplateParameters> {
    private final Set<String> parameterNames=new HashSet<>();

    private TemplateParametersDefinition() {
        super(TemplateParameters.class);
    }

    public TemplateParametersDefinition addParameters(String... args) {
        parameterNames.addAll(Arrays.asList(args));
        return this;
    }

    @Override
    public boolean messageArgumentsAreValid(
            MessageArgumentsImpl messageArguments,
            ErrorMessageCollector errorMessageCollector) {

        boolean valid=true;
        for (String parameterName : parameterNames) {
            if( ! messageArguments.hasMandatoryParameter(parameterName) ) {
                errorMessageCollector.addErrorMessage(errorMessage(parameterName));
                valid = false;
            }
        }
        return valid;
    }

    public static String errorMessage(String parameterName) {
        return "Missing mandatory parameter: '" + parameterName + "'";
    }

    @Override
    protected MessageParameters doCreate(TemplateParameters annotation) {
        return new TemplateParametersDefinition().addParameters(annotation.value());
    }
}
