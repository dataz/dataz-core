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

package org.failearly.dataset.internal.template.generator;

import org.failearly.dataset.exception.DataSetException;
import org.failearly.dataset.template.generator.RandomRangeGenerator;
import org.failearly.dataset.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.template.generator.support.UnlimitedGeneratorBase;
import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObject;

import java.util.Iterator;
import java.util.Random;

/**
 * RandomRangeGeneratorFactory is responsible for creating of implementation instances for {@link org.failearly.dataset.template.generator.RandomRangeGenerator}.
 */
public final class RandomRangeGeneratorFactory extends GeneratorFactoryBase<Integer, RandomRangeGenerator> {

    public RandomRangeGeneratorFactory() {
        super(RandomRangeGenerator.class);
    }

    @Override
    protected TemplateObject doCreate(RandomRangeGenerator annotation) {
        if (annotation.unique()) {
            return doCreateUniqueGenerator(annotation, calculateUniqueLimitValue(annotation));
        }

        return doCreateGenerator(annotation, annotation.limit(), calculateLimitValue(annotation));
    }

    private static int calculateUniqueLimitValue(RandomRangeGenerator generatorAnnotation) {
        final int rangeSize = generatorAnnotation.end() - generatorAnnotation.start() + 1;
        final int countValue = generatorAnnotation.count();

        if (countValue > 0) {
            return Math.min(countValue, rangeSize);
        }

        return rangeSize;
    }

    private static int calculateLimitValue(RandomRangeGenerator generatorAnnotation) {
        final int rangeSize = generatorAnnotation.end() - generatorAnnotation.start() + 1;
        final int countValue = generatorAnnotation.count();

        if (countValue > 0) {
            return countValue;
        }

        return rangeSize;
    }

    @Override
    protected UnlimitedGeneratorBase<Integer> doCreateUnlimitedGenerator(RandomRangeGenerator generatorAnnotation, Integer limitValue) {
        return new RandomRangeGeneratorImpl(generatorAnnotation);
    }


    @Override
    protected String doResolveDataSetName(RandomRangeGenerator annotation) {
        return annotation.dataset();
    }

    @Override
    protected Scope doResolveScope(RandomRangeGenerator annotation) {
        return annotation.scope();
    }

    private static final class RandomRangeGeneratorImpl extends UnlimitedGeneratorBase<Integer> {
        private Random random;
        private final int seed;
        private final int start;
        private final int end;
        private final int bound;


        private RandomRangeGeneratorImpl(RandomRangeGenerator annotation) {
            super(annotation, annotation.dataset(), annotation.name(), annotation.scope());

            this.start = annotation.start();
            this.end = annotation.end();
            this.seed = annotation.seed();
            this.random = random(this.seed);
            this.bound = end - start + 1;
        }

        @Override
        protected void doInit() throws DataSetException {
            super.doInit();
            checkInvariant(start < end, "start < end");
        }

        @Override
        protected void doReset() {
            this.random = random(seed);
        }


        @Override
        public final Iterator<Integer> createIterator() {
            return new Iterator<Integer>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Integer next() {
                    return nextValue();
                }
            };
        }

        private Integer nextValue() {
            final int val = random.nextInt(bound) + start;
            assert val >= start && val <= end : "value (=" + val + ") is not in range [" + start + "," + end + "]";
            return val;
        }
    }
}
