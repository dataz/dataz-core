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
        super(generator);
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
