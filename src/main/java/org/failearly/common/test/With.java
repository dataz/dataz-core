/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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

package org.failearly.common.test;

import org.failearly.dataset.exception.DataSetException;
import org.failearly.common.test.mb.MessageBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code With} handles exceptions for {@link With.Action} or {@link With.Producer} closures.
 * {@link org.failearly.dataset.exception.DataSetException} won't be handled, they will be rethrown.
 * Example:<br><br>
 * <pre>
 *    With with = With.newInstance((d, ex) {@literal ->} {throw new MyException(d+" failed!", ex);});
 *    java.sql.Connection con=with.producer("Create connection from " + url,(){@literal ->}DriverManager.getConnection(url));
 * </pre>
 */
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
    public void action(String description, MessageBuilders.LazyMessage message, Action action) {
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
    public <T> T producer(String description, MessageBuilders.LazyMessage message, Producer<T> producer) {
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

    private interface Closure<T> {
        T doApply() throws Exception;
    }

    private <T> T doApply(String description, Object message, Closure<T> closure) {
        try {
            LOGGER.debug("Apply '{}'", description);
            return closure.doApply();
        }
        catch(DataSetException ex) {
            LOGGER.debug("({}) Caught dataSet exception while applying '{}'.", name,
                description);
            throw ex;
        }
        catch(Exception ex) {
            LOGGER.warn("({}) Caught exception while applying '{}'. Caught exception: {}", name, description, ex);
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
