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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * MessageBuilderDelegateTest contains tests for ... .
 */
public class MessageBuilderDelegateTest {

    // formatter:off
    @Test
    public void standard_message_builder_methods__should_delegate_to_actually_builder__and__extendable() throws
        Exception {
        // arrange / given
        final CustomMessageBuilder customMessageBuilder=new CustomMessageBuilder(MessageBuilders.create());

        // act / when
        customMessageBuilder.lines(
            "Line 1",
            "Line 2"
        ).linesIndented(
            "Line A",
            "Line B"
        );


        // assert / then
        assertThat(customMessageBuilder.build(),
            is(
                "\nLine 1" +
                    "\nLine 2" +
                    "\n\tLine A" +
                    "\n\tLine B"
            )
        );
    }
    // formatter:on

    private static class CustomMessageBuilder extends MessageBuilderDelegate<CustomMessageBuilder> {

        CustomMessageBuilder(MessageBuilder messageBuilder) {
            super(messageBuilder);
        }

        public CustomMessageBuilder linesIndented(String... lines) {
            return this.sub().lines(lines).end();
        }
    }

}