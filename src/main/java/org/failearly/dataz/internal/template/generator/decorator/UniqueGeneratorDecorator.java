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

import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;
import org.failearly.dataz.template.generator.support.UnlimitedGeneratorBase;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Decorates an unlimited generator with unique (limited) behaviour.
 */
public final class UniqueGeneratorDecorator<T> extends LimitedGeneratorBase<T> {

    private final UnlimitedGeneratorBase<T> generator;
    private final int limit;

    UniqueGeneratorDecorator(UnlimitedGeneratorBase<T> generator, int limit) {
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
        return new Iterator<T>() {
            private int counter = 0;
            private final Iterator<T> iterator=generator.createIterator();
            private final Set<T> uniqueValues=new HashSet<>();

            @Override
            public boolean hasNext() {
                return counter < limit;
            }

            @Override
            public T next() {
                try {
                    if ( hasNext() ) {
                        return nextUniqueValue();
                    }
                    return null;
                }
                finally {
                    counter++;
                }
            }

            private T nextUniqueValue() {
                boolean unique=false;
                T nextValue=null;
                while(iterator.hasNext() && ! unique ) {
                    nextValue=iterator.next();
                    unique=uniqueValues.add(nextValue);
                }

                return nextValue;
            }
        };
    }

    @Override
    public String toString() {
        return generator.toString();
    }

}
