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

package org.failearly.dataz.template.encoder.support.test;

import org.failearly.dataz.common.test.annotations.Subject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


/**
 * Tests for TOA {@link SampleEncoder}, {@link SampleEncoderFactory} and {@link SampleEncoderFactory.SampleEncoderImpl}.
 */
@Subject({SampleEncoder.class, SampleEncoderFactory.class, SampleEncoderFactory.SampleEncoderImpl.class})
public class SampleEncoderTest
    extends EncoderTestBase<String, byte[], SampleEncoder, SampleEncoderFactory, SampleEncoderFactory.SampleEncoderImpl> {
    public SampleEncoderTest() {
        super(
            SampleEncoder.class, // TOA
            SampleEncoderFactory.class, // TOF
            SampleEncoderFactory.SampleEncoderImpl.class,  // TO
            TestFixture.class   // Test Fixture
        );
    }

    @Test
    public void how_to_create_a_template_object_from_annotation_on_class_object() {
        // act / when
        final SampleEncoderFactory.SampleEncoderImpl templateObject = createTemplateObjectFromAnnotation(FIRST_ANNOTATION);

        // assert / then
        // TODO: Better assertion, then not null check.
        assertThat(templateObject, is(notNullValue()));
    }

    @Test
    public void how_to_create_a_template_object_from_annotation_on_method_object() {
        // act / when
        final SampleEncoderFactory.SampleEncoderImpl templateObject = createTemplateObjectFromAnnotation("namedTest");
        // or
        // final SampleEncoderFactory.SampleEncoderImpl templateObject=createTemplateObjectFromAnnotation("namedTest", FIRST_ANNOTATION);

        // assert / then
        // TODO: Better assertion, then not null check.
        assertThat(templateObject, is(notNullValue()));
    }

    @Test
    public void how_to_use_template_object_within_template() throws Exception {
        // act / when
        final String generated = generate(
            "$TO.toString()", // or template(SIMPLE_TEMPLATE),
            createTemplateObjectFromAnnotation()
        );

        // assert / then
        // TODO: Better assertion, then not empty check.
        assertThat(generated, is(not("")));
    }


    @SampleEncoder(name = DTON /* TODO: Add more attributes */)
    private static class TestFixture {

        @SampleEncoder(name = DTON /* TODO: Add more attributes */)
        void namedTest() {
        }
    }
}