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

package org.failearly.dataz.template.generator.support.test;

import org.failearly.common.test.annotations.Subject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


/**
 * Tests for TOA {@link SampleLimitedGenerator}, {@link SampleLimitedGeneratorFactory} and {@link SampleLimitedGeneratorFactory.SampleLimitedGeneratorImpl}.
 */
@Subject({SampleLimitedGenerator.class, SampleLimitedGeneratorFactory.class, SampleLimitedGeneratorFactory.SampleLimitedGeneratorImpl.class})
public class SampleLimitedGeneratorTest
    extends LimitedGeneratorTestBase<Object, SampleLimitedGenerator, SampleLimitedGeneratorFactory, SampleLimitedGeneratorFactory.SampleLimitedGeneratorImpl> {
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
        final SampleLimitedGeneratorFactory.SampleLimitedGeneratorImpl templateObject=createTemplateObjectFromAnnotation(FIRST_ANNOTATION);

        // assert / then
        // TODO: Better assertion, then not null check.
        assertThat(templateObject, is(notNullValue()));
    }


    @Test
    public void how_to_create_a_template_object_from_annotation_on_method_object() {
        // act / when
        final SampleLimitedGeneratorFactory.SampleLimitedGeneratorImpl templateObject=createTemplateObjectFromAnnotation("namedTest");
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


    @SampleLimitedGenerator(name=DTON /* TODO: Add more attributes */)
    private static class TestFixture {

        @SampleLimitedGenerator(name=DTON /* TODO: Add more attributes */)
        void namedTest() {}
    }
}


