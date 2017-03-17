/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.internal.template.generator;

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.generator.RangeGenerator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.generator.support.IntegerRangeGenerator;
import org.failearly.dataz.template.generator.support.RangeGeneratorBase;

/**
 * RangeGeneratorFactory is responsible for creating instances of implementation for {@link org.failearly.dataz.template.generator.RangeGenerator}.
 */
public final class RangeGeneratorFactory extends GeneratorFactoryBase<Integer, RangeGenerator> {
    public RangeGeneratorFactory() {
        super(RangeGenerator.class);
    }

    @Override
    protected TemplateObject doCreate(RangeGenerator annotation, TemplateObjectAnnotationContext context) {
        return doCreateGenerator(annotation, context, annotation.limit());
    }

    @Override
    protected RangeGeneratorBase<Integer> doCreateLimitedGenerator(RangeGenerator generatorAnnotation, TemplateObjectAnnotationContext context, Integer limitValue) {
        return new RangeGeneratorImpl(generatorAnnotation, context);
    }

    @Override
    protected String doResolveName(RangeGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(RangeGenerator annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(RangeGenerator annotation) {
        return annotation.scope();
    }

    public static class RangeGeneratorImpl extends IntegerRangeGenerator {
        private RangeGeneratorImpl(RangeGenerator rangeGenerator, TemplateObjectAnnotationContext context) {
            super(rangeGenerator, context,
                rangeGenerator.from(),
                rangeGenerator.to(),
                rangeGenerator.step()
            );
        }
    }
}
