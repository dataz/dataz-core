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

package org.failearly.dataset.internal.template.generator.standard;

import org.failearly.dataset.template.generator.RangeGenerator;
import org.failearly.dataset.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObject;

/**
 * RangeGeneratorFactory is responsible for creating instances of implementation for {@link org.failearly.dataset.template.generator.RangeGenerator}.
 */
public final class RangeGeneratorFactory extends GeneratorFactoryBase<Integer, RangeGenerator> {
    public RangeGeneratorFactory() {
        super(RangeGenerator.class);
    }

    @Override
    protected TemplateObject doCreate(RangeGenerator annotation) {
        return doCreateGenerator(annotation, annotation.limit(), a -> 0);
    }

    @Override
    protected RangeGeneratorImpl doCreateLimitedGenerator(RangeGenerator generatorAnnotation, Integer limitValue) {
        return new RangeGeneratorImpl(generatorAnnotation);
    }


    @Override
    protected String doResolveDataSetName(RangeGenerator annotation) {
        return annotation.dataset();
    }

    @Override
    protected Scope doResolveScope(RangeGenerator annotation) {
        return annotation.scope();
    }
}
