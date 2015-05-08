/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.generator.support;


import java.util.Iterator;
import java.util.Objects;

/**
 * GeneratorBase is the base class for {@link Generator} implementations.
 */
public abstract class GeneratorBase<T> implements Generator<T> {

    private final String dataset;
    private final String name;
    Iterator<T> iterator;


    protected GeneratorBase(String name, String dataset) {
        this.name = name;
        this.dataset = dataset;

        assert ! Objects.toString(name,"").isEmpty() : "name is empty or null";
        assert ! Objects.toString(dataset,"").isEmpty() : "dataset is empty or null";
    }

    /**
     * Initialize current generator by creating the internal iterator (for {@link #next()}.
     *
     * @return itself
     */
    public final GeneratorBase<T> init() {
        this.iterator = createIterator();
        return this;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final String dataset() {
        return dataset;
    }

    @Override
    public Iterator<T> iterator() {
        return createIterator();
    }

    @Override
    public T next() {
        if( iterator.hasNext() )
            return iterator.next();
        return null;
    }

    /**
     * Internal use only!
     * @return creates a new iterator.
     */
    public abstract Iterator<T> createIterator();
}