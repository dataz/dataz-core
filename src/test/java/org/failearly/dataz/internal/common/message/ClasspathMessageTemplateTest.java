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
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ClasspathMessageTemplateTest contains tests for {@link ClasspathMessageTemplate}.
 */
@TestsFor(ClasspathMessageTemplate.class)
public class ClasspathMessageTemplateTest {

    private static final String ARGUMENT_1="argument 1";
    private static final String ARGUMENT_2="argument 2";

    @Test
    public void what_should_happen_with_no_template() throws Exception {
        // arrange / given
        final MessageBuilder messageTemplate=NoTemplateMessage.create();

        // act / when
        final Message message=messageTemplate.build();

        // assert / then
        assertThat(message.toString())
            .isEqualTo(NoTemplateMessage.EXPECTED_TEXT);
    }

    private static String expectedText(CharSequence... args) {
        return String.join("\n", args);
    }


    @Test
    public void what_should_happen_with_template_and_some_arguments() throws Exception {
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
    public void what_should_happen_with_same_template__but_different_arguments() throws
        Exception {
        // arrange / given
        final TemplateMessage messageBuilder=TemplateMessage.create();

        // act / when
        final Message message1=messageBuilder      //
            .with(TemplateMessage.PAR1, ARGUMENT_1)     //
            .with(TemplateMessage.PAR2, ARGUMENT_2)     //
            .build();

        final Message message2=messageBuilder      //
            .with(TemplateMessage.PAR1, ARGUMENT_1 + "(2nd)")     //
            .with(TemplateMessage.PAR2, ARGUMENT_2 + "(2nd)")     //
            .build();

        // assert / then
        assertThat(message1.generate())
            .isNotEqualTo(message2.generate());
    }

    @Test
    public void what_should_happen_with_same_template__but_one_build_lazy() throws Exception {
        // arrange / given
        final TemplateMessage messageBuilder=TemplateMessage.create();

        // act / when
        final Message message=messageBuilder      //
            .with(TemplateMessage.PAR1, ARGUMENT_1)     //
            .with(TemplateMessage.PAR2, ARGUMENT_2)     //
            .build();

        final Message lazyMessage=messageBuilder.buildLazyMessage((mb)->mb
                        .with(TemplateMessage.PAR1, ARGUMENT_1 + "(2nd)")
                        .with(TemplateMessage.PAR2, ARGUMENT_2 + "(2nd)")
            );

        // assert / then
        assertThat(message.generate())
            .isNotEqualTo(lazyMessage.generate());
    }

    @Test
    public void what_should_happen_with_message_builder_without_explicit_set_template_resource() throws Exception {
        // arrange / given
        final DefaultTemplateMessage messageBuilder=DefaultTemplateMessage.create();

        // act / when
        final Message message=messageBuilder      //
            .with(DefaultTemplateMessage.PAR1, ARGUMENT_1)     //
            .with(DefaultTemplateMessage.PAR2, ARGUMENT_2)     //
            .build();

        // assert / then
        assertThat(message.generate())
            .isEqualTo(expectedText(ARGUMENT_1, ARGUMENT_2));
    }


// -----------------------------
// The message (builder) classes
// -----------------------------

    @ClasspathMessageTemplate(value="no_template.txt")
    private static class NoTemplateMessage extends MessageBuilderBase<NoTemplateMessage> {

        static final String TEXT1="First raw text";
        static final String TEXT2="Second raw text";

        static final String EXPECTED_TEXT=expectedText(TEXT1, TEXT2);

        private NoTemplateMessage() {
            super(NoTemplateMessage.class);
        }

        public static NoTemplateMessage create() {
            return new NoTemplateMessage();
        }
    }

    @ClasspathMessageTemplate(    //
        value="template.txt.vm"   //
    )
    private static class TemplateMessage extends MessageBuilderBase<TemplateMessage> {
        static final String PAR1="par1";
        static final String PAR2="par2";


        TemplateMessage() {
            super(TemplateMessage.class);
        }

        public static TemplateMessage create() {
            return new TemplateMessage();
        }
    }

    @ClasspathMessageTemplate
    private static class DefaultTemplateMessage extends MessageBuilderBase<DefaultTemplateMessage> {
        static final String PAR1="par1";
        static final String PAR2="par2";


        DefaultTemplateMessage() {
            super(DefaultTemplateMessage.class);
        }

        public static DefaultTemplateMessage create() {
            return new DefaultTemplateMessage();
        }
    }
}