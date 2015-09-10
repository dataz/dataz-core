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
 * MessageBuilder is a builder for messages. MessageBuilder instances could be created by {@link MessageBuilders}.
 * <br><br>
 * If you like to extend the MessageBuilder use/extend {@link MessageBuilderDelegate}.
 *
 * <br><br>
 * Usage example:<br><br>
 * <pre>
 *    final MessageBuilder messageBuilder=MessageBuilders.create("Missing test method!")
 *                          .argument("baseclass","MessageBuilderTest")
 *                          .newline()
 *                          .line("Example:")
 *                          .newline()
 *                          .sub()
 *                              .line("public class MyTest extends __baseclass__ {")
 *                              .sub()
 *                                  .line("@Test")
 *                                  .line("public void test() {")
 *                                      .indent("// your test comes here")
 *                                  .line("}")
 *                              .end()
 *                              .line("}")
 *                          .end();
 *
 *    final String message=messageBuilder.build();
 *    // The content of message:
 *    //  Missing test method!
 *    //
 *    //  Example:
 *    //
 *    //      public class MyTest extends MessageBuilderTest {
 *    //          @Test
 *    //          public void test() {
 *    //              // your test comes here
 *    //          }
 *    //      }
 *    //
 * </pre>
 */
public interface MessageBuilder {
    /**
     * The escape symbol to be used for disable the replacement argument strings.
     */
    String ESCAPE="\\";

    /**
     * The default prefix could be changed by calling {@link #prefix(String)}.
     */
    String DEFAULT_PREFIX="__";

    /**
     * The default suffix could be changed by calling {@link #suffix(String)}.
     */
    String DEFAULT_SUFFIX="__";


    /**
     * The default (start) snippet.
     *
     * @see #snippetStart()
     * @see #lineNotIndented(String)
     */
    String SNIPPET_START="8><----------";


    /**
     * The default (end) snippet.
     *
     * @see #snippetEnd()
     * @see #lineNotIndented(String)
     */
    String SNIPPET_END="----------><8";

    /**
     * Convenient function for setting the prefix and suffix to the same value.
     * See {@link #prefix(String)} and {@link #suffix(String)}.
     *
     * @param prefixSuffix the prefixSuffix value
     * @return the builder
     *
     * @see #prefix(String)
     * @see #suffix(String)
     * @see #argument(String, Argument)
     * @see #argument(String, Object)
     */
    MessageBuilder prefixSuffix(String prefixSuffix);

    /**
     * Set the (argument) suffix to other value then {@value #DEFAULT_SUFFIX}. The suffix will be used within {@link #build()}, so
     * it's global and should be used only once.
     * @param suffix new suffix.
     * @return the builder
     * @see #argument(String, Argument)
     * @see #argument(String, Object)
     */
    MessageBuilder suffix(String suffix);

    /**
     * Set the (argument) prefix to other value then {@value #DEFAULT_PREFIX}. The prefix will be used within {@link #build()}, so
     * it's global and should be used only once.
     * @param prefix new prefix.
     * @return the builder
     * @see #argument(String, Argument)
     * @see #argument(String, Object)
     */
    MessageBuilder prefix(String prefix);

    /**
     * Add an argument named {@code argumentName} with a <i>static</i> value. The argument could be used in {@link #line(String)}
     * or in the first line and will be applied by {@link #build()}. The argument
     * are global, so it doesn't care when you add an argument.
     * <br><br>
     * <i>Remark</i>: If you want to suppress the replacing of a argument you could escape it with backslash (\).
     * So "\\__class__" will result in "__class__".
     * <br><br>
     * Example:<br><br>
     * <pre>
     *    String message=mb.create("public class __class__ {}")
     *      .argument("class","MyTestClass")
     *      .build();
     *    // --&gt; message == "public class MyTestClass {}";
     * </pre>
     *
     * @param argumentName the argument name
     * @param argument the static argument
     * @return the builder
     */
    MessageBuilder argument(String argumentName, Object argument);

    /**
     * Add an argument named {@code argumentName} with a <i>lazy/dynamic</i> argument. The argument could be used in {@link #line(String)}
     * or in the first line and will be applied by {@link #build()}. The argument
     * are global, so it doesn't care when you add an argument. The argument function will be only evaluated, if
     * you the {@code argumentName} could be found in any message.
     * <br><br>
     * <i>Remark</i>: If you want to suppress the replacing of a argument you could escape it with backslash (\).
     * So "\\__class__" will result in "__class__".
     * <br><br>
     * Example:<br><br>
     * <pre>
     *    String message=mb.create("public class __class__ { }")
     *      .argument("class",()-&gt;MyTestClass.class.getSimpleName())
     *      .build();
     *    // --&gt; message == "public class MyTestClass {}";
     * </pre>
     *
     * @param argumentName the argument name
     * @param argument the lazy/dynamic argument
     * @return the builder
     */
    MessageBuilder argument(String argumentName, Argument argument);

    /**
     * Set or add a String to firstLine. If there is already a not empty firstLine a blank (" ") will be prepend
     * to the new first line.
     * @param firstLine the first line
     * @return the builder
     */
    MessageBuilder firstLine(String firstLine);

    /**
     * Add a new line to the message. Could contain arguments.
     * @param line a line
     * @return the builder
     *
     * @see #argument(String, Argument)
     * @see #argument(String, Object)
     */
    MessageBuilder line(String line);

    /**
     * Add multiple lines to the message. Could contain arguments.
     * @param lines a line
     * @return the builder
     *
     * @see #argument(String, Argument)
     * @see #argument(String, Object)
     */
    MessageBuilder lines(String... lines);

    /**
     * Add a single newline ({@code \n}) to the message.
     * @return the builder
     */
    MessageBuilder newline();

    /**
     * Add multiple newlines {@code \n} to the message.
     * @param numNewLines number of new lines
     * @return the builder
     */
    MessageBuilder newlines(int numNewLines);

    /**
     * Create a sub message, which means that the lines created after sub, will be indented. The sub message could be
     * terminated by calling {@link #end()}.
     *
     * @return the builder
     *
     * @see #end()
     */
    MessageBuilder sub();

    /**
     * Terminates the {@code sub} message.
     *
     * @return the builder
     *
     * @see #sub()
     */
    MessageBuilder end();

    /**
     * Indent a single line. Convenient method for {@code mb.sub().line("my line").end()}.
     * @param indentedLine the line text
     * @return the builder
     */
    MessageBuilder indent(String indentedLine);

    /**
     * Insert start snippet symbol {@link #SNIPPET_START} on new line.
     * @return the builder
     */
    MessageBuilder snippetStart();

    /**
     * Insert end snippet symbol {@link #SNIPPET_END}.
     * @return the builder
     */
    MessageBuilder snippetEnd();

    /**
     * Add an not indented line. So it's like {@link #line(String)} but without indention.
     * @param line the line
     * @return the builder
     */
    MessageBuilder lineNotIndented(String line);

    /**
     * Start example block (incl. {@link #snippetStart()}).
     * @param line the line
     * @return the builder
     */
    MessageBuilder exampleStart(String line);

    /**
     * Start example block without text (incl. {@link #snippetStart()}).
     * @return the builder
     */
    MessageBuilder exampleStart();

    /**
     * End example block (incl. {@link #snippetEnd()}).
     * @return the builder
     */
    MessageBuilder exampleEnd();

    /**
     * Build the message.
     *
     * @return the (built) message
     */
    String build();

    @FunctionalInterface
    interface Argument {
        String apply();
    }
}
