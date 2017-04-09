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
 * OptEnum is responsible for setting an optional enum class.
 *
 * Sometimes there are meaningful defaults, which should not be overwritten. So the common known keyword default on an
 * impl element does not work.
 */
public @interface OptEnum {
    /**
     * This value will be used if
     * @return the value
     */
    Class<? extends Enum<?>> value() default Null.class;

    /**
     * Use default (application value) or {@link #value()}.
     *
     * @see Use#applyAnnotationOn(OptEnum, Consumer)
     */
    Use use() default Use.DEFAULT;

    /**
     * Use application default value or {@link OptEnum#value()}.
     */
    enum Use implements OptValueConsumer<Class<? extends Enum<?>>> {
        DEFAULT,
        VALUE {
            @Override
            public void apply(Consumer<Class<? extends Enum<?>>> valueConsumer, Class<? extends Enum<?>> value) {
                valueConsumer.accept(value);
            }
        };

        public final void applyAnnotationOn(OptEnum optEnum, Consumer<Class<? extends Enum<?>>> valueApplication) {
            optEnum.use().apply(valueApplication, optEnum.value());
        }
    }

    /**
     * Default enum for {@link OptEnum#value()}.
     */
    enum Null {}

}
