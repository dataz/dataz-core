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

package org.failearly.dataz.template.encoder.support;

import org.failearly.dataz.template.encoder.Encoder;

import java.util.Arrays;
import java.util.List;

/**
 * Encoders is a utility class containing some standard encoders.
 */
public final class Encoders {


    private static final EncoderBase<Object, Object> IDENTITY_ENCODER=new EncoderBase<Object, Object>() {
        @Override
        public Object encode(Object value) throws Exception {
            return value;
        }
    };
    private static final EncoderBase<char[], String> CHAR_ARRAY_TO_STRING_ENCODER=new EncoderBase<char[], String>() {
        @Override
        public String encode(char[] value) throws Exception {
            return new String(value);
        }
    };
    private static final EncoderBase<String, byte[]> STRING_TO_BYTE_ARRAY_ENCODER=new EncoderBase<String, byte[]>() {
        public byte[] encode(String source) throws Exception {
            return source.getBytes();
        }
    };
    private static final EncoderBase<byte[], String> BYTE_ARRAY_TO_STRING_ENCODER=new EncoderBase<byte[], String>() {
        @Override
        public String encode(byte[] value) throws Exception {
            return new String(value);
        }
    };

    private Encoders() {
    }

    /**
     * The identity encoder encodes nothing.
     * @return an encoder instance
     */
    public static Encoder identityEncoder() {
        return IDENTITY_ENCODER;
    }

    /**
     * Converts a string into char array.
     * @return an encoder instance
     */
    public static Encoder charArrayToString() {
        return CHAR_ARRAY_TO_STRING_ENCODER;
    }

    /**
     * Converts a string into byte array.
     * @return an encoder instance
     */
    public static Encoder stringToByteArray() {
        return STRING_TO_BYTE_ARRAY_ENCODER;
    }

    /**
     * Converts a byte array into a string.
     * @return an encoder instance
     */
    public static Encoder byteArrayToString() {
        return BYTE_ARRAY_TO_STRING_ENCODER;
    }

    /**
     * Chain or compose  encoders.
     * Caution: Any <i>null</i> input value or result of any encoder will return <i>null</i>.
     * Example:<br><br>
     * <pre>
     *    Encoder chainedEncoder = chain(ec1,ec2,ec3);
     *    assert chainedEncoder(x).equals( ec3(ec2(ec1(x))) ) : "Not ok.";
     * </pre>
     * @param encoders the encoders varargs. Must not be empty.
     * @return an encoder chain
     */
    public static Encoder chain(Encoder... encoders) {
        assert encoders.length > 0 : "At least one encoder must be provided. No encoder is not permitted!";
        return new EncoderChain(Arrays.asList(encoders));
    }


    private static final class EncoderChain extends EncoderBase<Object,Object> {

        private final List<Encoder> encoders;

        public EncoderChain(List<Encoder> encoders) {
            this.encoders=encoders;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object encode(Object value) throws Exception {
            Object result=value;
            for (Encoder encoder : encoders) {
                if( result==null )
                    break;
                result = encoder.encode(result);
            }

            return result;

        }
    }
}
