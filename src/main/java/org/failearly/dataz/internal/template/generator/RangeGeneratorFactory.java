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

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.generator.RangeGenerator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.generator.support.IntegerRangeGenerator;
import org.failearly.dataz.template.generator.support.RangeGeneratorBase;

import java.lang.reflect.AnnotatedElement;

/**
 * RangeGeneratorFactory is responsible for creating instances of implementation for {@link org.failearly.dataz.template.generator.RangeGenerator}.
 */
public final class RangeGeneratorFactory extends GeneratorFactoryBase<Integer, RangeGenerator> {
    public RangeGeneratorFactory() {
        super(RangeGenerator.class);
    }

    @Override
    protected TemplateObject doCreate(AnnotatedElement annotatedElement, RangeGenerator annotation) {
        return doCreateGenerator(annotatedElement, annotation, annotation.limit());
    }

    @Override
    protected RangeGeneratorBase<Integer> doCreateLimitedGenerator(AnnotatedElement annotatedElement, RangeGenerator generatorAnnotation, Integer limitValue) {
        return new RangeGeneratorImpl(generatorAnnotation);
    }

    @Override
    protected String doResolveName(RangeGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(RangeGenerator annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(RangeGenerator annotation) {
        return annotation.scope();
    }

    public static class RangeGeneratorImpl extends IntegerRangeGenerator {
        private RangeGeneratorImpl(RangeGenerator rangeGenerator) {
            super(rangeGenerator,
                    rangeGenerator.from(),
                    rangeGenerator.to(),
                    rangeGenerator.step()
            );
        }
    }
}
