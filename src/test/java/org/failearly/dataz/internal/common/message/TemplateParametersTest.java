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

package org.failearly.dataz.internal.common.message;

import org.failearly.dataz.common.test.annotations.TestsFor;
import org.failearly.dataz.common.test.ExceptionVerifier;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TemplateParametersTest contains tests for {@link TemplateParameters} using {@link InlineMessageTemplate}.
 */
@TestsFor(TemplateParameters.class)
public class TemplateParametersTest {
    @Test
    public void all_arguments_set__should_apply_on_template() {
        // arrange / given
        final String mand1="First Parameter Value";
        final String mand2="Second Parameter Value";
        final String mand3="Fourth Parameter Value";
        final String opt="Third (optional) Parameter Value";

        // act / when
        final org.failearly.dataz.internal.common.message.Message message= TestMessage.create((messageBuilder)->messageBuilder
            .with(TestMessage.MANDATORY_PARAMETER_1, mand1)
            .with(TestMessage.MANDATORY_PARAMETER_2, mand2)
            .with(TestMessage.MANDATORY_PARAMETER_3, mand3)
            .with(TestMessage.OPTIONAL_PARAMETER, opt)
        );

        // assert / then
        assertThat(message.toString())
            .isEqualTo(expectedText(mand1, mand2, opt, mand3));
    }


    @Test
    public void missing_arguments__should_result_in_an_exception_explaining_the_mandatory_parameters() {
        // act / when
        final MessageBuilder messageBuilder= TestMessage.create()
                .with(TestMessage.MANDATORY_PARAMETER_1,"any value")
                .with(TestMessage.MANDATORY_PARAMETER_2,"any value");

        // assert / then
        ExceptionVerifier.on(messageBuilder::build)
                .expect(IllegalArgumentException.class)
                .expect(                                                  //
                    "Missing template parameter(s): \n\n" +                 //
                        "Missing mandatory parameter: 'mandatory3'"       //
                )
                .verify();
    }

    private static String expectedText(CharSequence... args) {
        return String.join(InlineMessageTemplate.DEFAULT_SEPARATOR, args);
    }


// ------------
// The messages
// ------------

    @InlineMessageTemplate({                                   //
            TestMessage.TEMPLATE1,  //
            TestMessage.TEMPLATE2,  //
            TestMessage.TEMPLATE3,   //
            TestMessage.TEMPLATE4   //
        }
    )
    @TemplateParameters({
        TestMessage.MANDATORY_PARAMETER_3
    })
    private static class TestMessage extends MessageBuilderBase<TestMessage> implements Mandatory {

        static final String MANDATORY_PARAMETER_3="mandatory3";
        static final String OPTIONAL_PARAMETER="optional";
        static final String TEMPLATE1="$"+ MANDATORY_PARAMETER_1;
        static final String TEMPLATE2="$"+ MANDATORY_PARAMETER_2;
        static final String TEMPLATE3="$"+ OPTIONAL_PARAMETER;
        static final String TEMPLATE4="$"+ MANDATORY_PARAMETER_3;


        private TestMessage() {
            super(TestMessage.class);
        }

        static TestMessage create() {
            return new TestMessage();
        }
        static Message create(Initializer<TestMessage> initializer) {
            return create().buildLazyMessage(initializer);
        }
    }

    @TemplateParameters({
            Mandatory.MANDATORY_PARAMETER_1,
            Mandatory.MANDATORY_PARAMETER_2
    })
    private interface Mandatory {
        String MANDATORY_PARAMETER_1="mandatory1";
        String MANDATORY_PARAMETER_2="mandatory2";
    }

}
