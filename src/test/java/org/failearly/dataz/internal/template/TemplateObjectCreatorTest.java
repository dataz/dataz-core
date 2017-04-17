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

package org.failearly.dataz.internal.template;

import org.failearly.dataz.common.test.annotations.Subject;
import org.failearly.dataz.template.*;
import org.failearly.dataz.test.SimpleTemplateObject;
import org.failearly.dataz.test.SimpleTemplateObjectFactory;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * TemplateObjectCreatorTest contains tests for {@link TemplateObjectCreator} .
 */
@Subject({TemplateObjectCreator.class, TemplateObjectBase.class, TemplateObjectFactoryBase.class, SimpleTemplateObject.class, SimpleTemplateObjectFactory.class})
public final class TemplateObjectCreatorTest {

    private static final TemplateObjectFactory TEMPLATE_OBJECT_FACTORY = new SimpleTemplateObjectFactory();
    private static final TemplateObjectAnnotationContext TEMPLATE_OBJECT_ANNOTATION_CONTEXT = TemplateObjectAnnotationContext.createAnnotationContext(AnyClass.class);

    private static final int TOA_WITH_DEFAULT_SETTINGS = 0;
    private static final int TOA_WITH_NON_DEFAULT_SETTINGS = 1;
    private static final int LOCAL_TOA = 1;
    private static final int GLOBAL_TOA = 2;
    private static final int TOA_WITH_ONE_DATASET = 1;
    private static final int TOA_WITH_MULTIPLE_DATASETS = 2;
    private static final int TOA_WITH_NONE_UNIQUE_DATASETS = 3;

    private static Annotation getAnnotation(int annotationNumber) {
        return AnyClass.class.getAnnotationsByType(SimpleTemplateObject.class)[annotationNumber];
    }

    private static Set<String> expectedDatasets(String... expected) {
        return new HashSet<>(Arrays.asList(expected));
    }


    @Test
    public void which_TOA_values_are_available_on_the_TemplateObjectCreator() throws Exception {
        // arrange / given
        final Annotation toa = getAnnotation(TOA_WITH_NON_DEFAULT_SETTINGS);

        // act / when
        final TemplateObjectCreator creator = new TemplateObjectCreator(TEMPLATE_OBJECT_FACTORY, toa, TEMPLATE_OBJECT_ANNOTATION_CONTEXT);

        // assert / then
        assertThat("TOA?", creator.getAnnotation(), is(toa));
        assertThat("TOA.datasets()?", creator.getDataSetNames(), is(expectedDatasets("ds-11")));
        assertThat("TOA.scope()?", creator.hasScope(Scope.LOCAL), is(true));
    }

    @Test
    public void how_to_create_a_TemplateObject_from_TemplateObjectCreator() throws Exception {
        // arrange / given
        final Annotation toa = getAnnotation(TOA_WITH_NON_DEFAULT_SETTINGS);

        // act / when
        final TemplateObjectCreator creator = new TemplateObjectCreator(TEMPLATE_OBJECT_FACTORY, toa, TEMPLATE_OBJECT_ANNOTATION_CONTEXT);
        final TemplateObject templateObject = creator.createTemplateObjectInstance();

        // assert / then
        assertThat(templateObject.getClass().getName(), is("org.failearly.dataz.test.SimpleTemplateObjectFactory$SimpleTemplateObjectImpl"));
        assertThat(templateObject.getContext().getAnnotatedOrDeclaringClass(), equalTo(AnyClass.class));
    }

    private static TemplateObject createTemplateObjectFromCreator(int annotationNumber) {
        final Annotation toa = getAnnotation(annotationNumber);
        final TemplateObjectCreator creator = new TemplateObjectCreator(TEMPLATE_OBJECT_FACTORY, toa, TEMPLATE_OBJECT_ANNOTATION_CONTEXT);

        return creator.createTemplateObjectInstance();
    }

    @Test
    public void what_are_the_default_settings_of_any_TOA() throws Exception {
        // act / when
        final TemplateObject templateObject = createTemplateObjectFromCreator(TOA_WITH_DEFAULT_SETTINGS);

        // assert / then
        assertThat("Template Object's name?", templateObject.name(), is("TOA-with-defaults"));
        assertThat("Template Object's Scope?", templateObject.scope(), is(Scope.LOCAL));
        assertThat("Template Object's Datasets?", templateObject.datasets(), is(Collections.emptySet()));
    }

    @Test
    public void how_to_create_a_TemplateObject_with_none_default_scope() throws Exception {
        // assert / then
        assertThat("Local scope?", createTemplateObjectFromCreator(LOCAL_TOA).scope(), is(Scope.LOCAL));
        assertThat("Global scope?", createTemplateObjectFromCreator(GLOBAL_TOA).scope(), is(Scope.GLOBAL));
    }

    @Test
    public void how_to_create_a_TemplateObject_with_non_default_datasets() throws Exception {
        assertThat(createTemplateObjectFromCreator(TOA_WITH_DEFAULT_SETTINGS).datasets(), is(expectedDatasets()));
        assertThat(createTemplateObjectFromCreator(TOA_WITH_ONE_DATASET).datasets(), is(expectedDatasets("ds-11")));
        assertThat(createTemplateObjectFromCreator(TOA_WITH_MULTIPLE_DATASETS).datasets(), is(expectedDatasets("ds-21", "ds-22")));
    }

    @Test
    public void what_happens_to_non_unique_datasets() throws Exception {
        // arrange / given
        final Annotation toa = getAnnotation(TOA_WITH_NONE_UNIQUE_DATASETS);

        // act / when
        final TemplateObjectCreator creator = new TemplateObjectCreator(TEMPLATE_OBJECT_FACTORY, toa, TEMPLATE_OBJECT_ANNOTATION_CONTEXT);
        final TemplateObject templateObject = creator.createTemplateObjectInstance();

        // assert / then
        assertThat("TOC.datasets?", creator.getDataSetNames(), is(expectedDatasets("not-unique","UNIQUE")));
        assertThat("TOA.datasets?", templateObject.datasets(), is(expectedDatasets("not-unique","UNIQUE")));
    }


    @SimpleTemplateObject(name = "TOA-with-defaults")
    @SimpleTemplateObject(name = "TO-1", datasets = { "ds-11"}, scope = Scope.LOCAL)
    @SimpleTemplateObject(name = "TO-2", datasets = { "ds-21", "ds-22" }, scope = Scope.GLOBAL)
    @SimpleTemplateObject(name = "any-name", datasets = { "not-unique", "UNIQUE", "not-unique", "not-unique" })
    private final static class AnyClass {
    }
}