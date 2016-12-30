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

package org.failearly.dataz.internal.template.encoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.failearly.dataz.template.Scope;
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
    protected Encoder doCreate(SimpleEncoder annotation) {
        return new SimpleEncoderImpl(annotation);
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

        private SimpleEncoderImpl(SimpleEncoder annotation) {
            super(annotation);
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
