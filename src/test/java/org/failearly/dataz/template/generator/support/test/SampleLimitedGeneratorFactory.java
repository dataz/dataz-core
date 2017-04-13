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
package org.failearly.dataz.template.generator.support.test;

import org.failearly.dataz.common.Tests;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;

import java.util.Collections;
import java.util.Iterator;

/**
 * SampleLimitedGeneratorFactory creates a {@link SampleLimitedGeneratorImpl} from {@link SampleLimitedGenerator}.
 */
@Tests("SampleLimitedGeneratorTest")
public class SampleLimitedGeneratorFactory extends GeneratorFactoryBase<String,SampleLimitedGenerator> {
    public SampleLimitedGeneratorFactory() {
        super(SampleLimitedGenerator.class);
    }

    @Override
    protected String doResolveName(SampleLimitedGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(SampleLimitedGenerator annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(SampleLimitedGenerator annotation) {
        return annotation.scope();
    }

    @Override
    protected TemplateObject doCreate(SampleLimitedGenerator annotation, TemplateObjectAnnotationContext context) {
        return doCreateGenerator(annotation, context, annotation.limit());
    }

    @Override
    protected LimitedGeneratorBase<String> doCreateLimitedGenerator(final SampleLimitedGenerator annotation, TemplateObjectAnnotationContext context, final Integer limitValue) {
        return new SampleLimitedGeneratorImpl(annotation, context);
    }

    // Must be public for Velocity!
    @Tests("SampleLimitedGeneratorTest")
    public static class SampleLimitedGeneratorImpl extends LimitedGeneratorBase<String> {
        SampleLimitedGeneratorImpl(SampleLimitedGenerator annotation, TemplateObjectAnnotationContext context) {
            super(annotation, context);
        }

        @Override
        public Iterator<String> createIterator() {
            return Collections.emptyIterator();
        }
    }
}
