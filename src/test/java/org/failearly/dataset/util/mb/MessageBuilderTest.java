/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.util.mb;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * MessageBuilderTest contains tests for MessageBuilder.
 */
public class MessageBuilderTest {

    private final MessageBuilder messageBuilder=MessageBuilders.create();

    @Test
    public void add_single_lines__should_finally_result_in_a_message_starting_with_a_nl_per_line() throws Exception {
        // act / when
        messageBuilder.line("1st line");
        messageBuilder.line("2nd line");

        // assert / then
        assertThat(messageBuilder.build(), is("\n1st line\n2nd line"));
    }

    @Test
    public void create_with_a_first_line__should_start_without_nl() throws Exception {
        // arrange / given
        final MessageBuilder messageBuilder=MessageBuilders.create("Initial text");

        // act / when
        messageBuilder.line("1st line").line("2nd line");

        // assert / then
        assertThat(messageBuilder.build(), is("Initial text\n1st line\n2nd line"));
    }

    @Test
    public void adding_first_lines__should_start_without_nl_and_should_separated_by_blank() throws Exception {
        // arrange / given
        messageBuilder.line("1st line").line("2nd line");

        // act / when
        messageBuilder.firstLine("Initial text").firstLine("Second text on first line.");

        // assert / then
        assertThat(messageBuilder.build(), is("Initial text Second text on first line.\n1st line\n2nd line"));
    }

    @Test
    public void existing_arguments__should_be_replaced_by_argument_value() throws Exception {
        // arrange / given
        final MessageBuilder messageBuilder=MessageBuilders.create("Initial text (__arg0__)(Hello __name__)");
        messageBuilder.line("__arg0__,__arg0__")
            .line("2nd line with __name__");

        // act / when
        messageBuilder.argument("arg0", 314)
            .argument("name", "Loddar");

        // assert / then
        assertThat(messageBuilder.build(), is("Initial text (314)(Hello Loddar)\n314,314\n2nd line with Loddar"));
    }


    @Test
    public void none_existing_arguments__should_not_be_replaced() throws Exception {
        // arrange / given
        messageBuilder.line("__not_existing__")
            .line("Hello __name__!");

        // act / when
        messageBuilder.argument("name", "Loddar");

        // assert / then
        assertThat(messageBuilder.build(), is("\n__not_existing__\nHello Loddar!"));
    }

    @Test
    public void escaped_arguments_in_line__should_not_be_replaced() throws Exception {
        // arrange / given
        messageBuilder.line("Hello \\__name__!");

        // act / when
        messageBuilder.argument("name", "Loddar");

        // assert / then
        assertThat(messageBuilder.build(), is("\nHello __name__!"));
    }

    @Test
    public void recursive_arguments__should_be_replaced_with_last_value() throws Exception {
        // arrange / given
        messageBuilder.line("Hello __name__!");

        // act / when
        messageBuilder.argument("name", "__val__")
                      .argument("val", "Loddar");

        // assert / then
        assertThat(messageBuilder.build(), is("\nHello Loddar!"));
    }

    @Test
    public void infinite_recursive_arguments__should_stop_with_first_repeating_argument() throws Exception {
        // arrange / given
        messageBuilder.line("Hello __name__!");

        // act / when
        messageBuilder.argument("name", "__val__")
                      .argument("val", "__name__");

        // assert / then
        assertThat(messageBuilder.build(), is("\nHello __val__!"));
    }

    @Test
    public void use_own_same_prefix_suffix__should_replace_default_settings() throws Exception {
        // arrange / given
        messageBuilder.line("1) Hello $$name$$!").line("2) Hello \\$$name$$!");
        messageBuilder.argument("name", "Loddar");

        // act / when
        messageBuilder.prefixSuffix("$$");

        // assert / then
        assertThat(messageBuilder.build(), is("\n1) Hello Loddar!\n2) Hello $$name$$!"));
    }

    @Test
    public void use_own_diff_prefix_suffix__should_replace_default_settings() throws Exception {
        // arrange / given
        messageBuilder.line("1) Hello ${name}!").line("2) Hello \\${name}!");
        messageBuilder.argument("name", "Loddar");

        // act / when
        messageBuilder.prefix("${").suffix("}");

        // assert / then
        assertThat(messageBuilder.build(), is("\n1) Hello Loddar!\n2) Hello ${name}!"));
    }

    @Test
    public void newline__should_add_single_empty_line() throws Exception {
        // act / when
        messageBuilder.line("Hello")
            .newline()
            .line("world");

        // assert / then
        assertThat(messageBuilder.build(), is("\nHello\n\nworld"));
    }


    @Test
    public void newlines__should_add_multiple_empty_line() throws Exception {
        // act / when
        messageBuilder.line("Hello")
            .newlines(3)
            .line("world");

        // assert / then
        assertThat(messageBuilder.build(), is("\nHello\n\n\n\nworld"));
    }

    @Test
    public void unused_dynamic_arguments__should_not_applied() throws Exception {
        // arrange / given
        messageBuilder.line("Hello world");

        // assert / then
        messageBuilder.argument("not used", () -> {
            throw new UnsupportedOperationException("Should not be used");
        });

        // assert / then
        assertThat(messageBuilder.build(), is("\nHello world"));
    }

    @Test
    public void example__should_create_an_expressive_error_message_with_usage() throws Exception {
        // act / when
        final MessageBuilder messageBuilder=MessageBuilders.create("Missing test method!")
            .argument("baseclass", () -> this.getClass().getSimpleName())
            .newline()
            .exampleStart("Example:")
            .line("public class MyTest extends __baseclass__ {")
            .sub()
            .line("@Test")
            .line("public void test() {")
            .indent("// your test comes here")
            .line("}")
            .end()
            .line("}")
            .exampleEnd();

        // assert / then
        final String build=messageBuilder.build();
        System.err.println(build);
        assertThat(build, is(
            "Missing test method!" +
                "\n" +
                "\nExample:" +
                "\n" +
                "\n8><----------" +
                "\n\tpublic class MyTest extends MessageBuilderTest {" +
                "\n\t\t@Test" +
                "\n\t\tpublic void test() {" +
                "\n\t\t\t// your test comes here" +
                "\n\t\t}" +
                "\n\t}" +
                "\n----------><8"
        ));
    }

    @Test
    public void lazy_builder__should_create_and_build_a_message_when_toString_has_been_called() throws Exception {
        // act / when
        final MessageBuilders.LazyMessage lazyMessage=MessageBuilders.createLazyMessage(
            (mb) -> mb.firstLine("Missing test method!")
        );

        // assert / then
        assertThat(lazyMessage.toString(), is("Missing test method!"));
    }

    @Test
    public void toString_not_called_on_LazyMessage__should_the_LazyBuilder_must_not_called() throws Exception {
        // arrange / given
        final MessageBuilders.LazyBuilder lazyBuilder=mock(MessageBuilders.LazyBuilder.class);

        // act / when
        MessageBuilders.createLazyMessage(lazyBuilder);

        // assert / then
        verifyZeroInteractions(lazyBuilder);
    }


    @Test
    public void snippet__should_ignore_level() throws Exception {
        // arrange / given
        final MessageBuilder messageBuilder=MessageBuilders.create();

        // act / when
        messageBuilder.sub()
            .line("Line 1")
            .snippetStart()
            .line("Line 2")
            .snippetEnd()
            .line("Line 3")
            .end()
            .line("Line 4");


        // assert / then
        assertThat(messageBuilder.build(), is(
            "\n\tLine 1" +
                "\n8><----------" +
                "\n\tLine 2" +
                "\n----------><8" +
                "\n\tLine 3" +
                "\nLine 4"));
    }
}
