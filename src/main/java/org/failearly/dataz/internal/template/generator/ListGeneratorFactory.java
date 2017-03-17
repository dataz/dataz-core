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
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;
import org.failearly.dataz.template.generator.ListGenerator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;

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
    protected TemplateObject doCreate(ListGenerator annotation, TemplateObjectAnnotationContext context) {
        return doCreateGenerator(annotation, context, annotation.limit(), annotation.values().length);
    }

    @Override
    protected ListGeneratorImpl doCreateLimitedGenerator(ListGenerator generatorAnnotation, TemplateObjectAnnotationContext context, Integer limitValue) {
        return new ListGeneratorImpl(generatorAnnotation, context);
    }

    @Override
    protected String doResolveName(ListGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(ListGenerator annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(ListGenerator annotation) {
        return annotation.scope();
    }

    public static class ListGeneratorImpl extends LimitedGeneratorBase<String> {
        private final List<String> values;

        private ListGeneratorImpl(ListGenerator annotation, TemplateObjectAnnotationContext context) {
            super(annotation, context);
            this.values = Arrays.asList(annotation.values());
        }

        @Override
        public Iterator<String> createIterator() {
            return values.iterator();
        }
    }
}
