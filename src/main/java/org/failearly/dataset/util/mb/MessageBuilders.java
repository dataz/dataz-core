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

/**
 * Factory class for creating {@link MessageBuilder} instances.
 */
public final class MessageBuilders {
    private MessageBuilders() {
    }

    /**
     * Create a message builder instance with a first line (not starting with a new line).
     *
     * @param firstLine the first line (not starting with a newline or indention).
     *
     * @return new message builder.
     */
    public static MessageBuilder create(String firstLine) {
        return new MessageBuilderImpl(firstLine);
    }

    /**
     * Create a message builder instance.
     *
     * @return new message builder.
     */
    public static MessageBuilder create() {
        return create("");
    }

    /**
     * Create a (lazy) message. The message is going to be created when calling {@link
     * LazyMessage#build()}. Example:<br><br>
     * <pre>
     *    MessageBuilders.createLazyMessage((mb)-&gt;mb.firstLine("My lazy message"));
     * </pre>
     *
     * @param lazyBuilder the lazy builder (a functional interface)
     *
     * @return new lazy message.
     */
    public static LazyMessage createLazyMessage(LazyBuilder lazyBuilder) {
        return new LazyMessage(lazyBuilder);
    }

    @FunctionalInterface
    public interface LazyBuilder {
        /**
         * Apply the message builder.
         * @param mb the message builder
         * @return a message builder.
         */
        MessageBuilder apply(MessageBuilder mb);
    }

    /**
     * Holds the {@link LazyBuilder} and apply it the when
     * {@link #build()} or {@link #toString()} will be called.
     *
     * @see LazyBuilder#apply(MessageBuilder)
     */
    public static final class LazyMessage {
        private final LazyBuilder lazyBuilder;
        private String message=null;

        private LazyMessage(LazyBuilder lazyBuilder) {
            this.lazyBuilder=lazyBuilder;
        }

        /**
         * Build the message by applying the {@code lazyBuilder}.
         * @return the message
         *
         * @see LazyBuilder#apply(MessageBuilder)
         */
        public String build() {
            if( message == null)
                message=lazyBuilder.apply(MessageBuilders.create()).build();
            return message;
        }

        /**
         * Calls {@link #build()}.
         * @return result of {@link #build()}
         */
        @Override
        public String toString() {
            return build();
        }
    }
}