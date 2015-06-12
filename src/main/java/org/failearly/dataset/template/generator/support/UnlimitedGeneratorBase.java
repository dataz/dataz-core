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

package org.failearly.dataset.template.generator.support;

import org.failearly.dataset.template.generator.GeneratorConstants;
import org.failearly.dataset.template.Scope;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Random;

/**
 * UnlimitedGeneratorBase - the base implementation for unlimited generators.
 */
public abstract class UnlimitedGeneratorBase<T> extends GeneratorBase<T> implements UnlimitedGenerator<T> {
    protected UnlimitedGeneratorBase(String dataset, String name, Scope scope) {
        super(dataset, name, scope);
    }

    protected UnlimitedGeneratorBase(Annotation annotation, String dataset, String name, Scope scope) {
        super(annotation, dataset, name, scope);
    }

    /**
     * Creates a random value generator with or without seed.
     *
     * @param seed seed value or {@link GeneratorConstants#NO_SEED}.
     *
     * @return the random instance.
     */
    protected static Random random(int seed) {
        if( seed == GeneratorConstants.NO_SEED ) {
            return new Random();
        }

        return new Random(seed);
    }


    @Override
    public final T next() {
        if( ! iterator.hasNext() ) {
            iterator = this.createIterator();
        }

        return iterator.next();
    }

    @Override
    public final Iterator<T> iterator() {
        throw new UnsupportedOperationException("Don't use iterator() for unlimited generators! Use next() instead.");
    }

}
