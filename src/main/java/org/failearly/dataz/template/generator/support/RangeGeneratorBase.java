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

package org.failearly.dataz.template.generator.support;

import org.failearly.dataz.template.TemplateObjectAnnotationContext;

import java.lang.annotation.Annotation;
import java.util.Iterator;

/**
 * RangeGeneratorBase is the base class for all sequential range generators.
 */
public abstract class RangeGeneratorBase<T extends Number> extends LimitedGeneratorBase<T> {

    protected final T from;
    protected final T to;
    protected final T step;

    protected RangeGeneratorBase(Annotation annotation, TemplateObjectAnnotationContext context, T from, T to, T step) {
        super(annotation, context);

        this.from=from;
        this.to=to;
        this.step=step;

    }

    /**
     * Check if there is the iterator has next values.
     *
     * @param curr current value of the iterator
     * @return {@code true} if there are still values to generate.
     */
    protected abstract boolean hasNextValue(T curr);

    /**
     * Create next value of {@code curr}.
     *
     * @param curr the current value
     * @return next value
     */
    protected abstract T nextValue(T curr);

    @Override
    public Iterator<T> createIterator() {
        return new Iterator<T>() {
            private T curr =from;

            @Override
            public boolean hasNext() {
                return hasNextValue(curr);
            }

            @Override
            public T next() {
                final T currentValue = curr;
                curr = nextValue(curr);
                return currentValue;
            }
        };
    }


}
