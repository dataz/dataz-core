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

package org.failearly.dataz.template.support.test;

import org.failearly.common.test.annotations.Subject;
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