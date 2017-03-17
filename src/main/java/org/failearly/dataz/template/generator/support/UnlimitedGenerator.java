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

import org.failearly.dataz.template.generator.Generator;

import java.util.Iterator;

/**
 * UnlimitedGenerator supports only the internal generator. Please extend {@link UnlimitedGeneratorBase} instead
 * implementing this interface.
 */
public interface UnlimitedGenerator<T> extends Generator<T> {
    /**
     * Any unlimited iterator makes no sense, because {@link Iterator#hasNext()} will return always true and so
     * the loop will never stop.
     *
     * @throws UnsupportedOperationException will be always thrown by any UnlimitedGenerator.
     */
    @Override
    default Iterator<T> iterator() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported for UnlimitedGenerator.");
    }

    /**
     * UnlimitedGenerator supports only the internal iterator.
     *
     * @return never returns {@code null}.
     */
    @Override
    T next();

    @SuppressWarnings("unused")
    void __extend_UnlimitedGeneratorBase__instead_of_implementing_UnlimitedGenerator();
}
