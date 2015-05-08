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

import org.failearly.dataset.generator.support.LimitedGeneratorBase;
import org.failearly.dataset.generator.ListGenerator;
import org.failearly.dataset.generator.support.Generator;
import org.failearly.dataset.generator.support.GeneratorFactoryBase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * ListGeneratorFactory is responsible for ...
 */
public final class ListGeneratorFactory extends GeneratorFactoryBase<String,ListGenerator>{
    @Override
    public Generator<String> create(ListGenerator annotation) {
        return doCreateGenerator(annotation, annotation.limit(), a->a.values().length);
    }

    @Override
    protected ListGeneratorImpl doCreateLimitedGenerator(ListGenerator generatorAnnotation, Integer limitValue) {
        return new ListGeneratorImpl(generatorAnnotation.name(), generatorAnnotation.dataset(), generatorAnnotation.values());
    }

    @Override
    protected String doResolveDataSetName(ListGenerator annotation) {
        return annotation.dataset();
    }


    // Must be public for Velocity!
    public static class ListGeneratorImpl extends LimitedGeneratorBase<String> {
        private final List<String> values;

        ListGeneratorImpl(String name, String dataset, String[] values) {
            super(name, dataset);
            this.values = Arrays.asList(values);
        }

        @Override
        public Iterator<String> createIterator() {
            return values.iterator();

        }
    }
}
