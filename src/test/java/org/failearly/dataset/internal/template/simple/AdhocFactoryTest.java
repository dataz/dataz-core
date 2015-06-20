/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.internal.template.simple;

import org.failearly.dataset.template.Property;
import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.simple.Adhoc;
import org.failearly.dataset.template.simple.support.AdhocTemplateObjectBase;
import org.failearly.dataset.test.AnnotationHelper;
import org.junit.Before;
import org.junit.Test;

import static org.failearly.dataset.test.AssertException.assertException;
import static org.failearly.dataset.test.TemplateObjectMatchers.isTemplateObjectAttributes;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * AdhocFactoryTest contains tests for {@link Adhoc} and {@link AdhocFactory}.
 */
public class AdhocFactoryTest {

    private static final String DATASET = "DS";
    private static final String NAME = "TO-NAME";

    private AnnotationHelper<Adhoc> annotationHelper;

    @Before
    public void setUp() throws Exception {
        annotationHelper = AnnotationHelper.createAnnotationHelper(Adhoc.class)
                .withFixtureClass(TestFixture.class);
    }

    @Test
    public void should_set_standard_attributes() throws Exception {
        // arrange / given
        final CustomAdhocImplementation templateObject = createAdhocTemplateObject();

        // assert / then
        assertThat("Standard elements?", templateObject, isTemplateObjectAttributes(NAME, DATASET, Scope.GLOBAL));
    }

    @Test
    public void should_set_arguments() throws Exception {
        // arrange / given
        final CustomAdhocImplementation templateObject = createAdhocTemplateObject();

        // assert / then
        assertThat("args?", templateObject.getArguments(), contains("argument 0", "argument 1"));
    }

    @Test
    public void should_set_properties() throws Exception {
        // arrange / given
        final CustomAdhocImplementation templateObject = createAdhocTemplateObject();

        // assert / then
        assertThat("Key1?", templateObject.getProperties().getStringValue("key1"), is("value1"));
        assertThat("Key2?", templateObject.getProperties().getStringValue("key2"), is("value2"));
        assertException(IllegalArgumentException.class, () -> templateObject.getProperties().getStringValue("unknown key"));
    }

    private CustomAdhocImplementation createAdhocTemplateObject() {
        return (CustomAdhocImplementation) new AdhocFactory().create(annotationHelper.getAnnotation(0));
    }


    @Adhoc(
            name = NAME,
            dataset = DATASET,
            scope = Scope.GLOBAL,
            value = CustomAdhocImplementation.class, // <<<<<<<<< Here comes the customized Adhoc implementation.
            args = {"argument 0", "argument 1"},
            properties = {@Property(k = "key1", v = "value1"), @Property(k = "key2", v = "value2")}
    )
    private static class TestFixture {
    }

    /**
     * An implementation of {@link Adhoc.AdhocTemplateObject}.
     */
    @SuppressWarnings("unused")
    public static final class CustomAdhocImplementation extends AdhocTemplateObjectBase {
        public CustomAdhocImplementation() {
        }

        private CustomAdhocImplementation(Adhoc annotation) {
            super(annotation);
        }

        @Override
        public Adhoc.AdhocTemplateObject create(Adhoc annotation) {
            return new CustomAdhocImplementation(annotation);
        }
    }
}