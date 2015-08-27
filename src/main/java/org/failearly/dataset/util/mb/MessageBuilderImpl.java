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

import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * The basic implementation for {@link MessageBuilder}.
 */
final class MessageBuilderImpl implements MessageBuilder {

    private static final String TEMP_ESCAPE="_do-not-use_";

    private String firstLine;
    private final List<String> lines=new ArrayList<>();
    private final Map<String, Argument> arguments=new HashMap<>();
    private String prefix=DEFAULT_PREFIX;
    private String suffix=DEFAULT_SUFFIX;
    private int level=0;
    private String message;

    MessageBuilderImpl(String firstLine) {
        this.firstLine=firstLine;
    }

    @Override
    public MessageBuilder prefixSuffix(String prefixSuffix) {
        return this.prefix(prefixSuffix).suffix(prefixSuffix);
    }

    @Override
    public MessageBuilder suffix(String suffix) {
        this.suffix=suffix;
        return this;
    }

    @Override
    public MessageBuilder prefix(String prefix) {
        this.prefix=prefix;
        return this;
    }

    @Override
    public MessageBuilder argument(String argumentName, Object argument) {
        if( argument==null ) {
            return this.argument(argumentName, "(null)"::toString);
        }
        return this.argument(argumentName, argument::toString);
    }

    @Override
    public MessageBuilder argument(String argumentName, Argument argument) {
        this.arguments.put(argumentName, argument);
        return this;
    }

    @Override
    public MessageBuilder firstLine(String firstLine) {
        if( this.firstLine.isEmpty() )
            this.firstLine = firstLine;
        else
            this.firstLine = this.firstLine + " " + firstLine;
        return this;
    }

    @Override
    public MessageBuilder line(String line) {
        return addLine(line, true);
    }

    private MessageBuilder addLine(String line, boolean indent) {
        this.lines.add(indent ? indentLine(line) : line);
        return this;
    }

    @Override
    public MessageBuilder lines(String... lines) {
        for (String line : lines) {
            this.line(line);
        }
        return this;
    }

    @Override
    public MessageBuilder newline() {
        return line("");
    }

    @Override
    public MessageBuilder newlines(int numNewLines) {
        for (int i=0; i < numNewLines; i++) {
            newline();
        }
        return this;
    }

    @Override
    public MessageBuilder sub() {
        this.level++;
        return this;
    }

    @Override
    public MessageBuilder end() {
        this.level--;
        return this;
    }

    @Override
    public MessageBuilder indent(String indentedLine) {
        return this.sub().line(indentedLine).end();
    }

    @Override
    public MessageBuilder snippetStart() {
        return lineNotIndented(SNIPPET_START);
    }

    @Override
    public MessageBuilder snippetEnd() {
        return lineNotIndented(SNIPPET_END);
    }

    @Override
    public MessageBuilder lineNotIndented(String line)  {
        return this.addLine(line, false);
    }

    @Override
    public MessageBuilder exampleStart(String line) {
        return this.line(line).newline().exampleStart();
    }

    @Override
    public MessageBuilder exampleStart() {
        return this.snippetStart().sub();
    }

    @Override
    public MessageBuilder exampleEnd() {
        return this.end().snippetEnd();
    }


    @Override
    public String build() {
        if( this.message==null )
            this.message = doBuild();
        return this.message;
    }

    /**
     * Return the built message or before {@link #build()} has been called it returns the internal state of the builder.
     *
     * @return the built message or the internal state.
     */
    @Override
    public String toString() {
        return Objects.toString(this.message, getInternalState());
    }

    private String getInternalState() {
        return "MessageBuilder{" +
            "firstLine='" + firstLine + '\'' +
            ", lines=" + lines +
            ", level=" + level +
            ", arguments=" + arguments +
            '}';
    }


    private String doBuild() {
        final StringBuilder stringBuilder=new StringBuilder(applyArguments(this.firstLine));
        for (final String line : lines) {
            stringBuilder.append("\n").append(applyArguments(line));
        }

        return stringBuilder.toString();
    }

    private String indentLine(String line) {
        if( line.isEmpty() )
            return line;
        return StringUtils.repeat("\t", level) + line;
    }

    private String applyArguments(final String line) {
        return showEscapedArguments(
                    doApplyArgumentsRecursive(
                        hideEscapedArguments(line)
                    )
                );
    }

    private String hideEscapedArguments(String line) {
        return line.replace(ESCAPE + prefix, prefix + TEMP_ESCAPE);
    }

    private String doApplyArgumentsRecursive(String line) {
        String result=line;
        final Set<String> replacements=new HashSet<>();
        do {
            replacements.add(result);
            result=doApplyAllArgumentsOnce(result);
        } while( ! replacements.contains(result) );

        return result;
    }

    private String doApplyAllArgumentsOnce(String line) {
        for (Map.Entry<String, Argument> argument : arguments.entrySet()) {
            final String arg=toArgumentString(argument.getKey());
            if (line.contains(arg)) {
                line=line.replace(arg, argument.getValue().apply());
            }
        }
        return line;
    }

    private String showEscapedArguments(String result) {
        return result.replace(prefix + TEMP_ESCAPE, prefix);
    }

    private String toArgumentString(String argumentName) {
        return prefix + argumentName + suffix;
    }

}
