/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.util;

import org.failearly.dataset.exception.DataSetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code With} handles exceptions for {@link org.failearly.dataset.util.With.Action} or {@link org.failearly.dataset.util.With.Producer} closures.
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
     * @param description any description of the action.
     * @param action the action closure.
     */
    public void action(String description, Action action) {
        try {
            LOGGER.debug("Apply action: '{}'", description);
            action.apply();
        } catch(DataSetException ex) {
            LOGGER.debug("({}) Caught exception while applying action '{}'. Exception: {}", name, description, ex);
            throw ex;
        }
        catch (Exception ex) {
            LOGGER.warn("({}) Caught exception while applying action '{}'. Exception: {}", name, description, ex);
            exceptionHandler.handleException(description, ex);
        }
    }

    /**
     * Do apply the action and handle potential exception.
     * @param description any description of the action.
     * @param producer the producer closure.
     * @param <T> the produced type
     * @return the value of the producer or {@code null}.
     */
    public <T> T producer(String description, Producer<T> producer) {
        try {
            LOGGER.debug("Apply producer: '{}'", description);
            return producer.apply();
        } catch(DataSetException ex) {
            LOGGER.debug("({}) Caught exception while producing/creating '{}'. Exception: {}", name, description, ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.warn("({}) Caught exception while producing/creating '{}'. Exception: {}", name, description, ex);
            exceptionHandler.handleException(description, ex);
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
