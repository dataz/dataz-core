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

package org.failearly.dataz.internal.common.internal.message;

import org.failearly.dataz.internal.common.message.MessageArguments;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MessageArgumentsTest contains tests for ... .
 */
public class MessageArgumentsTest {
    @Test
    public void normal_arguments__should_both_be_in_resulting_map() throws Exception {
        // arrange / given
        final MessageArgumentsImpl messageArguments = new MessageArgumentsImpl();

        // act / when
        messageArguments.addMandatoryArgument("val1", 7)
            .addOptionalArgument("val2", "42");

        // assert / then
        assertThat(messageArguments.asMap())
            .containsEntry("val1", 7)
            .containsEntry("val2", "42");
    }

    @Test
    public void mandatory_arguments_must_not_be_overwritten() throws Exception {
        // arrange / given
        final MessageArgumentsImpl messageArguments = new MessageArgumentsImpl();

        // act / when
        messageArguments.addMandatoryArgument("val1", 7)
            .addOptionalArgument("val1", "42");

        // assert / then
        assertThat(messageArguments.asMap())
            .containsEntry("val1", 7);
    }

    @Test
    public void overwriting_normal_arguments__should_the_overwritten_value_in_resulting_map() throws Exception {
        // arrange / given
        final MessageArgumentsImpl messageArguments = new MessageArgumentsImpl();

        // act / when
        messageArguments
            .addMandatoryArgument("val1", 7)
            .addMandatoryArgument("val1", 123)
            .addMandatoryArgument("val2", "42");

        // assert / then
        assertThat(messageArguments.asMap())
            .containsEntry("val1", 123)
            .containsEntry("val2", "42");
    }


    @Test
    public void derived_argument__should_both_be_in_resulting_map_if_derived_value_has_not_been_set() throws Exception {
        // arrange / given
        final MessageArgumentsImpl messageArguments = new MessageArgumentsImpl();
        messageArguments.addMandatoryArgument("val2", "42");

        // act / when
        messageArguments.addDerivedArgument("val1", (MessageArguments.Accessor ma) -> ma.getValue("val2", String.class)
            .map(Integer::parseInt)
            .orElse(-1)
        );


        // assert / then
        assertThat(messageArguments.asMap())
            .containsEntry("val1", 42)
            .containsEntry("val2", "42");
    }

    @Test
    public void chained_arguments__should_both_be_in_resulting_map() throws Exception {
        // arrange / given
        final MessageArgumentsImpl messageArguments = new MessageArgumentsImpl();
        messageArguments.addMandatoryArgument("val3", "42");

        // act / when
        messageArguments.addDerivedArgument("val2", (MessageArguments.Accessor ma) -> ma.getValue("val3", String.class)
            .orElse(null)
        );
        messageArguments.addDerivedArgument("val1", (MessageArguments.Accessor ma) -> ma.getValue("val2", String.class)
            .map(Integer::parseInt)
            .orElse(0)
        );


        // assert / then
        assertThat(messageArguments.asMap())
            .containsEntry("val1", 42)
            .containsEntry("val2", "42")
            .containsEntry("val3", "42");
    }

    @Test
    public void overwriting__chained_value__should_overwritten_value() throws Exception {
        // arrange / given
        final MessageArgumentsImpl messageArguments = new MessageArgumentsImpl();
        messageArguments.addMandatoryArgument("val3", "42");
        messageArguments.addDerivedArgument("val2", (MessageArguments.Accessor ma) -> ma.getValue("val3", String.class)
            .orElse(null)
        );
        messageArguments.addDerivedArgument("val1", (MessageArguments.Accessor ma) -> ma.getValue("val2", String.class)
            .map(Integer::parseInt)
            .orElse(0)
        );

        // act / when
        messageArguments.addMandatoryArgument("val2", "3141");


        // assert / then
        assertThat(messageArguments.asMap())
            .containsEntry("val1", 3141)
            .containsEntry("val2", "3141")
            .containsEntry("val3", "42");
    }

    @Test
    public void endless_loop__should_be_detected() throws Exception {
        // arrange / given
        final MessageArgumentsImpl messageArguments = new MessageArgumentsImpl();

        // act / when
        messageArguments.addDerivedArgument("val2", (MessageArguments.Accessor ma) -> ma.getValue("val1", String.class)
            .orElse(null)
        );
        messageArguments.addDerivedArgument("val1", (MessageArguments.Accessor ma) -> ma.getValue("val2", String.class)
            .orElse(null)
        );

        // assert / then
        assertThat(messageArguments.asMap())
            .containsEntry("val1", null)
            .containsEntry("val2", null);
    }

    @Test
    public void using_a_derived_object_should_not_result_in_default_value() throws Exception {
        // arrange / given
        final MessageArgumentsImpl messageArguments = new MessageArgumentsImpl();
        messageArguments.addDerivedArgument("val1", a -> "The first value");

        // act / when
        messageArguments.addDerivedArgument("val2", a -> ""
            + a.getValue("val1", String.class, "First Default Value")
            + "/" + a.getValue("val1", String.class, "Second Default Value"));

        // assert / then
        assertThat(messageArguments.asMap())
            .containsEntry("val1", "The first value")
            .containsEntry("val2", "The first value/The first value");
    }
}