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

import java.util.Optional;
import java.util.function.Function;

/**
 * MessageArguments collects the argument value to the mandatory, optional and derived parameters.
 */
public interface MessageArguments {
    /**
     * Add/set a mandatory argument. If the argument has been set, the last setting will win.
     *
     * Can't be overwritten by {@link #addOptionalArgument(String, Object)} or {@link #addDerivedArgument(String, Function)}.
     *
     * @param name the arguments name
     * @param value  the arguments value.
     * @return itself
     *
     * @see  TemplateParameters
     */
    MessageArguments addMandatoryArgument(String name, Object value);

    /**
     * Add/set an optional argument.
     *
     * Could be overwritten by {@link #addDerivedArgument(String, Function)} or {@link #addMandatoryArgument(String, Object)}.
     *
     * @param name the arguments name
     * @param value  the arguments value.
     * @return itself
     *
     * @see  TemplateParameters
     */
    MessageArguments addOptionalArgument(String name, Object value);

    /**
     * Add an derived (optional) argument.
     *
     * Could be overwritten by {@link #addOptionalArgument(String, Object)} or {@link #addMandatoryArgument(String, Object)}.
     *
     * @param name the arguments name
     * @param func  function which access other value's for calculating the actually argument value.
     * @param <T> target type
     * @return itself
     *
     *
     * @see  TemplateParameters
     */
    <T> MessageArguments addDerivedArgument(String name, Function<Accessor, T> func);

    /**
     * Provides two accessor methods to {@link MessageArguments}.
     */
    interface Accessor {
        /**
         * Get the value of argument with {@code name}, if not set or derivable use {@code defaultValue}.
         * @param name the name of the message argument
         * @param clazz the expected type
         * @param defaultValue the default value
         * @param <R>  the type of the return value
         * @return The value of argument {@code name} or {@code defaultValue}.
         *
         * @see #getValue(String, Class)
         */
        <R> R getValue(String name, Class<R> clazz, R defaultValue);

        /**
         * Get the value of argument with {@code name}, if not set or derivable it will return {@link Optional#empty()}.
         * The type of the argument will be checked and a {@link ClassCastException} will be thrown in case of invalid
         * cast.
         *
         * @param name the name of the message argument
         * @param clazz the expected type
         * @param <R>  the type of the return value
         * @return The optional value of argument {@code name}.
         */
        <R> Optional<R> getValue(String name, Class<R> clazz);
    }
}
