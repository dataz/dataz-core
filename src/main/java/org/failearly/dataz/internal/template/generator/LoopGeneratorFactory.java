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
import org.failearly.dataz.template.generator.LoopGenerator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.generator.support.IntegerRangeGenerator;
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;

/**
 * LoopGeneratorFactory is responsible for creating instances of {@link org.failearly.dataz.template.generator.LoopGenerator}.
 */
public final class LoopGeneratorFactory extends GeneratorFactoryBase<Integer, LoopGenerator> {
    public LoopGeneratorFactory() {
        super(LoopGenerator.class);
    }

    @Override
    protected String doResolveName(LoopGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(LoopGenerator annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(LoopGenerator annotation) {
        return annotation.scope();
    }

    @Override
    protected TemplateObject doCreate(LoopGenerator annotation, TemplateObjectAnnotationContext context) {
        return doCreateGenerator(annotation, context, Limit.LIMITED);
    }

    @Override
    protected LimitedGeneratorBase<Integer> doCreateLimitedGenerator(LoopGenerator generatorAnnotation, TemplateObjectAnnotationContext context, Integer limitValue) {
        return new LoopGeneratorImpl(generatorAnnotation, context);
    }

    @SuppressWarnings("WeakerAccess")
    public static class LoopGeneratorImpl extends IntegerRangeGenerator {
        LoopGeneratorImpl(LoopGenerator loopGenerator, TemplateObjectAnnotationContext context) {
            super(loopGenerator, context, 1, loopGenerator.size(), 1);
        }

        @Override
        protected String fromToInvariant() {
            return "size >= 1";
        }
    }
}
