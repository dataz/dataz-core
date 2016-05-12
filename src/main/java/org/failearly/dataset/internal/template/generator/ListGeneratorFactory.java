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

package org.failearly.dataset.internal.template.generator;

import org.failearly.dataset.template.generator.support.LimitedGeneratorBase;
import org.failearly.dataset.template.generator.ListGenerator;
import org.failearly.dataset.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.template.Scope;
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
        return doCreateGenerator(annotation, annotation.limit(), annotation.values().length);
    }

    @Override
    protected ListGeneratorImpl doCreateLimitedGenerator(ListGenerator generatorAnnotation, Integer limitValue) {
        return new ListGeneratorImpl(generatorAnnotation);
    }

    @Override
    protected String doResolveDataSetName(ListGenerator annotation) {
        return annotation.dataset();
    }

    @Override
    protected Scope doResolveScope(ListGenerator annotation) {
        return annotation.scope();
    }

    public static class ListGeneratorImpl extends LimitedGeneratorBase<String> {
        private final List<String> values;

        private ListGeneratorImpl(ListGenerator annotation) {
            super(annotation);
            this.values = Arrays.asList(annotation.values());
        }

        @Override
        public Iterator<String> createIterator() {
            return values.iterator();
        }
    }
}
