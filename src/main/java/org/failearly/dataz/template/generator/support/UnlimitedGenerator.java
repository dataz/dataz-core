/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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
