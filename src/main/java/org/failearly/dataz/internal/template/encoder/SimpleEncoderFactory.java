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

package org.failearly.dataz.internal.template.encoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.encoder.Encoder;
import org.failearly.dataz.template.encoder.SimpleEncoder;
import org.failearly.dataz.template.encoder.support.EncoderBase;
import org.failearly.dataz.template.encoder.support.EncoderFactoryBase;
import org.failearly.dataz.template.encoder.support.StringEncoderBase;

import static org.failearly.dataz.template.encoder.support.Encoders.*;

/**
 * SimpleEncoderFactory is responsible for ...
 */
public final class SimpleEncoderFactory extends EncoderFactoryBase<SimpleEncoder> {
    public SimpleEncoderFactory() {
        super(SimpleEncoder.class);
    }

    @Override
    protected Encoder doCreate(SimpleEncoder annotation, TemplateObjectAnnotationContext context) {
        return new SimpleEncoderImpl(context, annotation);
    }

    @Override
    protected String doResolveName(SimpleEncoder annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(SimpleEncoder annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(SimpleEncoder annotation) {
        return annotation.scope();
    }

    public static final class SimpleEncoderImpl extends StringEncoderBase {

        private final Encoder encoder;

        private SimpleEncoderImpl(TemplateObjectAnnotationContext context, SimpleEncoder annotation) {
            super(annotation, context);
            encoder=createEncoder(annotation);
        }

        private static Encoder createEncoder(SimpleEncoder annotation) {
            switch (annotation.type()) {
                case NONE:
                    return identityEncoder();
                case HEX:
                    return chain(stringToByteArray(), toDataSetEncoder(new Hex()), charArrayToString());
                case BASE64:
                    return chain(stringToByteArray(), toDataSetEncoder(new Base64()), byteArrayToString());
            }

            throw new IllegalArgumentException("Unknown encoder type " + annotation.type());
        }


        @Override
        @SuppressWarnings("unchecked")
        public String encode(String value) throws Exception {
            return (String) encoder.encode(value);
        }
    }

    private static Encoder toDataSetEncoder(org.apache.commons.codec.Encoder encoder) {
        return new ApacheCodecEncoderFacade(encoder);
    }


    private static final class ApacheCodecEncoderFacade extends EncoderBase<Object, Object> {
        private final org.apache.commons.codec.Encoder encoder;

        private ApacheCodecEncoderFacade(org.apache.commons.codec.Encoder encoder) {
            this.encoder=encoder;
        }

        @Override
        public Object encode(Object value) throws Exception {
            return this.encoder.encode(value);
        }
    }
}
