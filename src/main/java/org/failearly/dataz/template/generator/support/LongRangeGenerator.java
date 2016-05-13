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

import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.template.Scope;

import java.lang.annotation.Annotation;

/**
 * LongRangeGenerator provides support for Long based range generators.
 */
public abstract class LongRangeGenerator extends RangeGeneratorBase<Long> {

    protected LongRangeGenerator(Annotation annotation, String dataset, String name, Scope scope, long from, long to, long step) {
        super(annotation, from, to, step);
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
    protected final boolean hasNextValue(Long curr) {
        return curr <= to;

    }

    @Override
    protected final Long nextValue(Long curr) {
        return curr + step;
    }
}
