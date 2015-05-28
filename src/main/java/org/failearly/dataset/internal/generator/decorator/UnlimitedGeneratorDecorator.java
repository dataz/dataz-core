/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.internal.generator.decorator;

import org.failearly.dataset.generator.support.GeneratorBase;
import org.failearly.dataset.generator.support.UnlimitedGeneratorBase;

import java.util.Iterator;

/**
 * Decorates a limited generator with unlimited behaviour.
 */
final class UnlimitedGeneratorDecorator<T> extends UnlimitedGeneratorBase<T> {

    private final GeneratorBase<T> generator;

    UnlimitedGeneratorDecorator(GeneratorBase<T> generator) {
        super(generator.dataset(), generator.name());
        this.generator = generator;
    }

    @Override
    public Iterator<T> createIterator() {
        return generator.createIterator();
    }


    @Override
    public String toString() {
        return "UnlimitedGeneratorDecorator(" +
                    "generator=" + generator +
                ')';
    }
}
