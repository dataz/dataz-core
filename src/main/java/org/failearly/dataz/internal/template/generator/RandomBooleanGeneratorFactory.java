/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.internal.template.generator;

import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.generator.Limit;
import org.failearly.dataz.template.generator.support.UnlimitedGeneratorBase;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.generator.RandomBooleanGenerator;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;

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
    protected TemplateObject doCreate(RandomBooleanGenerator annotation, TemplateObjectAnnotationContext context) {
        return doCreateGenerator(annotation, context, Limit.UNLIMITED);
    }

    @Override
    protected UnlimitedGeneratorBase<Boolean> doCreateUnlimitedGenerator(RandomBooleanGenerator generatorAnnotation, TemplateObjectAnnotationContext context, Integer limitValue) {
        return new RandomBooleanGeneratorImpl(generatorAnnotation, context);
    }

    @Override
    protected String doResolveName(RandomBooleanGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(RandomBooleanGenerator annotation) {
        return annotation.datasets();
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

        private RandomBooleanGeneratorImpl(RandomBooleanGenerator generatorAnnotation, TemplateObjectAnnotationContext context) {
            super(generatorAnnotation, context);

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
