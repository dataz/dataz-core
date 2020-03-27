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

package org.failearly.dataz.internal.common.message;

import org.failearly.dataz.common.test.ExceptionVerifier;
import org.failearly.dataz.common.test.annotations.TestsFor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * InlineMessageTemplateTest contains tests for {@link InlineMessageTemplate}.
 */
@TestsFor(InlineMessageTemplate.class)
public class InlineMessageTemplateTest {

    private static final String ARGUMENT_1="argument 1";
    private static final String ARGUMENT_2="argument 2";

    @Test
    public void no_template_parameters__should_return_the_raw_text() throws Exception {
        // arrange / given
        final MessageBuilder messageTemplate=NoTemplateMessage.create();

        // act / when
        final Message message=messageTemplate.build();

        // assert / then
        assertThat(message.toString())
            .isEqualTo(NoTemplateMessage.EXPECTED_TEXT);
    }

    @Test
    public void using_lazy_message__should_return_same_message() throws Exception {
        // act / when
        final Message message=NoTemplateMessage.create((mg) -> mg);

        // assert / then
        assertThat(message.toString())
            .isEqualTo(NoTemplateMessage.EXPECTED_TEXT);
    }

    @Test
    public void not_calling_toString_on_message_object__must_not_apply_initializer() throws Exception {
        // act / when
        // assert / then
        final Message message=NoTemplateMessage.create(
            (mg) -> {
                fail("Message Generator Initializer must be executed!");
                return mg;
            }
        );

        assertThat(message).isNotNull();
    }

    @Test
    public void no_template_annotation__build_should_throw_an_exception() throws Exception {
        // act / when
        final MessageBuilder message=MissingTemplateAnnotation.create();

        // assert / then
        ExceptionVerifier.on(message::build)
            .expect(IllegalArgumentException.class)
            .expect("At least one Message Template Annotation must be assigned.")
            .verify();
    }

    private static String expectedText(String... args) {
        return String.join(InlineMessageTemplate.DEFAULT_SEPARATOR, args);
    }


    @Test
    public void using_simple_template_message__should_replace_parameter_with_values() throws Exception {
        // arrange / given
        final TemplateMessage messageBuilder=TemplateMessage.create();

        // act / when
        final Message message=messageBuilder      //
            .with(TemplateMessage.PAR1, ARGUMENT_1)     //
            .with(TemplateMessage.PAR2, ARGUMENT_2)     //
            .build();

        // assert / then
        assertThat(message.generate())
            .isEqualTo(expectedText(ARGUMENT_1, ARGUMENT_2));
    }

    @Test
    public void generated_message__should_be_cached() throws Exception {
        // arrange / given
        final TemplateMessage messageBuilder=TemplateMessage.create();

        // act / when
        final Message message=messageBuilder      //
            .with(TemplateMessage.PAR1, ARGUMENT_1)     //
            .with(TemplateMessage.PAR2, ARGUMENT_2)     //
            .build();

        // assert / then
        final String firstGeneratedMessage=message.generate();
        assertThat(firstGeneratedMessage)
            .isSameAs(message.generate())
            .isSameAs(message.toString());
    }

    @Test
    public void lazy_using_simple_template_message__should_replace_parameter_with_values() throws Exception {
        // act / when
        final Message message=TemplateMessage.create(
            (mb) -> mb.with(TemplateMessage.PAR1, ARGUMENT_1)  //
                       .with(TemplateMessage.PAR2, ARGUMENT_2) //
        );

        // assert / then
        assertThat(message.generate())
            .isEqualTo(expectedText(ARGUMENT_1, ARGUMENT_2));
    }

    @Test
    public void using_derived_message__should_return_message_from_base_template() throws Exception {
        // arrange / given
        final DerivedTemplateMessage messageBuilder=DerivedTemplateMessage.create();

        // act / when
        final Message message=messageBuilder                                     //
            .with(DerivedTemplateMessage.PAR1, ARGUMENT_1)     //
            .with(DerivedTemplateMessage.PAR2, ARGUMENT_2)     //
            .build();                                    //

        // assert / then
        assertThat(message.generate())
            .isEqualTo(expectedText(ARGUMENT_1, ARGUMENT_2));
    }

    @Test
    public void using_message_with_standard_values__should_generate_message_with_standard_values() throws Exception {
        // arrange / given
        final TemplateMessageWithStandardValues messageBuilder=TemplateMessageWithStandardValues.create();

        // act / when
        final Message message=messageBuilder             //
            .build();                                    //

        // assert / then
        assertThat(message.generate())
            .isEqualTo(
                expectedText(
                    TemplateMessageWithStandardValues.STANDARD_VAL1, TemplateMessageWithStandardValues.STANDARD_VAL2
                )
            );
    }

    @Test
    public void overwriting_standard_values__should_use_not_standard_values()
        throws Exception {
        // arrange / given
        final TemplateMessageWithStandardValues messageBuilder=TemplateMessageWithStandardValues.create();

        // act / when
        final String notStandardValue="not standard value";
        final Message message=messageBuilder             //
            .with(TemplateMessageWithStandardValues.PAR1, notStandardValue) //
            .build();                                    //

        // assert / then
        assertThat(message.generate())
            .isEqualTo(
                expectedText(
                    notStandardValue, TemplateMessageWithStandardValues.STANDARD_VAL2
                )
            );
    }

    @Test
    public void lazy_overwriting_standard_values__should_use_not_standard_values()
        throws Exception {
        // arrange / given
        final MessageBuilderBase<?> messageBuilder=TemplateMessageWithStandardValues.create();

        // act / when
        final String notStandardValue="not standard value";
        final Message message=messageBuilder.buildLazyMessage(
            (mb) -> mb.with(TemplateMessageWithStandardValues.PAR1, notStandardValue)
        );

        // assert / then
        assertThat(message.generate())
            .isEqualTo(
                expectedText(
                    notStandardValue, TemplateMessageWithStandardValues.STANDARD_VAL2
                )
            );
    }

    @Test
    public void more_then_one_template_annotation__build_should_throw_an_exception() throws Exception {
        // act / when
        final MessageBuilder message=MoreThenOneTemplateAnnotation.create();

        // assert / then
        ExceptionVerifier.on(message::build)
            .expect(IllegalArgumentException.class)
            .expect("Exactly one Message Template Annotation is permitted.")
            .verify();
    }

    @Test
    public void message_with_derivable_values__should_be_applied_if_the_parameter_is_not_set() throws Exception {
        // arrange / given
        final Message message = MessageWithDerivableArguments.create((mb) -> {
            mb.withPar2("123");
            return mb;
        });


        // act / when
        // assert / then
        assertThat(message.generate())
                .isEqualTo(
                        expectedText(
                                "123", "123"
                        )
                );
    }

    @Test
    public void message_with_derivable_values__should_be_applied_if_the_value_is_null() throws Exception {
        // arrange / given
        final Message message = MessageWithDerivableArguments.create((mb) -> {
            mb.withPar1(null).withPar2("123");
            return mb;
        });


        // act / when
        // assert / then
        assertThat(message.generate())
                .isEqualTo(
                        expectedText(
                                "123", "123"
                        )
                );
    }

    @Test
    public void message_with_derivable_values__must_not_applied__if_the_value_is_set() throws Exception {
        // arrange / given
        final Message message = MessageWithDerivableArguments.create((mb) -> {
            mb.withPar1(42).withPar2("123");
            return mb;
        });


        // act / when
        // assert / then
        assertThat(message.generate())
                .isEqualTo(
                        expectedText(
                                "42", "123"
                        )
                );
    }

// -----------------------------
// The message (builder) classes
// -----------------------------

    @InlineMessageTemplate(value={NoTemplateMessage.TEXT1, NoTemplateMessage.TEXT2},
        separator=NoTemplateMessage.SEP)
    private static class NoTemplateMessage extends MessageBuilderBase<NoTemplateMessage> {

        static final String SEP="/";
        static final String TEXT1="First raw text";
        static final String TEXT2="Second raw text";

        static final String EXPECTED_TEXT=String.join(SEP, TEXT1, TEXT2);

        private NoTemplateMessage() {
            super(NoTemplateMessage.class);
        }

        static NoTemplateMessage create() {
            return new NoTemplateMessage();
        }

        static Message create(Initializer<NoTemplateMessage> initializer) {
            return create().buildLazyMessage(initializer);
        }
    }

    @InlineMessageTemplate(                                                           //
        value={TemplateMessage.TEMPLATE1, TemplateMessage.TEMPLATE2}       //
    )
    private static class TemplateMessage extends MessageBuilderBase<TemplateMessage> {
        static final String PAR1="par1";
        static final String PAR2="par2";
        static final String TEMPLATE1="$" + PAR1;
        static final String TEMPLATE2="$" + PAR2;


        TemplateMessage() {
            super(TemplateMessage.class);
        }

        static TemplateMessage create() {
            return new TemplateMessage();
        }

        static Message create(Initializer<TemplateMessage> initializer) {
            return create().buildLazyMessage(initializer);
        }
    }

    @InlineMessageTemplate(                                                           //
            value={MessageWithDerivableArguments.TEMPLATE1, MessageWithDerivableArguments.TEMPLATE2}       //
    )
    @TemplateParameters(MessageWithDerivableArguments.PAR2)
    private static class MessageWithDerivableArguments extends MessageBuilderBase<MessageWithDerivableArguments> {

        static final String PAR1="par1";
        static final String PAR2="par2";
        static final String TEMPLATE1="$" + PAR1;
        static final String TEMPLATE2="$" + PAR2;

        private MessageWithDerivableArguments() {
            super(MessageWithDerivableArguments.class);
        }

        static Message create(Initializer<MessageWithDerivableArguments> initializer) {
            return new MessageWithDerivableArguments().buildLazyMessage(initializer);
        }

        @Override
        protected void standardArgumentsHook(MessageArguments messageArguments) {
            messageArguments.addDerivedArgument(PAR1, accessor -> accessor.getValue(PAR2, String.class, null));
        }

        MessageWithDerivableArguments withPar1(Integer value) {
            return with(PAR1, value);
        }

        MessageWithDerivableArguments withPar2(String intString) {
            return this.with(PAR2, intString);
        }
    }

    // CAUTION: No Template Annotation is correct
    private static class MissingTemplateAnnotation extends MessageBuilderBase<MissingTemplateAnnotation> {
        private MissingTemplateAnnotation() {
            super(MissingTemplateAnnotation.class);
        }

        static MissingTemplateAnnotation create() {
            return new MissingTemplateAnnotation();
        }
    }


    @InlineMessageTemplate(                                                           //
        value={TemplateMessageBase.TEMPLATE1, TemplateMessageBase.TEMPLATE2}       //
    )
    private static abstract class TemplateMessageBase<T extends TemplateMessageBase<T>> extends
        MessageBuilderBase<T> {
        static final String PAR1="par1";
        static final String PAR2="par2";
        static final String TEMPLATE1="$" + PAR1;
        static final String TEMPLATE2="$" + PAR2;


        TemplateMessageBase(Class<T> clazz) {
            super(clazz);
        }
    }

    private static class DerivedTemplateMessage extends TemplateMessageBase<DerivedTemplateMessage> {

        private DerivedTemplateMessage() {
            super(DerivedTemplateMessage.class);
        }

        static DerivedTemplateMessage create() {
            return new DerivedTemplateMessage();
        }
    }

    @InlineMessageTemplate(                                                           //
        value={MoreThenOneTemplateAnnotation.TEMPLATE1, MoreThenOneTemplateAnnotation.TEMPLATE2}  //
    )
    private static class MoreThenOneTemplateAnnotation extends TemplateMessageBase<MoreThenOneTemplateAnnotation> {

        private MoreThenOneTemplateAnnotation() {
            super(MoreThenOneTemplateAnnotation.class);
        }

        static MoreThenOneTemplateAnnotation create() {
            return new MoreThenOneTemplateAnnotation();
        }
    }

    private static class TemplateMessageWithStandardValues extends TemplateMessageBase<TemplateMessageWithStandardValues> {

        static final String STANDARD_VAL1="standard val1";
        static final String STANDARD_VAL2="standard val2";

        private TemplateMessageWithStandardValues() {
            super(TemplateMessageWithStandardValues.class);
        }

        static TemplateMessageWithStandardValues create() {
            return new TemplateMessageWithStandardValues();
        }

        @Override
        protected void standardArgumentsHook(MessageArguments messageArguments) {
            messageArguments
                .addMandatoryArgument(PAR1, STANDARD_VAL1)
                .addMandatoryArgument(PAR2, STANDARD_VAL2);
        }
    }


}