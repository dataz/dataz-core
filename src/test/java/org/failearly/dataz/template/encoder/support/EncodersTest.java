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

import org.apache.commons.lang.StringUtils;
import org.failearly.dataz.template.encoder.Encoder;
import org.failearly.common.test.ExceptionVerifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * EncodersTest contains tests for ... .
 */
@SuppressWarnings("unchecked")
public class EncodersTest {

    private static final String ANY_STRING="Hello world";

    @Test
    public void identityEncoder__should_return_same_object() throws Exception {
        // arrange / given
        final Object any=new Object();
        final Encoder<Object, Object> encoder=Encoders.identityEncoder();

        // assert / then
        assertThat(encoder.encode(any), is(sameInstance(any)));
    }


    @Test
    public void stringToByteArray__should_return_get_bytes_of_string() throws Exception {
        // arrange / given
        final Encoder<String, byte[]> encoder=Encoders.stringToByteArray();

        // assert / then
        assertThat(encoder.encode(ANY_STRING), is(ANY_STRING.getBytes()));
    }


    @Test
    public void charArrayToString__should_return_string() throws Exception {
        // arrange / given
        final Encoder<char[], String> encoder=Encoders.charArrayToString();

        // assert / then
        assertThat(encoder.encode(ANY_STRING.toCharArray()), is(ANY_STRING));
    }

    @Test
    public void byteArrayToString__should_return_string() throws Exception {
        // arrange / given
        final Encoder<byte[], String> encoder=Encoders.byteArrayToString();

        // assert / then
        assertThat(encoder.encode(ANY_STRING.getBytes()), is(ANY_STRING));
    }

    @Test
    public void chain__should_apply_encoders_from_left_to_right() throws Exception {
        // arrange / given
        final Encoder<String, String> encoder=Encoders.chain(
                Encoders.stringToByteArray(),
                invertByteArray(),
                Encoders.byteArrayToString()
        );

        // assert / then
        assertThat(encoder.encode(ANY_STRING), is(StringUtils.reverse(ANY_STRING)));
    }

    private static Encoder invertByteArray() {
        return new EncoderBase<byte[], byte[]>() {
            @Override
            public byte[] encode(byte[] value) throws Exception {
                return StringUtils.reverse(
                    new String(value)
                ).getBytes();
            }
        };
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain__must_not_except_no_encoders() throws Exception {
        ExceptionVerifier.on(() -> Encoders.chain())
            .expect(AssertionError.class)
            .expect("At least one encoder must be provided. No encoder is not permitted!")
            .verify();
    }

}