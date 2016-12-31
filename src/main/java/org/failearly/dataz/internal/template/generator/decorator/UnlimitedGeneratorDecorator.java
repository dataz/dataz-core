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
import org.failearly.dataz.template.generator.support.InternalIteratorExhaustedException;
import org.failearly.dataz.template.generator.support.GeneratorBase;
import org.failearly.dataz.template.generator.support.UnlimitedGeneratorBase;

import java.util.Iterator;

/**
 * Decorates a limited generator with unlimited behaviour.
 */
public final class UnlimitedGeneratorDecorator<T> extends UnlimitedGeneratorBase<T> {

    private final GeneratorBase<T> generator;

    UnlimitedGeneratorDecorator(GeneratorBase<T> generator) {
        super(generator);
        this.generator = generator;
    }

    @Override
    public Iterator<T> createIterator() {
        return generator.createIterator();
    }


    @Override
    public String toString() {
        return generator.toString();
    }

    @Override
    protected void doInit() throws DataSetException {
        super.doInit();
        generator.init();
    }

    @Override
    protected T doNext() {
        try {
            return generator.next();
        } catch (InternalIteratorExhaustedException e) {
           reset();
        }

        return generator.next();
    }

    @Override
    protected T doLastValue() {
        return generator.lastValue();
    }

    @Override
    protected void doReset() {
        generator.reset();
    }
}
