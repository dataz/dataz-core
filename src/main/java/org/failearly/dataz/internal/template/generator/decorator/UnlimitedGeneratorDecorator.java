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
