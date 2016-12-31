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

package org.failearly.dataz.template.simple;

import org.failearly.common.test.ExceptionVerifier;
import org.failearly.common.test.annotations.Subject;
import org.failearly.dataz.common.Property;
import org.failearly.dataz.internal.template.simple.AdhocFactory;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.simple.support.AdhocTemplateObjectBase;
import org.failearly.dataz.template.support.test.TemplateObjectTestBase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * AdhocTest contains tests for {@link Adhoc} and {@link AdhocFactory}.
 */
@Subject({Adhoc.class, AdhocFactory.class, Adhoc.AdhocTemplateObject.class})
public class AdhocTest extends TemplateObjectTestBase<Adhoc, AdhocFactory, Adhoc.AdhocTemplateObject> {

    public AdhocTest() {
        super(
            Adhoc.class, AdhocFactory.class, Adhoc.AdhocTemplateObject.class, TestFixture.class);
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
        ExceptionVerifier.on(() -> templateObject.getProperties().getStringValue("unknown key"))
            .expect(IllegalArgumentException.class).verify();
    }

    private CustomAdhocImplementation createAdhocTemplateObject() throws Exception {
        return (CustomAdhocImplementation) createTemplateObjectFromAnnotation(0);
    }


    @Adhoc(
            name = DTON,
            value = CustomAdhocImplementation.class,
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

        private CustomAdhocImplementation(TemplateObjectAnnotationContext context, Adhoc annotation) {
            super(context, annotation);
        }

        @Override
        public Adhoc.AdhocTemplateObject create(TemplateObjectAnnotationContext context, Adhoc annotation) {
            return new CustomAdhocImplementation(context, annotation);
        }
    }
}