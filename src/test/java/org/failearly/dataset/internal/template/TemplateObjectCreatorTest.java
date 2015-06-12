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

package org.failearly.dataset.internal.template;

import org.failearly.dataset.template.generator.ConstantGenerator;
import org.failearly.dataset.template.generator.Limit;
import org.failearly.dataset.internal.template.generator.standard.ConstantGeneratorFactory;
import org.failearly.dataset.template.TemplateObject;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * TemplateObjectCreatorTest contains tests for {@link TemplateObjectCreator} .
 */
public final class TemplateObjectCreatorTest {

    private static final ConstantGeneratorFactory CONSTANT_GENERATOR_FACTORY = new ConstantGeneratorFactory();

    @Test
    public void template_object_creator__should_create_correct_template_object() throws Exception {
        // arrange / given
        final TemplateObjectCreator creatorAssociatedTo1stAnnotation = new TemplateObjectCreator(CONSTANT_GENERATOR_FACTORY, getAnnotation(0));
        final TemplateObjectCreator creatorAssociatedTo2ndAnnotation = new TemplateObjectCreator(CONSTANT_GENERATOR_FACTORY, getAnnotation(1));

        // act / when
        final TemplateObject templateObject1 = creatorAssociatedTo1stAnnotation.createTemplateObjectInstance();
        final TemplateObject templateObject2 = creatorAssociatedTo2ndAnnotation.createTemplateObjectInstance();

        // assert / then
        assertThat("Creator's DataSet?", creatorAssociatedTo1stAnnotation.getDataSetName(), is("DS1"));
        assertThat("Template Object's DataSet?", templateObject1.dataset(), is("DS1"));
        assertThat("Template Object's Name?", templateObject1.name(), is("unlimited"));

        // ... and
        assertThat("Creator's DataSet?", creatorAssociatedTo2ndAnnotation.getDataSetName(), is("DS2"));
        assertThat("Template Object's DataSet?", templateObject2.dataset(), is("DS2"));
        assertThat("Template Object's Name?", templateObject2.name(), is("limited"));
    }

    private Annotation getAnnotation(int annotationNumber) {
        return AnyClass.class.getAnnotationsByType(ConstantGenerator.class)[annotationNumber];
    }


    @ConstantGenerator(name = "unlimited", dataset = "DS1", constant = "Not Important", limit = Limit.UNLIMITED)
    @ConstantGenerator(name = "limited", dataset = "DS2", constant = "Not Important", limit = Limit.LIMITED)
    private final static class AnyClass {
    }
}