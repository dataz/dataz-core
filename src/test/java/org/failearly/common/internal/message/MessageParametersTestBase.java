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

package org.failearly.common.internal.message;

import org.failearly.common.test.annotations.TestsFor;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;

/**
 * Base (test) class for {@link MessageParameters}.
 */
@TestsFor(MessageParameters.class)
public abstract class MessageParametersTestBase<T extends MessageParameters> {
    protected static final String FIRST_PARAMETER="arg1";
    protected static final String SECOND_PARAMETER="arg2";

    private final ErrorMessageCollector errorMessageCollector=Mockito.mock(ErrorMessageCollector.class);
    private static final MessageArgumentsImpl NO_ARGUMENTS=createMessageArguments();

    private static MessageArgumentsImpl createMessageArguments(String... parameterNames) {
        final String dontCare="don't care";
        final MessageArgumentsImpl messageArguments=new MessageArgumentsImpl();
        for (String argumentName : parameterNames) {
            messageArguments.addMandatoryArgument(argumentName, dontCare);
        }

        return messageArguments;
    }

    protected static String[] createParameters(String... strings) {
        return strings;
    }

    /**
     * Create an empty {@link MessageParameters} object.
     * @return empty message parameters object.
     */
    protected abstract T createEmptyMessageParameters();

    @After
    public void noMoreInteractionsOnErrorCollector() throws Exception {
        Mockito.verifyNoMoreInteractions(errorMessageCollector);
    }

    @Test
    public void no_declared_parameters__should_accept_everything() throws Exception {
        // arrange / given
        final MessageParameters messageParameters=createEmptyMessageParameters();

        // assert / then
        assertThat("No arguments",
            messageParameters.messageArgumentsAreValid(NO_ARGUMENTS, errorMessageCollector), is(true)
        );
        assertThat("Any arguments",
            messageParameters.messageArgumentsAreValid(createMessageArguments("a","b","x"), errorMessageCollector),
            is(true)
        );
        Mockito.verifyZeroInteractions(errorMessageCollector);
    }

    /**
     * Create an prepared not empty {@link MessageParameters} instance.
     * @return message parameters.
     */
    protected abstract T createMessageParameters();


    @Test
    public void all_mandatory_arguments_available__should_be_valid() throws Exception {
        // arrange / given
        final MessageParameters messageParameters=createMessageParameters();

        // assert / then
        final MessageArgumentsImpl exactlyMatchedArguments=createMessageArguments(FIRST_PARAMETER, SECOND_PARAMETER);
        assertThat("exactly matched",
            messageParameters.messageArgumentsAreValid(exactlyMatchedArguments, errorMessageCollector),
            is(true)
        );
    }

    @Test
    public void all_mandatory_arguments_and_additional_optional_available__should_be_valid() throws Exception {
        // arrange / given
        final MessageParameters messageParameters=createMessageParameters();

        // assert / then
        final MessageArgumentsImpl oneOptionalArgument=createMessageArguments(
                                                FIRST_PARAMETER,
                                                SECOND_PARAMETER,
                                                "anyOptionalArgument"
                            );
        assertThat("all mandatory and an optional argument",
            messageParameters.messageArgumentsAreValid(oneOptionalArgument, errorMessageCollector)
            , is(true)
        );
    }

    /**
     * Create the expected error message for specified parameter name.
     * @param parameterName the parameter name
     * @return the expected error message.
     */
    protected abstract String expectedErrorMessage(String parameterName);

    @Test
    public void missing_all_arguments__should_not_be_valid() throws Exception {
        // arrange / given
        final MessageParameters messageParameters=createMessageParameters();

        // assert / then
        assertThat("No arguments at all",
            messageParameters.messageArgumentsAreValid(NO_ARGUMENTS, errorMessageCollector), is(false)
        );
        Mockito.verify(errorMessageCollector, atLeastOnce()).addErrorMessage(expectedErrorMessage(FIRST_PARAMETER));
        Mockito.verify(errorMessageCollector, atLeastOnce()).addErrorMessage(expectedErrorMessage(SECOND_PARAMETER));
    }

    @Test
    public void missing_single_argument__should_not_be_valid() throws Exception {
        // arrange / given
        final MessageParameters messageParameters=createMessageParameters();

        // assert / then
        assertThat("Single missing argument",
            messageParameters.messageArgumentsAreValid(createMessageArguments(FIRST_PARAMETER), errorMessageCollector),
            is(false)
        );
        Mockito.verify(errorMessageCollector, atLeastOnce()).addErrorMessage(expectedErrorMessage(SECOND_PARAMETER));
    }

    @Test
    public void misspelled_arguments__should_not_be_valid() throws Exception {
        // arrange / given
        final MessageParameters messageParameters=createMessageParameters();

        // assert / then
        final String misspelledParameter="Arg2";
        assertThat("Correct number, but spelling mistake",
            messageParameters.messageArgumentsAreValid(createMessageArguments(FIRST_PARAMETER, misspelledParameter), errorMessageCollector),
            is(false)
        );
        Mockito.verify(errorMessageCollector, atLeastOnce()).addErrorMessage(expectedErrorMessage(SECOND_PARAMETER));

    }
}
