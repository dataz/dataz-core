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

package org.failearly.dataz.template.generator.support;


import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.TemplateObjectBase;
import org.failearly.dataz.template.generator.Generator;

import java.lang.annotation.Annotation;
import java.util.Iterator;

/**
 * GeneratorBase should be the base class for any {@link Generator} implementations. Please extend {@link
 * UnlimitedGeneratorBase} or {@link LimitedGeneratorBase} instead extending this base class.
 */
public abstract class GeneratorBase<T> extends TemplateObjectBase implements Generator<T> {

    private Iterator<T> internalIterator;
    private T lastValue;


    /**
     * Decorate the other template object (here a generator)
     * @param other the template object to be decorated
     */
    protected GeneratorBase(TemplateObject other) {
        super(other);
    }

    /**
     * The standard constructor.
     * @param annotation your impl
     * @param context the context object
     */
    GeneratorBase(Annotation annotation, TemplateObjectAnnotationContext context) {
        super(annotation, context);
    }

    /**
     * Initialize current generator by creating the internal iterator (for {@link #next()}.
     */
    public final void init() throws DataSetException {
        super.init();
        this.internalIterator = createIterator();
    }

    final void resetInternalIterator() {
        this.lastValue = null;
        this.internalIterator = createIterator();
    }

    final Iterator<T> internalIterator() {
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
    public final T next() {
        return doNext();
    }

    protected T doNext() {
        if (internalIterator.hasNext())
            lastValue = internalIterator.next();
        else
            throw new InternalIteratorExhaustedException(this.name());

        LOGGER.debug("Generator '{}' in datasets '{}': Generated value is '{}'", name(), datasets(), lastValue);

        return lastValue;
    }

    @Override
    public final T getNext() {
        return next();
    }

    public final T lastValue() {
        return doLastValue();
    }

    protected T doLastValue() {
        if( lastValue==null ) {
            throw new IllegalStateException("Please first call next() on '" + this.name() + "' before accessing lastValue()!");
        }
        return lastValue;
    }

    @Override
    public final T getLastValue() {
        return lastValue();
    }

    @Override
    public final void reset() {
        doReset();
    }

    protected void doReset() {
        resetInternalIterator();
    }

    final T setLastValue(T value) {
        this.lastValue = value;
        LOGGER.debug("Generator '{}' in dataz '{}': Last generated value is '{}'", name(), datasets(), lastValue);
        return lastValue;
    }

    /**
     * Internal use only!
     *
     * @return creates a new iterator.
     */
    public abstract Iterator<T> createIterator();

}
