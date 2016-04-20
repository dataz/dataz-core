/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */

package org.failearly.dataset.internal.template;

import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObject;
import org.failearly.dataset.template.TemplateObjectFactory;
import org.failearly.dataset.test.MyTemplateObjectAnnotation;
import org.failearly.dataset.test.MyTemplateObjectFactory;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * TemplateObjectCreatorTest contains tests for {@link TemplateObjectCreator} .
 */
public final class TemplateObjectCreatorTest {

    private static final String TEMPLATE_OBJECT_1 = "unlimited";
    private static final String TEMPLATE_OBJECT_2 = "limited";
    private static final String DS1 = "DS1";
    private static final String DS2 = "DS2";

    private static final TemplateObjectFactory TEMPLATE_OBJECT_FACTORY = new MyTemplateObjectFactory();

    @Test
    public void template_object_creator__should_create_correct_template_object() throws Exception {
        // arrange / given
        final TemplateObjectCreator creatorAssociatedTo1stAnnotation = new TemplateObjectCreator(TEMPLATE_OBJECT_FACTORY, getAnnotation(0));
        final TemplateObjectCreator creatorAssociatedTo2ndAnnotation = new TemplateObjectCreator(TEMPLATE_OBJECT_FACTORY, getAnnotation(1));

        // act / when
        final TemplateObject templateObject1 = creatorAssociatedTo1stAnnotation.createTemplateObjectInstance();
        final TemplateObject templateObject2 = creatorAssociatedTo2ndAnnotation.createTemplateObjectInstance();

        // assert / then
        assertThat("Creator's DataSet?", creatorAssociatedTo1stAnnotation.getDataSetName(), is(DS1));
        assertThat("Creator's Scope?", creatorAssociatedTo1stAnnotation.hasScope(Scope.GLOBAL), is(true));
        assertThat("Template Object's DataSet?", templateObject1.dataset(), is(DS1));
        assertThat("Template Object's Name?", templateObject1.name(), is(TEMPLATE_OBJECT_1));
        assertThat("Template Object's Scope?", templateObject1.scope(), is(Scope.GLOBAL));

        // ... and
        assertThat("Creator's DataSet?", creatorAssociatedTo2ndAnnotation.getDataSetName(), is(DS2));
        assertThat("Creator's Scope?", creatorAssociatedTo2ndAnnotation.hasScope(Scope.LOCAL), is(true));
        assertThat("Template Object's DataSet?", templateObject2.dataset(), is(DS2));
        assertThat("Template Object's Name?", templateObject2.name(), is(TEMPLATE_OBJECT_2));
        assertThat("Template Object's Scope?", templateObject2.scope(), is(Scope.LOCAL));
    }

    private Annotation getAnnotation(int annotationNumber) {
        return AnyClass.class.getAnnotationsByType(MyTemplateObjectAnnotation.class)[annotationNumber];
    }


    @MyTemplateObjectAnnotation(name = TEMPLATE_OBJECT_1, dataset = DS1, scope = Scope.GLOBAL)
    @MyTemplateObjectAnnotation(name = TEMPLATE_OBJECT_2, dataset = DS2, scope = Scope.LOCAL)
    private final static class AnyClass {
    }
}