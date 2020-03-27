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

import java.lang.annotation.*;

/**
 * MessageParameters validates the message arguments.
 */
public interface MessageParameters {
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Definition {
        Class<? extends MessageParameters> value();
    }

    /**
     * Factory method for creating a message parameters.
     * @param annotation the impl
     * @return a new message parameter validator instance.
     */
    MessageParameters create(Annotation annotation);

    /**
     * The validation function.
     * <br><br>
     * If {@code messageArgumentsAreValid(...)} returns {@code true}, the {@code errorMessageCollector} will not be
     * called.<br>
     * If {@code messageArgumentsAreValid(...)} returns {@code false}, the {@code errorMessageCollector} will be called
     * for every wrong parameter.
     *
     * @param messageArguments the message arguments.
     * @param errorMessageCollector collects the error messages.
     * @return true if valid otherwise false.
     */
    boolean messageArgumentsAreValid(MessageArgumentsImpl messageArguments, ErrorMessageCollector errorMessageCollector);
}
