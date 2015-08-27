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
    protected void checkConditions() {
        checkTemplateObjectAnnotationClass(this.templateObjectAnnotationClass);
        checkTemplateObjectFactoryClass(this.templateObjectFactoryClass);
        checkTestFixtureClass(this.testFixtureClass);
    }


    // @formatter:off
    private TemplateObjectMessageBuilder initializeMessageBuilder(TemplateObjectMessageBuilder messageBuilder) {
        return messageBuilder
                    .withTestClass(this.getClass())
                    .withTemplateObjectAnnotationClass(this.templateObjectAnnotationClass)
                    .withTemplateObjectFactoryClass(this.templateObjectFactoryClass)
                    .withTemplateObjectFactoryBaseClassName(getTemplateObjectFactoryBaseClass())
                    .withTestFixtureClass(this.testFixtureClass)
                    .withTemplateObjectName(getTemplateObjectName())
                    .withTemplateObjectType(getTemplateObjectType())
                    .withTestClassAdditionalGenerics(getAdditionalGenerics());
    }

    protected String[] getAdditionalGenerics() {
        return new String[0];
    }

    private void checkTemplateObjectAnnotationClass(Class<A> templateObjectAnnotationClass) {
        notNull(templateObjectAnnotationClass, createLazyMessage((mb) -> {
            return initializeMessageBuilder(DevelopmentMessageBuilders.missingTemplateObjectAnnotation(mb))
                .withMissingVariable("templateObjectAnnotationClass");
        }));
    }

    private void checkTemplateObjectFactoryClass(Class<TOF> templateObjectFactoryClass) {
        notNull(templateObjectFactoryClass, createLazyMessage((mb) -> {
            return initializeMessageBuilder(DevelopmentMessageBuilders.missingTemplateObjectFactory(mb))
                .withMissingVariable("templateObjectFactoryClass");
        }));
    }
    private void checkTestFixtureClass(Class<?> testFixtureClass) {
        notNull(templateObjectFactoryClass, createLazyMessage((mb) -> {
            return initializeMessageBuilder(DevelopmentMessageBuilders.missingTestFixture(mb))
                .withMissingVariable("testFixtureClass");
        }));
        notNull(testFixtureClass, createLazyMessage((mb)-> {
            standardArguments(mb);
            messageError(mb, "Missing parameter 'testFixtureClass' or null.");
            actionCreateTestFixtureClass(mb);
            actionCompileAndRunTest(mb, 2);

            return mb;
        }));
    }

    private void messageError(MessageBuilder mb, String errorMessage) {
        mb.firstLine(errorMessage)
            .newline();
    }

    private void actionCompileAndRunTest(MessageBuilder mb, int actionNumber) {
        mb.newline()
            .line(actionNumber + ") Compile and rerun your test class!")
            .newlines(2);
    }

    private void actionCreateTestFixtureClass(MessageBuilder mb) {
        mb.line("1) Please create a test fixture class.")
            .newline()
            .lines(
                "A test fixture class is a helper class. This is the place to assign test data: instances of ",
                "your __ton__ Annotation for your tests. The test fixture class has no other purpose then to hold",
                "the __ton__ Annotations."
            )
            .newline()
            .exampleStart("A test fixture class and it's initial annotation:")
                .line("public class __testclass__ extends __testbase__<__additionaltypes____toa__,__tof__> {")
                .sub()
                    .line("public __testclass__() {")
                    .sub()
                        .line("super(")
                        .sub()
                            .line("__toa__.class,")
                            .line("__tof__.class,")
                            .line("__testfixture__.class")
                        .end()
                        .line(");")
                    .end()
                    .line("}")
                .end()
                .newlines(2)
                .sub()
                    .lines(
                        "// TEMPLATE_OBJECT_NAME is used by some of template(..) methods",
                        "@__toa__(name=TEMPLATE_OBJECT_NAME /* TODO: Add more attributes */)",
                        "private static class TestFixture {}"
                    )
                .end()
            .line("}")
            .exampleEnd();
    }


    protected MessageBuilder standardArguments(MessageBuilder mb) {
        mb.argument("testclass", resolveTestClass())
            .argument("testbase", resolveTestBaseClass())
            .argument("testfixture", resolveTestFixtureClass())
            .argument("ton", getTemplateObjectName())
            .argument("tot", getTemplateObjectType())
            .argument("toa", resolveTemplateObjectAnnotationName())
            .argument("tof", resolveTemplateObjectFactory())
            .argument("tofb", getTemplateObjectFactoryBaseClass());
         return mb;
    }

    // @formatter:on

    protected String getTemplateObjectName() {
        return "Template Object";
    }

    protected String getTemplateObjectType() {
        return TemplateObject.class.getSimpleName();
    }

    /**
     * Should be overloaded by sub (development) class.
     *
     * @return the name of the Template Object Factory base class.
     */
    protected String getTemplateObjectFactoryBaseClass() {
        return TemplateObjectFactoryBase.class.getSimpleName();
    }

    private String resolveTestFixtureClass() {
        return this.testFixtureClass != null ? this.testFixtureClass.getSimpleName() : "TestFixture";
    }

    private String resolveTemplateObjectFactory() {
        return this.templateObjectFactoryClass != null ?
            this.templateObjectFactoryClass.getSimpleName() :
            this.resolveTemplateObjectAnnotationName() + "Factory";
    }

    private String resolveTestBaseClass() {
        return this.getClass().getSuperclass().getSimpleName();
    }

    private String resolveTestClass() {
        return this.getClass().getSimpleName();
    }

    protected final String resolveTemplateObjectAnnotationName() {
        return this.templateObjectAnnotationClass != null ? this.templateObjectAnnotationClass
            .getSimpleName() : this.getClass().getSimpleName().replace("Test", "");
    }

    protected static void checkCondition(boolean condition, MessageBuilders.LazyMessage message) {
        if (!condition) {
            throw new IllegalArgumentException(message.build());
        }
    }

    protected static void notNull(Object object, MessageBuilders.LazyMessage message) {
        if (object == null) {
            throw new NullPointerException(message.build());
        }
    }

    @Test
    public void aaa_template_object_factory__should_resolve_assigned_dataset_from_annotation() throws Exception {
        final TemplateObjectFactory templateObjectFactory=templateObjectFactoryClass.newInstance();

        assertThat("Resolved dataset?",
            templateObjectFactory.resolveDataSetName(defaultDeclaredAnnotation()),
            is(expectedDataSetName())
        );
    }

    @Test
    public void aaa_template_object_factory__should_resolve_assigned_scope_from_annotation() throws Exception {
        final TemplateObjectFactory templateObjectFactory=templateObjectFactoryClass.newInstance();

        assertThat("Resolved scope?",
            templateObjectFactory.resolveScope(defaultDeclaredAnnotation()),
            is(expectedScope())
        );
    }

    @Test
    public final void aaa_any_template_object__should_provide_a_name_element() throws Exception {
        final TemplateObject templateObject=defaultTemplateObject();
        assertThat("Name?", templateObject.name(), is(expectedName()));
    }

    @Test
    public final void aaa_any_template_object__should_provide_a_dataset_element() throws Exception {
        final TemplateObject templateObject=defaultTemplateObject();
        assertThat("Dataset?", templateObject.dataset(), is(expectedDataSetName()));
    }

    @Test
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
