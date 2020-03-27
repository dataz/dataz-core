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

package org.failearly.dataz.internal.template.generator.decorator;

import org.failearly.dataz.template.generator.support.GeneratorBase;
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;
import org.failearly.dataz.template.generator.support.UnlimitedGeneratorBase;

/**
 * GeneratorDecorators provides Decorator functions.
 */
public final class GeneratorDecorators {
    private GeneratorDecorators() {
    }

    /**
     * Makes from a limited decorator an unlimited one.
     *
     * @param limitedGenerator the limited generator.
     * @param <T>              Generator's target type.
     * @return an unlimited generator.
     */
    public static <T> UnlimitedGeneratorBase<T> makeUnlimited(GeneratorBase<T> limitedGenerator) {
        return new UnlimitedGeneratorDecorator<>(limitedGenerator);
    }

    /**
     * Makes from an unlimited decorator an limited one.
     *
     * @param unlimitedGenerator the unlimited generator.
     * @param limitValue the limit value.
     * @param <T>                Generator's target type.
     * @return a limited generator.
     */
    public static <T> LimitedGeneratorBase<T> makeLimited(UnlimitedGeneratorBase<T> unlimitedGenerator, int limitValue) {
        return new LimitedGeneratorDecorator<>(unlimitedGenerator, limitValue);
    }

    /**
     * Makes an unlimited generator a unique one.
     * @param unlimitedGenerator the unlimited generator.
     * @param limitValue the limit value.
     * @param <T>                Generator's target type.
     * @return a limited generator.
     */
    public static <T> GeneratorBase<T> makeUnique(UnlimitedGeneratorBase<T> unlimitedGenerator, int limitValue) {
        return new UniqueGeneratorDecorator<>(unlimitedGenerator, limitValue);
    }
}
