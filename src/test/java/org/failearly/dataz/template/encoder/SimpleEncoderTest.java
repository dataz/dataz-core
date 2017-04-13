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

package org.failearly.dataz.template.encoder;

import org.failearly.dataz.internal.common.test.annotations.Subject;
import org.failearly.dataz.internal.template.encoder.SimpleEncoderFactory;
import org.failearly.dataz.internal.template.encoder.SimpleEncoderFactory.SimpleEncoderImpl;
import org.failearly.dataz.template.encoder.support.test.EncoderTestBase;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * SimpleEncoderTest contains tests for {@link SimpleEncoder} and  {@link SimpleEncoderFactory}.
 */
@Subject({SimpleEncoder.class, SimpleEncoderFactory.class, SimpleEncoderImpl.class})
public class SimpleEncoderTest extends EncoderTestBase<String, String, SimpleEncoder, SimpleEncoderFactory, SimpleEncoderImpl> {

    private static final int NONE_ENCODER=0;
    private static final int HEX_ENCODER=1;
    private static final String HELLO_WORLD_TEMPLATE=PLACE_HOLDER_TON + ".encode('Hello world')";
    private static final int BASE64_ENCODER=2;

    public SimpleEncoderTest() {
        super(
            SimpleEncoder.class,
            SimpleEncoderFactory.class,
                SimpleEncoderImpl.class, TestFixture.class
        );
    }

    @Test
    public void none_encoder() throws Exception {
        // act / when
        final String generated=generate(
            template(HELLO_WORLD_TEMPLATE),
                super.createTemplateObjectFromAnnotation(NONE_ENCODER)
        );

        // assert / then
        assertThat(generated, is("Hello world"));
    }

    @Test
    public void hex_encoder() throws Exception {
        // act / when
        final String generated=generate(
            template(HELLO_WORLD_TEMPLATE),
                super.createTemplateObjectFromAnnotation(HEX_ENCODER)
        );

        // assert / then
        assertThat(generated, is("48656c6c6f20776f726c64"));
    }

   @Test
    public void base64_encoder() throws Exception {
        // act / when
       final String generated=generate(
            template(HELLO_WORLD_TEMPLATE),
               super.createTemplateObjectFromAnnotation(BASE64_ENCODER)
        );

        // assert / then
        assertThat(generated, is("SGVsbG8gd29ybGQ="));
    }



    @SimpleEncoder(name= DTON, type = SimpleEncoder.Type.NONE)
    @SimpleEncoder(name= DTON, type = SimpleEncoder.Type.HEX)
    @SimpleEncoder(name= DTON, type = SimpleEncoder.Type.BASE64)
    private static class TestFixture {
    }
}

