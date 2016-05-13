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

import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;
import org.failearly.dataz.template.generator.support.UnlimitedGeneratorBase;

import java.util.Iterator;

/**
 * Decorates an unlimited generator with limited behaviour.
 */
public final class LimitedGeneratorDecorator<T> extends LimitedGeneratorBase<T> {

    private final UnlimitedGeneratorBase<T> generator;
    private final int limit;

    LimitedGeneratorDecorator(UnlimitedGeneratorBase<T> generator, int limit) {
        super(generator.dataset(), generator.name(), generator.scope());

        this.generator = generator;
        this.limit = limit;
    }

    @Override
    protected void doInit() throws DataSetException {
        super.doInit();
        generator.init();
    }

    @Override
    public Iterator<T> createIterator() {
        return new LimitedIterator();
    }

    @Override
    public String toString() {
        return generator.toString();
    }

    private class LimitedIterator implements Iterator<T> {
        private int counter = 0;
        private final Iterator<T> iterator=generator.createIterator();

        @Override
        public boolean hasNext() {
            return counter < limit;
        }

        @Override
        public T next() {
            try {
                if (hasNext() && iterator.hasNext()) {
                    return iterator.next();
                }
                return null;
            }
            finally {
                counter++;
            }
        }
    }
}
