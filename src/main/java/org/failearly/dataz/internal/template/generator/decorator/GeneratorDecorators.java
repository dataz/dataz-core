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
