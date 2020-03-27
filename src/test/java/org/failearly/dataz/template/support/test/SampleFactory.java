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

package org.failearly.dataz.template.support.test;

import org.failearly.dataz.common.Tests;
import org.failearly.dataz.template.*;

/**
 * SampleFactory creates a {@link TemplateObject} from {@link Sample}.
 */
/**
 * SampleFactory creates a {@link TemplateObject} from {@link Sample}.
 */
@Tests("SampleTest")
public class SampleFactory extends TemplateObjectFactoryBase<Sample> {
    public SampleFactory() {
        super(Sample.class);
    }

    @Override
    protected String doResolveName(Sample annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(Sample annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(Sample annotation) {
        return annotation.scope();
    }

    @Override
    protected TemplateObject doCreate(Sample annotation, TemplateObjectAnnotationContext context) {
        return new SampleImpl(annotation, context);
    }

    // Must be public for Velocity!
    @Tests("SampleTest")
    public static class SampleImpl extends TemplateObjectBase {
        SampleImpl(Sample annotation, TemplateObjectAnnotationContext context) {
            super(annotation, context);
        }


        public String sample() {
            return getAnnotation(Sample.class).sample();
        }
    }

}
