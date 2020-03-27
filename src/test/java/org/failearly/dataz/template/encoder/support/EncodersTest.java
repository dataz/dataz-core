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

package org.failearly.dataz.template.encoder.support;

import org.apache.commons.lang.StringUtils;
import org.failearly.dataz.template.encoder.Encoder;
import org.failearly.dataz.common.test.ExceptionVerifier;
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