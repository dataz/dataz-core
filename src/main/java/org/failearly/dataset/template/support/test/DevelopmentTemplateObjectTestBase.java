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

package org.failearly.dataset.template.support.test;

import org.failearly.dataset.template.*;
import org.failearly.dataset.template.support.test.mb.DevelopmentMessageBuilders;
import org.failearly.dataset.template.support.test.mb.TemplateObjectMessageBuilder;
import org.failearly.dataset.util.mb.MessageBuilder;
import org.failearly.dataset.util.mb.MessageBuilders;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.failearly.dataset.util.mb.MessageBuilders.createLazyMessage;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * TemplateObjectTestBase is is a test support class for creating {@link TemplateObject} annotations and the
 * factory class {@link TemplateObjectFactory}. This class contains tests which must be true for all template object
 * annotations, the associated {@link TemplateObject} implementation and {@link TemplateObjectFactory}. Use this class
 * when you start developing a new template object (annotation). After you have developed your annotation, replace
 * this base class with {@link TemplateObjectTestBase}.
 *
 * @param <A>   the template object annotation  (use {@link NullTemplateObjectAnnotation})
 * @param <TOF> the template object factory (use NullTemplateObjectFactory)
 *
 * @see TemplateObjectTestBase
 * @see org.failearly.dataset.template.generator.support.test.DevelopmentGeneratorTestBase
 */
@SuppressWarnings("unused")
public abstract class DevelopmentTemplateObjectTestBase<A extends Annotation, TOF extends TemplateObjectFactory>
    extends TemplateObjectTestBase<A, TOF> {
    private static final String ANNOTATION_ELEMENT_FOR_NAME="name";
    private static final String ANNOTATION_ELEMENT_FOR_DATASET="dataset";
    private static final String ANNOTATION_ELEMENT_FOR_SCOPE="scope";

    private static final int FIRST_ANNOTATION=0;

    /**
     * JUST for the first step.
     */
    protected DevelopmentTemplateObjectTestBase() {
        super(null, null, null);
    }

    protected DevelopmentTemplateObjectTestBase(
        Class<A> templateObjectAnnotationClass, Class<TOF> templateObjectFactoryClass, Class<?> testFixtureClass
    ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, testFixtureClass);
    }

    @Override
    protected void checkInitialSteps() {
        assertTemplateObjectAnnotationClass(this.templateObjectAnnotationClass);
        assertTemplateObjectFactoryClass(this.templateObjectFactoryClass);
        assertTestFixtureClass(this.testFixtureClass);
    }


    private void assertTemplateObjectAnnotationClass(Class<A> templateObjectAnnotationClass) {
        assertNotNull(templateObjectAnnotationClass, createLazyMessage((mb) -> {
            return initializeMessageBuilder(DevelopmentMessageBuilders.missingTemplateObjectAnnotation(mb))
                .withMissingVariable("templateObjectAnnotationClass");
        }));
    }

    private void assertTemplateObjectFactoryClass(Class<TOF> templateObjectFactoryClass) {
        assertNotNull(templateObjectFactoryClass, createLazyMessage((mb) -> {
            return initializeMessageBuilder(DevelopmentMessageBuilders.missingTemplateObjectFactory(mb))
                .withMissingVariable("templateObjectFactoryClass");
        }));
    }
    private void assertTestFixtureClass(Class<?> testFixtureClass) {
        assertNotNull(testFixtureClass, createLazyMessage((mb) -> {
            return initializeMessageBuilder(DevelopmentMessageBuilders.missingTestFixture(mb))
                .withMissingVariable("testFixtureClass");
        }));
    }
    // @formatter:on

    private static void assertNotNull(Object object, MessageBuilders.LazyMessage message) {
        assertCondition(object != null, message);
    }

    @Test
    public void any_template_object_factory__should_create_a_valid_template_object() throws Exception {
        final TemplateObjectFactory templateObjectFactory=templateObjectFactoryClass.newInstance();
        final TemplateObject templateObject=defaultTemplateObject();

        assertNotNull(templateObject, createLazyMessage((mb) -> initializeMessageBuilder(missingTemplateObjectMessage(mb))));
    }

    protected TemplateObjectMessageBuilder missingTemplateObjectMessage(MessageBuilder mb) {
        return DevelopmentMessageBuilders.missingTemplateObject(mb);
    }


    @Test @Ignore("To be remove")
    public void aaa_template_object_factory__should_resolve_assigned_dataset_from_annotation() throws Exception {
        final TemplateObjectFactory templateObjectFactory=templateObjectFactoryClass.newInstance();

        assertThat("Resolved dataset?",
            templateObjectFactory.resolveDataSetName(defaultDeclaredAnnotation()),
            is(expectedDataSetName())
        );
    }

    @Test @Ignore("To be remove")
    public void aaa_template_object_factory__should_resolve_assigned_scope_from_annotation() throws Exception {
        final TemplateObjectFactory templateObjectFactory=templateObjectFactoryClass.newInstance();

        assertThat("Resolved scope?",
            templateObjectFactory.resolveScope(defaultDeclaredAnnotation()),
            is(expectedScope())
        );
    }

    @Test @Ignore("To be remove")
    public final void aaa_any_template_object__should_provide_a_name_element() throws Exception {
        final TemplateObject templateObject=defaultTemplateObject();
        assertThat("Name?", templateObject.name(), is(expectedName()));
    }

    @Test @Ignore("To be remove")
    public final void aaa_any_template_object__should_provide_a_dataset_element() throws Exception {
        final TemplateObject templateObject=defaultTemplateObject();
        assertThat("Dataset?", templateObject.dataset(), is(expectedDataSetName()));
    }

    @Test @Ignore("To be remove")
    public final void aaa_any_template_object__should_provide_a_scope_element() throws Exception {
        final TemplateObject templateObject=defaultTemplateObject();
        assertThat("Scope?", templateObject.scope(), is(expectedScope()));
    }

    private String resolveTemplateObjectFactoryClassName() {
        return templateObjectAnnotationClass.getDeclaredAnnotation(TemplateObjectFactoryDefinition.class).factory().getName();
    }

    private TemplateObject defaultTemplateObject() throws Exception {
        return createTemplateObjectFromAnnotationIndex(FIRST_ANNOTATION);
    }

    private TemplateObject doCreateTemplateObjectFromAnnotation(A annotation) throws Exception {
        final TOF templateObjectFactory=templateObjectFactoryClass.newInstance();

        return templateObjectFactory.create(annotation);
    }

    private A defaultDeclaredAnnotation() {
        return getDeclaredAnnotation(FIRST_ANNOTATION);
    }

    private String expectedDataSetName() {
        return resolveAnnotationElementValue(ANNOTATION_ELEMENT_FOR_DATASET);
    }

    private String expectedName() {
        return resolveAnnotationElementValue(ANNOTATION_ELEMENT_FOR_NAME);
    }

    private Scope expectedScope() {
        return annotationHelper.resolveElementValue(FIRST_ANNOTATION, ANNOTATION_ELEMENT_FOR_SCOPE, Scope.class).getScopeValue();
    }

    private String resolveAnnotationElementValue(String element) {
        return annotationHelper.resolveElementValue(FIRST_ANNOTATION, element);
    }
}
