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
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@Subject({SampleLimitedGenerator.class, SampleLimitedGeneratorFactory.class, SampleLimitedGeneratorFactory.SampleLimitedGeneratorImpl.class})
public class SampleLimitedGeneratorTest
    extends LimitedGeneratorTestBase<String, SampleLimitedGenerator, SampleLimitedGeneratorFactory, SampleLimitedGeneratorFactory.SampleLimitedGeneratorImpl> {
    public SampleLimitedGeneratorTest() {
        super(
            SampleLimitedGenerator.class, // TOA
            SampleLimitedGeneratorFactory.class, // TOF
            SampleLimitedGeneratorFactory.SampleLimitedGeneratorImpl.class,  // TO
            TestFixture.class   // Test Fixture
        );
    }

    @Test
    public void how_to_create_a_template_object_from_annotation_on_class_object() {
        // act / when
        final SampleLimitedGeneratorFactory.SampleLimitedGeneratorImpl templateObject = createTemplateObjectFromAnnotation(FIRST_ANNOTATION);

        // assert / then
        // TODO: Better assertion, then not null check.
        assertThat(templateObject, is(notNullValue()));
    }

    @Test
    public void how_to_create_a_template_object_from_annotation_on_method_object() {
        // act / when
        final SampleLimitedGeneratorFactory.SampleLimitedGeneratorImpl templateObject = createTemplateObjectFromAnnotation("namedTest");
        // or
        // final SampleLimitedGeneratorFactory.SampleLimitedGeneratorImpl templateObject=createTemplateObjectFromAnnotation("namedTest", FIRST_ANNOTATION);

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

    @SampleLimitedGenerator(name = DTON /* TODO: Add more attributes */)
    private static class TestFixture {

        @SampleLimitedGenerator(name = DTON /* TODO: Add more attributes */)
        void namedTest() {
        }
    }

}



