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

package org.failearly.dataz.internal.util;

import org.failearly.dataz.internal.common.message.Message;
import org.failearly.dataz.exception.DataSetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code With} handles exceptions for {@link With.Action} or {@link With.Producer} closures.
 * {@link org.failearly.dataz.exception.DataSetException} won't be handled, they will be rethrown.
 * Example:<br><br>
 * <pre>
 *    With with = With.newInstance((d, ex) {@literal ->} {throw new MyException(d+" failed!", ex);});
 *    java.sql.Connection con=with.producer("Create connection from " + url,(){@literal ->}DriverManager.getConnection(url));
 * </pre>
 */
@SuppressWarnings("unused")
public final class With {

    private static final Logger LOGGER = LoggerFactory.getLogger(With.class);
    private static final With IGNORE_INSTANCE = create((description, exception) -> {/* do nothing */ }, "ignore");
    private final ExceptionHandler exceptionHandler;
    private final String name;

    private With(ExceptionHandler exceptionHandler, String name) {
        this.exceptionHandler = exceptionHandler;
        this.name = name;
    }

    /**
     * Create an instance of the exception handler.
     * @param exceptionHandler the exception handler.
     * @param name used for logging.
     * @return an instance of With.
     */
    public static With create(ExceptionHandler exceptionHandler, String name) {
        return new With(exceptionHandler, name);
    }

    /**
     * Get the ignore instance.
     * @return the ignore instance.
     */
    public static With ignore() {
        return IGNORE_INSTANCE;
    }

    /**
     * Do apply the action and handle potential exception.
     *
     * @param description The description.
     * @param message any (lazy) error message of the action.
     * @param action the action closure.
     */
    public void action(String description, Message message, Action action) {
        doApply(description, message, ()->{action.apply();return "<not-used>";});
    }

    /**
     * Do apply the action and handle potential exception.
     * @param description any description of the action.
     * @param action the action closure.
     */
    public void action(String description, Action action) {
        doApply(description, description, ()->{action.apply();return "<not-used>";});
    }

    /**
     * Do apply the action and handle potential exception.
     * @param description any description of the action.
     * @param message any (lazy) error message of the producer.
     * @param producer the producer closure.
     * @param <T> the produced type
     * @return the value of the producer or {@code null}.
     */
    public <T> T producer(String description, Message message, Producer<T> producer) {
        return doApply(description, message, producer::apply);
    }

    /**
     * Do apply the action and handle potential exception.
     * @param description any description of the action.
     * @param producer the producer closure.
     * @param <T> the produced type
     * @return the value of the producer or {@code null}.
     */
    public <T> T producer(String description, Producer<T> producer) {
        return doApply(description, description, producer::apply);
    }

    @FunctionalInterface
    private interface Closure<T> {
        T doApply() throws Exception;
    }

    private <T> T doApply(String description, Object message, Closure<T> closure) {
        try {
            LOGGER.debug("Apply '{}'", description);
            return closure.doApply();
        }
        catch(DataSetException ex) {
            LOGGER.debug("({}) Caught DataSetException while applying '{}'.", name,
                description);
            throw ex;
        }
        catch(Exception ex) {
            LOGGER.warn("({}) Caught exception while applying '{}'. Origin message: {}", name, description, ex.getMessage());
            exceptionHandler.handleException(message.toString(), ex);
        }
        return null;
    }

    public interface ExceptionHandler {
        void handleException(String description, Exception exception);
    }

    public interface Producer<T> {
        T apply() throws Exception;
    }

    public interface Action {
        void apply() throws Exception;
    }
}
