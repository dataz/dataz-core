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

import org.failearly.common.annotations.Tests;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.generator.support.UnlimitedGeneratorBase;
import org.failearly.dataz.template.support.test.ReplaceMe;

import java.util.Collections;
import java.util.Iterator;

/**
 * SampleUnlimitedGeneratorFactory creates a {@link SampleUnlimitedGeneratorImpl} from {@link SampleUnlimitedGenerator}.
 */
@Tests("SampleUnlimitedGeneratorTest")
public class SampleUnlimitedGeneratorFactory extends GeneratorFactoryBase<ReplaceMe,SampleUnlimitedGenerator> {
    public SampleUnlimitedGeneratorFactory() {
        super(SampleUnlimitedGenerator.class);
    }

    @Override
    protected String doResolveName(SampleUnlimitedGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(SampleUnlimitedGenerator annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(SampleUnlimitedGenerator annotation) {
        return annotation.scope();
    }

    @Override
    protected TemplateObject doCreate(SampleUnlimitedGenerator annotation, TemplateObjectAnnotationContext context) {
        return doCreateGenerator(annotation, context, annotation.limit());
    }

    @Override
    protected UnlimitedGeneratorBase<ReplaceMe> doCreateUnlimitedGenerator(SampleUnlimitedGenerator annotation, TemplateObjectAnnotationContext context, Integer limitValue) {
        return new SampleUnlimitedGeneratorImpl(annotation, context);
    }

    // Must be public for Velocity!
    @Tests("SampleUnlimitedGeneratorTest")
    public static class SampleUnlimitedGeneratorImpl extends UnlimitedGeneratorBase<ReplaceMe> {
        SampleUnlimitedGeneratorImpl(SampleUnlimitedGenerator annotation, TemplateObjectAnnotationContext context) {
            super(annotation, context);
            // TODO: For each (not standard) annotation element there should be an appropriate field assignment.
        }

        @Override
        public Iterator<ReplaceMe> createIterator() {
            // TODO: Implement SampleUnlimitedGeneratorImpl#createIterator
            return Collections.emptyIterator();
        }
    }
}

