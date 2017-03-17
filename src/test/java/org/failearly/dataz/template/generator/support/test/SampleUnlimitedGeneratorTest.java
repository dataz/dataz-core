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

package org.failearly.dataz.template.generator.support.test;

import org.failearly.common.test.annotations.Subject;
import org.failearly.dataz.template.support.test.ReplaceMe;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for TOA {@link SampleUnlimitedGenerator}, {@link SampleUnlimitedGeneratorFactory} and {@link SampleUnlimitedGeneratorFactory.SampleUnlimitedGeneratorImpl}.
 */
@Subject({SampleUnlimitedGenerator.class, SampleUnlimitedGeneratorFactory.class, SampleUnlimitedGeneratorFactory.SampleUnlimitedGeneratorImpl.class})
public class SampleUnlimitedGeneratorTest
    extends UnlimitedGeneratorTestBase<ReplaceMe, SampleUnlimitedGenerator, SampleUnlimitedGeneratorFactory, SampleUnlimitedGeneratorFactory.SampleUnlimitedGeneratorImpl> {
    public SampleUnlimitedGeneratorTest() {
        super(
            SampleUnlimitedGenerator.class, // TOA
            SampleUnlimitedGeneratorFactory.class, // TOF
            SampleUnlimitedGeneratorFactory.SampleUnlimitedGeneratorImpl.class,  // TO
            TestFixture.class   // Test Fixture
        );
    }

    @Test
    public void how_to_create_a_template_object_from_annotation_on_class_object() {
        // act / when
        final SampleUnlimitedGeneratorFactory.SampleUnlimitedGeneratorImpl templateObject = createTemplateObjectFromAnnotation(FIRST_ANNOTATION);

        // assert / then
        // TODO: Better assertion, then not null check.
        assertThat(templateObject, is(notNullValue()));
    }

    @Test
    public void how_to_create_a_template_object_from_annotation_on_method_object() {
        // act / when
        //final SampleUnlimitedGeneratorFactory.SampleUnlimitedGeneratorImpl templateObject = createTemplateObjectFromAnnotation("namedTest");
        // or
        final SampleUnlimitedGeneratorFactory.SampleUnlimitedGeneratorImpl templateObject=createTemplateObjectFromAnnotation("namedTest", FIRST_ANNOTATION);

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

    @SampleUnlimitedGenerator(name = DTON /* TODO: Add more attributes */)
    private static class TestFixture {

        @SampleUnlimitedGenerator(name = DTON /* TODO: Add more attributes */)
        void namedTest() {
        }
    }
}

