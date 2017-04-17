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

package org.failearly.dataz.template.support.test;

import org.failearly.dataz.common.test.annotations.Subject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


/**
 * Tests for TOA {@link Sample}, {@link SampleFactory} and {@link SampleFactory.SampleImpl}.
 */
@Subject({Sample.class, SampleFactory.class, SampleFactory.SampleImpl.class})
public class SampleTest
    extends TemplateObjectTestBase<Sample, SampleFactory, SampleFactory.SampleImpl> {
    public SampleTest() {
        super(
            Sample.class, // TOA
            SampleFactory.class, // TOF
            SampleFactory.SampleImpl.class,  // TO
            TestFixture.class   // Test Fixture
        );
    }

    @Test
    public void how_to_create_a_template_object_from_annotation_on_class_object() {
        // act / when
        final SampleFactory.SampleImpl templateObject = createTemplateObjectFromAnnotation(FIRST_ANNOTATION);

        // assert / then
        assertThat(templateObject, is(notNullValue()));
    }

    @Test
    public void how_to_create_a_template_object_from_annotation_on_method_object() {
        // act / when
        final SampleFactory.SampleImpl templateObject=createTemplateObjectFromAnnotation("namedTest");
        // or
        // final SampleFactory.SampleImpl templateObject=createTemplateObjectFromAnnotation("namedTest", FIRST_ANNOTATION);

        // assert / then
        assertThat(templateObject, is(notNullValue()));
    }

    @Test
    public void how_to_use_template_object_within_template() throws Exception {
        // act / when
        final String generated = generate(
            "$TO.sample()",
            createTemplateObjectFromAnnotation()
        );

        // assert / then
        assertThat(generated, is("sample value #1"));
    }

    @SuppressWarnings("unused")
    @Sample(name=DTON, sample = "sample value #1")
    private static class TestFixture {

        @Sample(name = DTON /* TODO: Add more attributes */)
        void namedTest() {
        }
    }
}