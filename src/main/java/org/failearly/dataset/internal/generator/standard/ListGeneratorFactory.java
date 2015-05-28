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
package org.failearly.dataset.internal.generator.standard;

import org.failearly.dataset.generator.support.LimitedGeneratorBase;
import org.failearly.dataset.generator.ListGenerator;
import org.failearly.dataset.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.template.TemplateObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * ListGeneratorFactory is responsible for ...
 */
public final class ListGeneratorFactory extends GeneratorFactoryBase<String,ListGenerator>{

    public ListGeneratorFactory() {
        super(ListGenerator.class);
    }

    @Override
    protected TemplateObject doCreate(ListGenerator annotation) {
        return doCreateGenerator(annotation, annotation.limit(), a->a.values().length);
    }

    @Override
    protected ListGeneratorImpl doCreateLimitedGenerator(ListGenerator generatorAnnotation, Integer limitValue) {
        return new ListGeneratorImpl(generatorAnnotation.dataset(), generatorAnnotation.name(), generatorAnnotation.values());
    }

    @Override
    protected String doResolveDataSetName(ListGenerator annotation) {
        return annotation.dataset();
    }


    // Must be public for Velocity!
    public static class ListGeneratorImpl extends LimitedGeneratorBase<String> {
        private final List<String> values;

        ListGeneratorImpl(String dataset, String name, String[] values) {
            super(dataset, name);
            this.values = Arrays.asList(values);
        }

        @Override
        public Iterator<String> createIterator() {
            return values.iterator();

        }
    }
}
