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
import org.failearly.dataset.template.Scope;

import java.lang.annotation.Annotation;

/**
 * IntegerRangeGenerator is the base class for Integer based range generators.
 */
public abstract class IntegerRangeGenerator extends RangeGeneratorBase<Integer> {
    protected IntegerRangeGenerator(Annotation annotation, String dataset, String name, Scope scope, int from, int to, int step) {
        super(annotation, dataset, name, scope, from, to, step);
    }

    @Override
    protected final void doInit() throws DataSetException {
        super.doInit();
        checkInvariant(from <= to, fromToInvariant());
        checkInvariant(step > 0, stepInvariant());
    }

    protected String stepInvariant() {
        return "step > 0";
    }

    protected String fromToInvariant() {
        return "from <= to";
    }

    @Override
    protected final boolean hasNextValue(Integer curr) {
        return curr <= to;

    }

    @Override
    protected final Integer nextValue(Integer curr) {
        return curr + step;
    }
}
