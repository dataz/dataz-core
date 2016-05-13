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

package org.failearly.dataz.template.generator;

import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.generator.support.*;

/**
 * Any generator is a template object which provides two definition of iterators:
 * <br><br>
 * <ul>
 * <li>An <b>external iterator</b>. Each generator is also a {@link Iterable}, which could be used in foreach loops. This
 * external iterator is implemented by the {@link #iterator()} and is entirely independent from the internal iterator and other
 * external iterators.</li>
 * <li>An <b>internal iterator</b>. {@link #next()} builds the internal iterator.</li>
 * </ul>
 * <p>
 * There are two definition of generators:<br><br>
 * <ul>
 * <li>{@link UnlimitedGenerator} which support <b>only the internal iterator</b>.</li>
 * <li>{@link LimitedGenerator} which supports both iterators.</li>
 * </ul>
 * <br><br>
 * <b>Remark</b>: Please do not implement this interface, {@link UnlimitedGenerator} or {@link LimitedGenerator}.
 * Extend either {@link LimitedGeneratorBase} or {@link UnlimitedGeneratorBase}.
 *
 * @see UnlimitedGenerator
 * @see LimitedGenerator
 * @see UnlimitedGeneratorBase
 * @see LimitedGeneratorBase
 */
public interface Generator<T> extends TemplateObject, Iterable<T> {

    /**
     * Returns {@code true} if the internal iterator is not yet exhausted.
     * @return {@code true} if there are still any value.
     */
    boolean hasNext();

    /**
     * The internal iterator.
     *
     * @return the next value.
     *
     * @throws InternalIteratorExhaustedException thrown in case of exhausted generator (only on limited generator).
     *
     * @see #getNext()
     */
    T next() throws InternalIteratorExhaustedException;

    /**
     * Alias for {@link #next()}, so it's possible to use the bean notation within the template.
     *
     * @return next value.
     * @see #next()
     */
    T getNext();

    /**
     * Returns the last value of previous called {@link #next()}.
     *
     * @return the last value of {@link #next()} or {@code null}.
     */
    T lastValue();

    /**
     * Alias for {@link #lastValue()}.
     *
     * @return last value.
     */
    T getLastValue();

    /**
     * Reset the generator (or internal iterator), but NOT the iterator created by {@link #iterator()}.
     */
    void reset();

    /**
     * Do not implement {@link Generator}. Extend either {@link UnlimitedGeneratorBase} or
     * {@link LimitedGeneratorBase}.
     */
    @SuppressWarnings("unused")
    void __do_not_implement_Generator__instead_extend_GeneratorBase();
}
