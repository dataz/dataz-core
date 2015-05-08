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

import org.failearly.dataset.generator.RandomRangeGenerator;
import org.failearly.dataset.generator.support.Generator;
import org.failearly.dataset.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.generator.support.UnlimitedGeneratorBase;

import java.util.Iterator;
import java.util.Random;

/**
 * RandomRangeGeneratorFactory is responsible for creating of implementation instances for {@link org.failearly.dataset.generator.RandomRangeGenerator}.
 */
public final class RandomRangeGeneratorFactory extends GeneratorFactoryBase<Integer,RandomRangeGenerator> {

    @Override
    public Generator<Integer> create(RandomRangeGenerator annotation) {
        if( annotation.unique() ) {
            return doCreateUniqueGenerator(annotation, RandomRangeGeneratorFactory::calculateUniqueLimitValue);
        }

        return doCreateGenerator(annotation, annotation.limit(), RandomRangeGenerator::count);
    }

    private static int calculateUniqueLimitValue(RandomRangeGenerator generatorAnnotation) {
        final int rangeSize = generatorAnnotation.end() - generatorAnnotation.start() + 1;
        final int countValue = generatorAnnotation.count();

        if( countValue >= 0 ) {
            return Math.min(countValue,rangeSize);
        }

        return rangeSize;
    }

    @Override
    protected UnlimitedGeneratorBase<Integer> doCreateUnlimitedGenerator(RandomRangeGenerator generatorAnnotation, Integer limitValue) {
        return new RandomRangeGeneratorImpl(generatorAnnotation.name(), generatorAnnotation.dataset(), generatorAnnotation.start(), generatorAnnotation.end(), generatorAnnotation.seed());
    }


    @Override
    protected String doResolveDataSetName(RandomRangeGenerator annotation) {
        return annotation.dataset();
    }

    /**
     * Implementation for {@link org.failearly.dataset.generator.RandomRangeGenerator}.
     */
    public static final class RandomRangeGeneratorImpl extends UnlimitedGeneratorBase<Integer> {
        private final Random random;
        private int start = 0;
        private int end = 0;
        private int bound = 0;


        private RandomRangeGeneratorImpl(String name, String dataset, int start, int end, int seed) {
            super(name, dataset);

            this.start = start;
            this.end = end;
            this.bound = end - start + 1;
            this.random = random(seed);

            assert start < end : "RangeGenerator: start >= end";
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
