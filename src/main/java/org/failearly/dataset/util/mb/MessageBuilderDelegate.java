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
 * Extend MessageBuilderDelegate if you want to create your own message builder based on {@link MessageBuilder}.
 */
@SuppressWarnings("unchecked")
public abstract class MessageBuilderDelegate<T extends MessageBuilderDelegate> implements MessageBuilder {

    private final MessageBuilder messageBuilder;
    private boolean initCalled=false;

    protected MessageBuilderDelegate(MessageBuilder messageBuilder) {
        this.messageBuilder=messageBuilder;
    }

    /**
     * Custom initialization. If not called explicitly it will be called by {@link #build()}.
     * If you want to do some custom initialization, overwrite {@link #doInit()};
     * @return this builder
     */
    public final T init() {
        if( ! this.initCalled ) {
            doInit();
        }
        this.initCalled = true;
        return (T) this;
    }

    /**
     * Overwrite, if some custom initialization is necessary. The default implementation does nothing.
     * <br><br>
     * Remark: Called by {@link #init()} only once.
     */
    protected void doInit() {
        // do nothing
    }

    /**
     * Prepare the message. Will be called by {@link #build()}. The default implementation does nothing.
     */
    protected void prepare() {
        // do nothing
    }

    public final T prefixSuffix(String prefixSuffix) {
        messageBuilder.prefixSuffix(prefixSuffix);
        return (T) this;
    }

    public final T suffix(String suffix) {
        messageBuilder.suffix(suffix);
        return (T) this;
    }

    public final T prefix(String prefix) {
        messageBuilder.prefix(prefix);
        return (T) this;
    }

    public final T argument(String argumentName, Object argument) {
        messageBuilder.argument(argumentName, argument);
        return (T) this;
    }

    public final T argument(String argumentName, MessageBuilder.Argument argument) {
        messageBuilder.argument(argumentName, argument);
        return (T) this;
    }

    public final T firstLine(String firstLine) {
        messageBuilder.firstLine(firstLine);
        return (T) this;
    }

    public final T line(String line) {
        messageBuilder.line(line);
        return (T) this;
    }

    public final T lines(String... lines) {
        messageBuilder.lines(lines);
        return (T) this;
    }

    public final T newline() {
        messageBuilder.newline();
        return (T) this;
    }

    public final T newlines(int numNewLines) {
        messageBuilder.newlines(numNewLines);
        return (T) this;
    }

    public final T sub() {
        messageBuilder.sub();
        return (T) this;
    }

    public final T end() {
        messageBuilder.end();
        return (T) this;
    }

    public final T indent(String indentedLine) {
        messageBuilder.indent(indentedLine);
        return (T) this;
    }

    public final T snippetStart() {
        messageBuilder.snippetStart();
        return (T) this;
    }

    public final T snippetEnd() {
        messageBuilder.snippetEnd();
        return (T) this;
    }

    public final T lineNotIndented(String line) {
        messageBuilder.lineNotIndented(line);
        return (T) this;
    }

    public final T exampleStart(String line) {
        messageBuilder.exampleStart(line);
        return (T) this;
    }

    @Override
    public final T exampleStart() {
        messageBuilder.exampleStart();
        return (T) this;
    }

    public final T exampleEnd() {
        messageBuilder.exampleEnd();
        return (T) this;
    }

    public final String build() {
        init();
        prepare();
        return messageBuilder.build();
    }

    @Override
    public final String toString() {
        return messageBuilder.toString();
    }

}
