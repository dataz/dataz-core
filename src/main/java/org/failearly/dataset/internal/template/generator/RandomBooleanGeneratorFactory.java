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

import org.failearly.dataset.template.generator.Limit;
import org.failearly.dataset.template.generator.support.UnlimitedGeneratorBase;
import org.failearly.dataset.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.template.generator.RandomBooleanGenerator;
import org.failearly.dataset.template.Scope;
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
        return doCreateGenerator(annotation, Limit.UNLIMITED);
    }

    @Override
    protected UnlimitedGeneratorBase<Boolean> doCreateUnlimitedGenerator(RandomBooleanGenerator generatorAnnotation, Integer limitValue) {
        return new RandomBooleanGeneratorImpl(generatorAnnotation);
    }

    @Override
    protected String doResolveDataSetName(RandomBooleanGenerator annotation) {
        return annotation.dataset();
    }

    @Override
    protected Scope doResolveScope(RandomBooleanGenerator annotation) {
        return annotation.scope();
    }


    @SuppressWarnings("WeakerAccess")
    public static class RandomBooleanGeneratorImpl extends UnlimitedGeneratorBase<Boolean> {

        private static final int BOUND = 1_000;
        private final int seed;
        private final int percentAsIntValue;
        private Set<Integer> trueIntValues;
        private Random random;

        private RandomBooleanGeneratorImpl(RandomBooleanGenerator generatorAnnotation) {
            super(generatorAnnotation);

            this.percentAsIntValue = toPercentValue(generatorAnnotation.percent());
            this.seed = generatorAnnotation.seed();
        }

        @Override
        protected void doInit() {
            super.doInit();
            checkInvariant(0<percentAsIntValue && percentAsIntValue<toPercentValue(100), "percent in (0,100)");

            this.random = random(seed);
            this.trueIntValues = createValues(percentAsIntValue, random);
        }

        private static int toPercentValue(float value) {
            return (int)((value * BOUND)/100);
        }

        private static Set<Integer> createValues(int percent, Random random) {
            final Set<Integer> result=new HashSet<>();
            while( result.size() < percent ) {
                result.add(random.nextInt(BOUND));
            }
            return result;
        }

        @Override
        protected void doReset() {
            random = random(seed);
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
