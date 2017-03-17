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

package org.failearly.dataz.template.encoder.support.test;

import org.failearly.common.annotations.Tests;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.encoder.Encoder;
import org.failearly.dataz.template.encoder.support.EncoderBase;
import org.failearly.dataz.template.encoder.support.EncoderFactoryBase;

/**
 * SampleEncoderFactory creates a {@link Encoder} from {@link SampleEncoder}.
 */
@Tests("SampleEncoderTest")
public class SampleEncoderFactory extends EncoderFactoryBase<SampleEncoder> {
    public SampleEncoderFactory() {
        super(SampleEncoder.class);
    }

    @Override
    protected String doResolveName(SampleEncoder annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(SampleEncoder annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(SampleEncoder annotation) {
        return annotation.scope();
    }

    @Override
    protected Encoder doCreate(SampleEncoder annotation, TemplateObjectAnnotationContext context) {
        return new SampleEncoderImpl(annotation, context);
    }

    // Must be public for Velocity!
    @Tests("SampleEncoderTest")
    public static class SampleEncoderImpl extends EncoderBase<String, byte[]> {
        SampleEncoderImpl(SampleEncoder annotation, TemplateObjectAnnotationContext context) {
            super(annotation, context);
            // TODO: For each (not standard) annotation element there should be an appropriate field assignment.
        }


        @Override
        public byte[] encode(final String value) throws Exception {
            return value.getBytes();

        }
    }
}
