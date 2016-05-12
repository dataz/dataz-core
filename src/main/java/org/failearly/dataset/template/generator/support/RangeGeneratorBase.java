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

package org.failearly.dataset.template.generator.support;

import java.lang.annotation.Annotation;
import java.util.Iterator;

/**
 * RangeGeneratorBase is the base class for all sequential range generators.
 */
public abstract class RangeGeneratorBase<T extends Number> extends LimitedGeneratorBase<T> {

    protected final T from;
    protected final T to;
    protected final T step;

    protected RangeGeneratorBase(Annotation annotation, T from, T to, T step) {
        super(annotation);

        this.from=from;
        this.to=to;
        this.step=step;

    }

    /**
     * Check if there is the iterator has next values.
     *
     * @param curr current value of the iterator
     * @return {@code true} if there are still values to generate.
     */
    protected abstract boolean hasNextValue(T curr);

    /**
     * Create next value of {@code curr}.
     *
     * @param curr the current value
     * @return next value
     */
    protected abstract T nextValue(T curr);

    @Override
    public Iterator<T> createIterator() {
        return new Iterator<T>() {
            private T curr =from;

            @Override
            public boolean hasNext() {
                return hasNextValue(curr);
            }

            @Override
            public T next() {
                final T currentValue = curr;
                curr = nextValue(curr);
                return currentValue;
            }
        };
    }


}
