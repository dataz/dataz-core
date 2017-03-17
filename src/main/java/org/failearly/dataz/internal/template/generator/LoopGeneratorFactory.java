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

package org.failearly.dataz.internal.template.generator;

import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.generator.Limit;
import org.failearly.dataz.template.generator.LoopGenerator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.generator.support.IntegerRangeGenerator;
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;

/**
 * LoopGeneratorFactory is responsible for creating instances of {@link org.failearly.dataz.template.generator.LoopGenerator}.
 */
public final class LoopGeneratorFactory extends GeneratorFactoryBase<Integer, LoopGenerator> {
    public LoopGeneratorFactory() {
        super(LoopGenerator.class);
    }

    @Override
    protected String doResolveName(LoopGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(LoopGenerator annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(LoopGenerator annotation) {
        return annotation.scope();
    }

    @Override
    protected TemplateObject doCreate(LoopGenerator annotation, TemplateObjectAnnotationContext context) {
        return doCreateGenerator(annotation, context, Limit.LIMITED);
    }

    @Override
    protected LimitedGeneratorBase<Integer> doCreateLimitedGenerator(LoopGenerator generatorAnnotation, TemplateObjectAnnotationContext context, Integer limitValue) {
        return new LoopGeneratorImpl(generatorAnnotation, context);
    }

    @SuppressWarnings("WeakerAccess")
    public static class LoopGeneratorImpl extends IntegerRangeGenerator {
        LoopGeneratorImpl(LoopGenerator loopGenerator, TemplateObjectAnnotationContext context) {
            super(loopGenerator, context, 1, loopGenerator.size(), 1);
        }

        @Override
        protected String fromToInvariant() {
            return "size >= 1";
        }
    }
}
