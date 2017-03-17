/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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
