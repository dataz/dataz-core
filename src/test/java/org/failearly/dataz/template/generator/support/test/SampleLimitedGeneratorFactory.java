/*
 * Copyright (c) 2009.
 *
 * Date: 31.10.16
 *
 */
package org.failearly.dataz.template.generator.support.test;

import org.failearly.common.annotations.Tests;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.generator.Generator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;

import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.Iterator;

/**
 * SampleLimitedGeneratorFactory creates a {@link Generator} from {@link SampleLimitedGenerator}.
 */
@Tests("SampleLimitedGeneratorTest")
public class SampleLimitedGeneratorFactory extends GeneratorFactoryBase<Object,SampleLimitedGenerator> {
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
    protected TemplateObject doCreate(AnnotatedElement annotatedElement, SampleLimitedGenerator annotation) {
        return doCreateGenerator(annotatedElement, annotation, annotation.limit());
    }

    @Override
    protected LimitedGeneratorBase<Object> doCreateLimitedGenerator(AnnotatedElement annotatedElement, final SampleLimitedGenerator annotation, final Integer limitValue) {
        return new SampleLimitedGeneratorImpl(annotation);
    }

    // Must be public for Velocity!
    @Tests("SampleLimitedGeneratorTest")
    public static class SampleLimitedGeneratorImpl extends LimitedGeneratorBase<Object> {
        SampleLimitedGeneratorImpl(SampleLimitedGenerator annotation) {
            super(annotation);
            // TODO: For each (not standard) annotation element there should be an appropriate field assignment.
        }

        @Override
        public Iterator<Object> createIterator() {
            // TODO: Implement SampleLimitedGeneratorImpl#createIterator
            return Collections.emptyIterator();
        }
    }
}
