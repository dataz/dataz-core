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

import org.failearly.dataset.generator.support.Generator;
import org.failearly.dataset.generator.Limit;
import org.failearly.dataset.generator.support.UnlimitedGeneratorBase;
import org.failearly.dataset.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.generator.RandomBooleanGenerator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * RandomBooleanGeneratorFactory is responsible for ...
 */
public final class RandomBooleanGeneratorFactory extends GeneratorFactoryBase<Boolean,RandomBooleanGenerator> {
    public RandomBooleanGeneratorFactory() {
    }

    @Override
    public Generator<Boolean> create(RandomBooleanGenerator annotation) {
        return doCreateGenerator(annotation, Limit.UNLIMITED, unlimited());
    }

    @Override
    protected UnlimitedGeneratorBase<Boolean> doCreateUnlimitedGenerator(RandomBooleanGenerator generatorAnnotation, Integer limitValue) {
        return new RandomBooleanGeneratorImpl(generatorAnnotation.name(), generatorAnnotation.dataset(), generatorAnnotation.percent(), generatorAnnotation.seed());
    }

    @Override
    protected String doResolveDataSetName(RandomBooleanGenerator annotation) {
        return annotation.dataset();
    }

    private static class RandomBooleanGeneratorImpl extends UnlimitedGeneratorBase<Boolean> {

        private static final int BOUND = 1_000;
        private final Random random;
        private final Set<Integer> trueIntValues;

        private RandomBooleanGeneratorImpl(String name, String dataset, float percent, int seed) {
            super(name, dataset);

            assert 0<percent : "@RandomBooleanGenerator(name=" + name +"): percent <= 0. Only value between 0 and 100 are permitted.";

            final int percentAsIntValue = (int)(percent*(BOUND/100));

            assert percentAsIntValue<10_000 : "@RandomBooleanGenerator(name=" + name +"): percent >= 100. Only value between 0 and 100 are permitted.";

            this.random = random(seed);
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