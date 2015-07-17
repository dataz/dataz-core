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
import org.failearly.dataset.template.common.Scope;

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
     * Creates a random value generator.
     *
     * @param seed seed value or {@link GeneratorConstants#DEFAULT_SEED}.
     *
     * @return the random instance.
     */
    protected static Random random(int seed) {
        return new Random(seed);
    }

    @Override
    public final T next() {
        if( ! internalIterator().hasNext() ) {
            resetInternalIterator();
        }

        return setLastValue(internalIterator().next());
    }

    @Override
    public final void reset() {
        doReset();
    }


    /**
     * Do the actually reset.
     */
    protected abstract void doReset();


    @Override
    public final Iterator<T> iterator() {
        throw new UnsupportedOperationException("Don't use iterator() for unlimited generators! Use next() instead.");
    }

    @Override
    public final void __extend_UnlimitedGeneratorBase__instead_of_implementing_UnlimitedGenerator() {
        throw new UnsupportedOperationException("__extend_UnlimitedGeneratorBase__instead_of_implementing_UnlimitedGenerator must not be called");
    }

    @Override
    public final void __do_not_implement_Generator() {
        throw new UnsupportedOperationException("__do_not_implement_Generator must not be called");

    }
}
