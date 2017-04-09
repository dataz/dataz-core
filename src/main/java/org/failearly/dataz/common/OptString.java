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
package org.failearly.dataz.common;

import java.util.function.Consumer;

/**
 * OptString is responsible for setting an optional (string) value.
 *
 * Sometimes there are meaningful defaults, which should not be overwritten. So the common known keyword default on an
 * impl element does not work.
 */
public @interface OptString {
    /**
     * This value will be used if
     * @return the value
     */
    String value() default "";

    /**
     * Use default (application value) or {@link #value()}.
     *
     * @see Use#applyAnnotationOn(OptString, Consumer)
     */
    Use use() default Use.DEFAULT;

    /**
     * Use application default value or {@link OptString#value()}.
     */
    enum Use implements OptValueConsumer<String> {
        DEFAULT,
        VALUE {
            @Override
            public void apply(Consumer<String> valueConsumer, String value) {
                valueConsumer.accept(value);
            }
        };

        public final void applyAnnotationOn(OptString optString, Consumer<String> valueApplication) {
            optString.use().apply(valueApplication, optString.value());
        }
    }

}
