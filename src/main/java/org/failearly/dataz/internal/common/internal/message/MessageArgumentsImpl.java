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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * Implementation of {@link MessageArguments} and {@link Accessor}.
 */
public final class MessageArgumentsImpl implements MessageArguments {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageArgumentsImpl.class);

    private final Map<String, Object> mandatoryArguments = new HashMap<>();
    private final Map<String, Object> optionalArguments = new HashMap<>();
    private final Map<String, AccessorImpl> derivedArguments = new HashMap<>();


    @Override
    public MessageArguments addMandatoryArgument(String name, Object value) {
        mandatoryArguments.put(name, value);
        return this;
    }

    @Override
    public MessageArguments addOptionalArgument(String name, Object value) {
        optionalArguments.put(name, value);
        return this;
    }


    @Override
    public <T> MessageArguments addDerivedArgument(String name, Function<Accessor, T> func) {
        derivedArguments.put(name, new AccessorImpl<>(func));
        return this;
    }

    public boolean hasMandatoryParameter(String name) {
        return mandatoryArguments.containsKey(name);
    }

    final Map<String, Object> asMap() {
        final HashMap<String, Object> result = new HashMap<>();
        final Set<String> parameters = allParameterNames();
        for (String parameter : parameters) {
            cache.clear();
            result.put(parameter, resolveArgumentValue(parameter));
        }

        return Collections.unmodifiableMap(result);
    }

    private Object resolveArgumentValue(String parameter) {
        return Optional.ofNullable(mandatoryArguments.get(parameter))
            .orElseGet(() -> resolveOptionalOrDerivedValue(parameter));
    }

    private Object resolveOptionalOrDerivedValue(String parameter) {
        return Optional.ofNullable(optionalArguments.get(parameter))
            .orElseGet(() -> derivedArguments.get(parameter).apply());
    }

    private Set<String> allParameterNames() {
        final Set<String> parameters = new HashSet<>();
        parameters.addAll(mandatoryArguments.keySet());
        parameters.addAll(optionalArguments.keySet());
        parameters.addAll(derivedArguments.keySet());
        return parameters;
    }

    private final Map<String, Object> cache = new HashMap<>();
    private static final Object CANT_EVALUATE_VALUE=null;

    private boolean isWithinCache(String name) {
        if (!cache.containsKey(name)) {
            // WARNING: The cache must know the name, otherwise recursive detection wont work.
            cache.put(name, CANT_EVALUATE_VALUE);
            return false;
        }
        return true;
    }

    private <R> Optional<R> addToCache(String name, Class<R> clazz, Object value) {
        if (value == null || clazz.isInstance(value)) {
            cache.put(name, value);
            return Optional.ofNullable(clazz.cast(value));
        }

        throw new ClassCastException(
            "Value of parameter '" + name + "' is not of target class " + clazz.getName()
                + ". It's of type " + value.getClass() + "!");

    }

    private <R> Optional<R> getValueFromCache(String name, Class<R> clazz) {
        LOGGER.debug("Value with name='{}' already accessed. Ignored.", name);
        final Object value = cache.get(name);
        if (value == null || clazz.isInstance(value)) {
            return Optional.ofNullable(clazz.cast(value));
        }

        return Optional.empty();
    }

    /**
     * Provides two accessor methods to the message mandatoryArguments. Chaining is permitted.
     *
     * @param <T> the target type
     */
    private final class AccessorImpl<T> implements Accessor {
        private final Function<Accessor, T> func;

        private AccessorImpl(Function<Accessor, T> func) {
            this.func = func;
        }

        private T apply() {
            return func.apply(this);
        }

        @Override
        public <R> R getValue(String name, Class<R> clazz, R defaultValue) {
            return getValue(name, clazz)  //
                .orElse(defaultValue);
        }

        @Override
        public <R> Optional<R> getValue(String name, Class<R> clazz) {
            if (!isWithinCache(name)) {
                return addToCache(name, clazz, resolveArgumentValue(name));
            }

            return getValueFromCache(name, clazz);
        }
    }

}
