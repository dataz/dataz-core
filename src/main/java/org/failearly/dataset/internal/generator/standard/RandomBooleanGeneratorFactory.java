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

import org.failearly.dataset.generator.Limit;
import org.failearly.dataset.generator.support.UnlimitedGeneratorBase;
import org.failearly.dataset.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.generator.RandomBooleanGenerator;
import org.failearly.dataset.template.TemplateObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * RandomBooleanGeneratorFactory is responsible for ...
 */
public final class RandomBooleanGeneratorFactory extends GeneratorFactoryBase<Boolean,RandomBooleanGenerator> {
    public RandomBooleanGeneratorFactory() {
        super(RandomBooleanGenerator.class);
    }

    @Override
    protected TemplateObject doCreate(RandomBooleanGenerator annotation) {
        return doCreateGenerator(annotation, Limit.UNLIMITED, unlimited());
    }

    @Override
    protected UnlimitedGeneratorBase<Boolean> doCreateUnlimitedGenerator(RandomBooleanGenerator generatorAnnotation, Integer limitValue) {
        return new RandomBooleanGeneratorImpl(generatorAnnotation);
    }

    @Override
    protected String doResolveDataSetName(RandomBooleanGenerator annotation) {
        return annotation.dataset();
    }

    private static class RandomBooleanGeneratorImpl extends UnlimitedGeneratorBase<Boolean> {

        private static final int BOUND = 1_000;
        private final Random random;
        private final Set<Integer> trueIntValues;

        private RandomBooleanGeneratorImpl(RandomBooleanGenerator generatorAnnotation) {
            super(generatorAnnotation, generatorAnnotation.dataset(), generatorAnnotation.name());

            final int percentAsIntValue = (int)(generatorAnnotation.percent() * (BOUND/100));

            assert 0<percentAsIntValue : generatorAnnotation +": percent <= 0. Only value between 0 and 100 are permitted.";
            assert percentAsIntValue<10_000 : generatorAnnotation +": percent >= 100. Only value between 0 and 100 are permitted.";

            this.random = random(generatorAnnotation.seed());
            this.trueIntValues = createValues(percentAsIntValue);
        }

        private Set<Integer> createValues(int percent) {
            Set<Integer> result=new HashSet<>();
            while( result.size() < percent ) {
                result.add(random.nextInt(BOUND));
            }
            return result;
        }

        @Override
        public Iterator<Boolean> createIterator() {
            return new Iterator<Boolean>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Boolean next() {
                    return nextValue();
                }
            };
        }

        private boolean nextValue() {
            return trueIntValues.contains(random.nextInt(BOUND));
        }

    }
}
