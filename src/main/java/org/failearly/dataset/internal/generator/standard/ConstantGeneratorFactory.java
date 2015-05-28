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

import org.failearly.dataset.generator.ConstantGenerator;
import org.failearly.dataset.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.generator.support.UnlimitedGeneratorBase;
import org.failearly.dataset.template.TemplateObject;

import java.util.Iterator;

/**
 * ConstantGeneratorFactory is responsible for creating of implementation instances for {@link org.failearly.dataset.generator.ConstantGenerator}.
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
        return new ConstantGeneratorImpl(generatorAnnotation.name(), generatorAnnotation.dataset(), generatorAnnotation.constant());
    }

    @Override
    protected String doResolveDataSetName(ConstantGenerator annotation) {
        return annotation.dataset();
    }

    // Must be public for Velocity!
    public static class ConstantGeneratorImpl extends UnlimitedGeneratorBase<String> {
        private final String constant;

        ConstantGeneratorImpl(String name, String dataset, String constant) {
            super(dataset, name);
            this.constant = constant;
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



        @Override
        public String toString() {
            return "ConstantGenerator{" +
                    "dataset=" + super.dataset() +
                    ", name=" + super.name() +
                    ", constant=" + constant +
                    '}';
        }

    }
}
