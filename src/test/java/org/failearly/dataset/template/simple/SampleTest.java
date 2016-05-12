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

package org.failearly.dataset.template.simple;

import org.failearly.common.test.annotations.Subject;
import org.failearly.dataset.template.support.test.TemplateObjectTestBase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
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
    public void TOF___should_create_appropriate_TO() {
        // arrange / given
        // act / when
        final SampleFactory.SampleImpl templateObject=createTemplateObjectFromAnnotation(0);

        // assert / then
        // TODO: Better assertion, then not null check.
        assertThat(templateObject, is(notNullValue()));
    }

    @Test
    public void TOF___should_create_appropriate_TO__using_method_access() {
        // act / when
        final SampleFactory.SampleImpl templateObject=createTemplateObjectFromAnnotation("namedTest");
        // or
        // final SampleFactory.SampleImpl templateObject=createTemplateObjectFromAnnotation("namedTest", 0);

        // assert / then
        // TODO: Better assertion, then not null check.
        assertThat(templateObject, is(notNullValue()));
    }

    @Test
    public void TO_applied_in_template() throws Exception {
        // arrange / given
        // act / when
        final String generated = generate(template(SIMPLE_TEMPLATE), createTemplateObjectFromAnnotation());

        // assert / then
        // TODO: Better assertion, then not empty check.
        assertThat(generated, is(not("")));
    }

    /**
     * The purpose is to hold your Sample annotations.
     */
    @Sample(name=DTON /* TODO: Add more attributes */)
    private static class TestFixture {

        @Sample(name=DTON /* TODO: Add more attributes */)
        void namedTest() {}
    }
}