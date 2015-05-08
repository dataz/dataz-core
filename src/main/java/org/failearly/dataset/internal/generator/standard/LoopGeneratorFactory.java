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
package org.failearly.dataset.internal.generator.standard;

import org.failearly.dataset.generator.support.Generator;
import org.failearly.dataset.generator.Limit;
import org.failearly.dataset.generator.support.LimitedGeneratorBase;
import org.failearly.dataset.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.generator.LoopGenerator;

/**
 * LoopGeneratorFactory is responsible for creating instances of {@link org.failearly.dataset.generator.LoopGenerator}.
 */
public final class LoopGeneratorFactory extends GeneratorFactoryBase<Integer, LoopGenerator> {
    @Override
    protected String doResolveDataSetName(LoopGenerator annotation) {
        return annotation.dataset();
    }

    @Override
    public Generator<Integer> create(LoopGenerator annotation) {
        return doCreateGenerator(annotation, Limit.LIMITED, (a)->annotation.size());
    }

    @Override
    protected LimitedGeneratorBase<Integer> doCreateLimitedGenerator(LoopGenerator generatorAnnotation, Integer limitValue) {
        return new RangeGeneratorImpl(generatorAnnotation);
    }
}
