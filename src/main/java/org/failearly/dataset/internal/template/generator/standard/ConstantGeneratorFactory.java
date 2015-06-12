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

import org.failearly.dataset.template.generator.ConstantGenerator;
import org.failearly.dataset.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.template.generator.support.UnlimitedGeneratorBase;
import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObject;

import java.util.Iterator;

/**
 * ConstantGeneratorFactory is responsible for creating of implementation instances for {@link org.failearly.dataset.template.generator.ConstantGenerator}.
 */
public final class ConstantGeneratorFactory extends GeneratorFactoryBase<String, ConstantGenerator> {

    public ConstantGeneratorFactory() {
        super(ConstantGenerator.class);
    }

    @Override
    protected TemplateObject doCreate(ConstantGenerator annotation) {
        return doCreateGenerator(annotation, annotation.limit(), ConstantGenerator::count);
    }

    @Override
    protected UnlimitedGeneratorBase<String> doCreateUnlimitedGenerator(ConstantGenerator generatorAnnotation, Integer limitValue) {
        return new ConstantGeneratorImpl(generatorAnnotation);
    }

    @Override
    protected String doResolveDataSetName(ConstantGenerator annotation) {
        return annotation.dataset();
    }


    @Override
    protected Scope doResolveScope(ConstantGenerator annotation) {
        return annotation.scope();
    }

    // Must be public for Velocity!
    public static class ConstantGeneratorImpl extends UnlimitedGeneratorBase<String> {
        private final String constant;

        ConstantGeneratorImpl(ConstantGenerator annotation) {
            super(annotation, annotation.dataset(), annotation.name(), annotation.scope());
            this.constant = annotation.constant();
        }

        @Override
        public Iterator<String> createIterator() {
            return new Iterator<String>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public String next() {
                    return constant;
                }
            };
        }
    }
}
