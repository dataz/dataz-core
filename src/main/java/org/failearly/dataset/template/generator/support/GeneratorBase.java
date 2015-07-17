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


import org.failearly.dataset.exception.DataSetException;
import org.failearly.dataset.template.common.Scope;
import org.failearly.dataset.template.common.TemplateObjectBase;
import org.failearly.dataset.template.generator.Generator;
import org.failearly.dataset.template.generator.InternalIteratorExhaustedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Iterator;

/**
 * GeneratorBase is the base class for {@link Generator} implementations. Please extend {@link UnlimitedGeneratorBase} or
 * {@link LimitedGeneratorBase} instead exting this base class.
 */
public abstract class GeneratorBase<T> extends TemplateObjectBase implements Generator<T> {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Iterator<T> internalIterator;
    private T lastValue;


    protected GeneratorBase(String dataset, String name, Scope scope) {
        super(dataset, name, scope);
    }

    protected GeneratorBase(Annotation annotation, String dataset, String name, Scope scope) {
        super(annotation, dataset, name, scope);
    }

    /**
     * Initialize current generator by creating the internal iterator (for {@link #next()}.
     *
     * @return itself
     */
    public final GeneratorBase<T> init() throws DataSetException {
        resetInternalIterator();
        doInit();
        return this;
    }

    protected void doInit() throws DataSetException {
        // DO NOTHING
    }

    protected final void resetInternalIterator() {
        this.lastValue = null;
        this.internalIterator = createIterator();
    }

    protected final Iterator<T> internalIterator() {
        return internalIterator;
    }

    @Override
    public Iterator<T> iterator() {
        return createIterator();
    }


    @Override
    public final boolean hasNext() {
        return internalIterator.hasNext();
    }

    @Override
    public T next() {
        if (internalIterator.hasNext())
            lastValue = internalIterator.next();
        else
            throw new InternalIteratorExhaustedException(this.name());

        LOGGER.debug("Generator '{}' in dataset '{}': Next generated value is '{}'", name(), dataset(), lastValue);
        return lastValue;
    }

    @Override
    public final T getNext() {
        return next();
    }

    public T lastValue() {
        if( lastValue==null ) {
            throw new IllegalStateException("Please first call next() on " + this.name() + " before accessing the lastValue!");
        }
        return lastValue;
    }

    @Override
    public final T getLastValue() {
        return lastValue();
    }

    @Override
    public void reset() {
        resetInternalIterator();
    }

    protected final T setLastValue(T value) {
        this.lastValue = value;
        LOGGER.debug("Generator '{}' in dataset '{}': Last generated value is '{}'", name(), dataset(), lastValue);
        return lastValue;
    }

    /**
     * Internal use only!
     *
     * @return creates a new iterator.
     */
    public abstract Iterator<T> createIterator();

}
