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

package org.failearly.dataset.internal.template.generator;

import org.failearly.dataset.template.generator.Limit;
import org.failearly.dataset.template.generator.LoopGenerator;
import org.failearly.dataset.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.template.generator.support.IntegerRangeGenerator;
import org.failearly.dataset.template.generator.support.LimitedGeneratorBase;
import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObject;

/**
 * LoopGeneratorFactory is responsible for creating instances of {@link org.failearly.dataset.template.generator.LoopGenerator}.
 */
public final class LoopGeneratorFactory extends GeneratorFactoryBase<Integer, LoopGenerator> {
    public LoopGeneratorFactory() {
        super(LoopGenerator.class);
    }

    @Override
    protected String doResolveDataSetName(LoopGenerator annotation) {
        return annotation.dataset();
    }

    @Override
    protected Scope doResolveScope(LoopGenerator annotation) {
        return annotation.scope();
    }

    @Override
    protected TemplateObject doCreate(LoopGenerator annotation) {
        return doCreateGenerator(annotation, Limit.LIMITED);
    }

    @Override
    protected LimitedGeneratorBase<Integer> doCreateLimitedGenerator(LoopGenerator generatorAnnotation, Integer limitValue) {
        return new LoopGeneratorImpl(generatorAnnotation);
    }

    private class LoopGeneratorImpl extends IntegerRangeGenerator {
        public LoopGeneratorImpl(LoopGenerator loopGenerator) {
            super(loopGenerator, loopGenerator.dataset(), loopGenerator.name(), loopGenerator.scope(), 1, loopGenerator.size(), 1);
        }

        @Override
        protected String fromToInvariant() {
            return "size >= 1";
        }
    }
}
