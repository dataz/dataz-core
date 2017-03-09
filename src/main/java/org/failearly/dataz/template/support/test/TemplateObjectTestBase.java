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

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.failearly.common.message.Message;
import org.failearly.dataz.internal.template.support.test.message.basic.NoDevelopmentTemplateObjectErrorMessages;
import org.failearly.dataz.internal.template.support.test.message.basic.TemplateObjectErrorMessages;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.lang.annotation.Annotation;

import static org.failearly.dataz.template.TemplateObjectAnnotationContext.createAnnotationContext;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * TemplateObjectTestBase is is a test support class for creating {@link TemplateObject} annotations and the
 * factory class {@link TemplateObjectFactory}.
 * <br><br>
 * Remark: If you createTransactionContext developing use {@link DevelopmentTemplateObjectTestBase} instead of this base class.
 *
 * @param <TOA> the template object annotation
 * @param <TOF> the template object factory
 * @see DevelopmentTemplateObjectTestBase
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class TemplateObjectTestBase<TOA extends Annotation, TOF extends TemplateObjectFactory, TO extends TemplateObject> {
    /**
     * The default template object name (TO).
     *
     * @see #template(String)
     */
    protected static final String DTON = "TO";

    /**
     * The placeholder for Template Object Name (TON). WIll be replaces by {@link #template(String, String)}
     */
    protected static final String PLACE_HOLDER_TON = "%ton%";
    /**
     *
     */
    protected static final String SIMPLE_TEMPLATE = PLACE_HOLDER_TON;


    protected static final int FIRST_ANNOTATION = 0;
    private static final String INNER_TEMPLATE = "%inner-template%";
    private static final String TEMPLATE_LOOP = "#foreach($i in [1 .. %loop-counter%])" + INNER_TEMPLATE + "#end";

    private static final TemplateObjectErrorMessages messageFactory = new NoDevelopmentTemplateObjectErrorMessages();

    private final Class<TOA> templateObjectAnnotationClass;
    private final Class<TOF> templateObjectFactoryClass;
    private final Class<?> testFixtureClass;
    private final Class<TO> templateObjectClass;

    private AnnotationHelper<TOA> annotationHelper;

    private final VelocityEngine engine = new VelocityEngine();


    @SuppressWarnings("unused")
    protected TemplateObjectTestBase() {
        this(null, null, null, null);
    }

    protected TemplateObjectTestBase(
            Class<TOA> templateObjectAnnotationClass,
            Class<TOF> templateObjectFactoryClass,
            Class<TO> templateObjectClass,
            Class<?> testFixtureClass) {
        this.templateObjectAnnotationClass = templateObjectAnnotationClass;
        this.templateObjectFactoryClass = templateObjectFactoryClass;
        this.testFixtureClass = testFixtureClass;
        this.templateObjectClass = templateObjectClass;
    }

    @Before
    public void setup() {
        checkInitialSteps();
        this.annotationHelper = AnnotationHelper
                .createAnnotationHelper(templateObjectAnnotationClass)
                .withFixtureClass(testFixtureClass);
        this.engine.init();
    }

    private void checkInitialSteps() {
        assertTemplateObjectAnnotationClass(this.templateObjectAnnotationClass);
        assertTemplateObjectFactoryClass(this.templateObjectFactoryClass);
        assertTemplateObjectClass(this.templateObjectClass);
        assertTestFixtureClass(this.testFixtureClass);
    }

    private void assertTemplateObjectAnnotationClass(Class<TOA> templateObjectAnnotationClass) {
        assertNotNull(templateObjectAnnotationClass, getTemplateObjectErrorMessages().missingTemplateObjectAnnotation(mb ->
                mb.withTestClass(this)
        ));
    }

    private void assertTemplateObjectFactoryClass(Class<TOF> templateObjectFactoryClass) {
        assertNotNull(templateObjectFactoryClass, getTemplateObjectErrorMessages().missingTemplateObjectFactory(mb ->
                mb.withTestClass(this)
                        .withTemplateObjectAnnotationClass(this.templateObjectAnnotationClass)));
    }

    private void assertTemplateObjectClass(Class<TO> templateObjectClass) {
        assertNotNull(templateObjectClass, getTemplateObjectErrorMessages().missingTemplateObject(mb ->
                mb.withTestClass(this)
                        .withTemplateObjectAnnotationClass(this.templateObjectAnnotationClass)
                        .withTemplateObjectFactoryClass(this.templateObjectFactoryClass)));
    }

    private void assertTestFixtureClass(Class<?> testFixtureClass) {
        assertNotNull(testFixtureClass, getTemplateObjectErrorMessages().missingTestFixture(mb ->
                mb.withTestClass(this)
                        .withTemplateObjectAnnotationClass(this.templateObjectAnnotationClass)
                        .withTemplateObjectFactoryClass(this.templateObjectFactoryClass)
                        .withTemplateObjectClass(this.templateObjectClass)
        ));
    }

    @Test
    public final void what_are_the_basic_steps_to_create_a_template_object() throws Exception {
        assertAtLeastOneAnnotation();
        assertInitialStepsDone();
    }

    private void assertInitialStepsDone() {
        if (isDevelopment()) {
            fail(getTemplateObjectErrorMessages().initialStepsDone(mb ->
                    mb.withTestClass(this)
                            .withTemplateObjectAnnotationClass(this.templateObjectAnnotationClass)
                            .withTemplateObjectFactoryClass(this.templateObjectFactoryClass)
                            .withTemplateObjectClass(this.templateObjectClass)
                            .withTestFixtureClass(this.testFixtureClass)
            ).toString());
        }
    }

    private boolean isDevelopment() {
        return this instanceof DevelopmentTemplateObjectTestBase;
    }

    private void assertAtLeastOneAnnotation() {
        assertCondition(hasAtLeastOneAnnotation(), getTemplateObjectErrorMessages().missingAnnotationOfTestFixture(mb ->
                mb.withTestClass(this)
                        .withTemplateObjectAnnotationClass(this.templateObjectAnnotationClass)
                        .withTemplateObjectFactoryClass(this.templateObjectFactoryClass)
                        .withTemplateObjectClass(this.templateObjectClass)
                        .withTestFixtureClass(this.testFixtureClass)
        ));
    }


    /**
     * Creates a velocity template by replacing {@value #PLACE_HOLDER_TON} with {@code templateObjectName}.
     * Used by first parameter of {@link #generate(String, TemplateObject)}.
     *
     * @param template           a template with {@value #PLACE_HOLDER_TON} ({@value #PLACE_HOLDER_TON} will be replaced)
     * @param templateObjectName the name of the template object (here generator)
     * @return the raw velocity template
     * @see #generate(String, TemplateObject)
     */
    protected static String template(String template, String templateObjectName) {
        return template.replace(PLACE_HOLDER_TON, "$" + templateObjectName);
    }

    /**
     * Creates a velocity template by replacing {@value #PLACE_HOLDER_TON} with {@link #DTON}.
     * Used by first parameter of {@link #generate(String, TemplateObject)}.
     *
     * @param template a template with {@value #PLACE_HOLDER_TON} ({@value #PLACE_HOLDER_TON} will be replaced)
     * @return the raw velocity template
     * @see #generate(String, TemplateObject)
     * @see #template(String, String)
     */
    protected static String template(String template) {
        return template(template, DTON);
    }

    /**
     * Creates a velocity template by replacing {@value #PLACE_HOLDER_TON} with {@code templateObjectName} and surrounds it with
     * "{@value #TEMPLATE_LOOP}".
     * Used by first parameter of {@link #generate(String, TemplateObject)}.
     *
     * @param template           a template with {@value #PLACE_HOLDER_TON} ({@value #PLACE_HOLDER_TON} will be replaced)
     * @param templateObjectName the name of the template object (here generator)
     * @param loopCounter        the number of repeating the inner part of the template
     * @return the raw velocity template
     * @see #generate(String, TemplateObject)
     * @see #template(String, String)
     */
    protected static String template(String template, String templateObjectName, int loopCounter) {
        return surroundWithLoop(template(template, templateObjectName), loopCounter);
    }

    /**
     * Creates a velocity template by replacing {@value #PLACE_HOLDER_TON} with {@link #DTON} and surrounds it with
     * "{@value #TEMPLATE_LOOP}".
     * Used by first parameter of {@link #generate(String, TemplateObject)}.
     *
     * @param template    a template with {@value #PLACE_HOLDER_TON} ({@value #PLACE_HOLDER_TON} will be replaced)
     * @param loopCounter the number of repeating the inner part of the template
     * @return the raw velocity template
     * @see #template(String, String, int)
     * @see #generate(String, TemplateObject)
     */
    protected static String template(String template, int loopCounter) {
        return template(template, DTON, loopCounter);
    }

    private static String surroundWithLoop(String innerTemplate, int loopCounter) {
        return TEMPLATE_LOOP.replace(INNER_TEMPLATE, innerTemplate).replace("%loop-counter%", "" + loopCounter);
    }

    private Context createVelocityContext(TemplateObject templateObject) {
        final Context context = new VelocityContext();
        context.put(templateObject.name(), templateObject);
        return context;
    }

    /**
     * Merge/generate the template object into the {@code template} using {@link org.apache.velocity.app.Velocity}.
     *
     * @param template       the template as String
     * @param templateObject the template object (created by {@link #createTemplateObjectFromAnnotation(int)}.
     * @return the merged/generated result.
     * @see #template(String, String)
     * @see #template(String, String, int)
     * @see #createTemplateObjectFromAnnotation(int)
     * @see #createTemplateObjectFromAnnotation(String)
     * @see #createTemplateObjectFromAnnotation(String, int)
     */
    protected final String generate(String template, TemplateObject templateObject) {
        final StringWriter stringWriter = new StringWriter();
        this.engine.evaluate(createVelocityContext(templateObject), stringWriter, "<dataZ>", template);
        return stringWriter.toString();
    }

    /**
     * Convenient method for {@code createTemplateObjectFromAnnotationIndex(0, templateObjectClass)}.
     *
     * @param templateObjectClass the target TO class.
     * @param <XTO> eXpected {@link TemplateObject} class
     *
     * @return first {@link TemplateObject} created from first annotation on your Test Fixture class.
     */
    protected final <XTO extends TemplateObject> XTO createTemplateObjectFromAnnotation(Class<XTO> templateObjectClass) {
        return createTemplateObjectFromAnnotation(FIRST_ANNOTATION, templateObjectClass);
    }

    /**
     * Create a {@link TemplateObject} from {@code annotationNumber} annotation of {@code testFixtureClass}, but the
     * target TemplateObject class is different. This could happen if the actually TO has been decorated by other
     * classes - i.e. {@link org.failearly.dataz.internal.template.generator.decorator.GeneratorDecorators}.
     *
     * @param annotationNumber    the number (or index) of the TOA on your {@code testFixtureClass}.
     * @param templateObjectClass the target TO class.
     * @param <XTO> eXpected {@link TemplateObject} class
     *
     * @return the template object
     * @see org.failearly.dataz.internal.template.generator.decorator.GeneratorDecorators
     */
    protected final <XTO extends TemplateObject> XTO createTemplateObjectFromAnnotation(int annotationNumber, Class<XTO> templateObjectClass) {
        return doCreateTemplateObjectFromAnnotation(resolveTestFixtureAnnotation(annotationNumber), templateObjectClass);
    }

    /**
     * Create a {@link TemplateObject} from {@code nthAnnotation} annotation of the named method of your
     * {@code testFixtureClass}.
     *
     * @param methodName          the method name of your {@code testFixtureClass}.
     * @param nthAnnotation       the nth (or index) of your TOA on your {@code methodName}.
     * @param templateObjectClass the target TO class.
     * @param <XTO> eXpected {@link TemplateObject} class
     *
     * @return the template object
     */
    protected final <XTO extends TemplateObject> XTO createTemplateObjectFromAnnotation(String methodName, int nthAnnotation, Class<XTO> templateObjectClass) {
        return doCreateTemplateObjectFromAnnotation(resolveTestFixtureAnnotation(methodName, nthAnnotation), templateObjectClass);
    }

    /**
     * Convenient method for {@code createTemplateObjectFromAnnotationIndex(methodName, 0)}.
     *
     * @param methodName the method name of your {@code testFixtureClass}.
     * @param templateObjectClass the target TO class.
     * @param <XTO> eXpected {@link TemplateObject} class
     *
     * @return the template object
     * @see #createTemplateObjectFromAnnotation(String, int)
     */
    protected final <XTO extends TemplateObject> XTO createTemplateObjectFromAnnotation(String methodName, Class<XTO> templateObjectClass) {
        return createTemplateObjectFromAnnotation(methodName, FIRST_ANNOTATION, templateObjectClass);
    }

    /**
     * Convenient method for {@code createTemplateObjectFromAnnotationIndex(0)}.
     *
     * @return first {@link TemplateObject} created from first annotation on your Test Fixture class.
     */
    protected final TO createTemplateObjectFromAnnotation() {
        return createTemplateObjectFromAnnotation(FIRST_ANNOTATION);
    }


    /**
     * Create a {@link TemplateObject} from {@code annotationNumber} annotation of {@code testFixtureClass}.
     *
     * @param annotationNumber the number (or index) of the TOA on your {@code testFixtureClass}.
     * @return the template object
     */
    protected final TO createTemplateObjectFromAnnotation(int annotationNumber) {
        return createTemplateObjectFromAnnotation(annotationNumber, this.templateObjectClass);
    }

    /**
     * Convenient method for {@code createTemplateObjectFromAnnotationIndex(methodName, 0)}.
     *
     * @param methodName the method name of your {@code testFixtureClass}.
     * @return the template object
     * @see #createTemplateObjectFromAnnotation(String, int)
     */
    protected final TO createTemplateObjectFromAnnotation(String methodName) {
        return createTemplateObjectFromAnnotation(methodName, FIRST_ANNOTATION);
    }

    /**
     * Create a {@link TemplateObject} from {@code nthAnnotation} annotation of the named method of your
     * {@code testFixtureClass}.
     *
     * @param methodName    the method name of your {@code testFixtureClass}.
     * @param nthAnnotation the nth (or index) of your TOA on your {@code methodName}.
     * @return the template object
     */
    protected final TO createTemplateObjectFromAnnotation(String methodName, int nthAnnotation) {
        return createTemplateObjectFromAnnotation(methodName, nthAnnotation, this.templateObjectClass);
    }

    static void assertNotNull(Object object, Message message) {
        assertCondition(object != null, message);
    }

    static void assertCondition(boolean condition, Message message) {
        if (!condition) {
            throw new TemplateObjectTestSetupException(message.generate());
        }
    }

    private <XTO extends TemplateObject> XTO doCreateTemplateObjectFromAnnotation(TOA annotation, Class<XTO> targetClass) {
        return toTemplateObject(createRawTemplateObjectFromAnnotation(annotation), targetClass);
    }

    private TemplateObject createRawTemplateObjectFromAnnotation(TOA annotation) {
        final TOF templateObjectFactory;
        try {
            templateObjectFactory = templateObjectFactoryClass.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not create an instance of " + templateObjectFactoryClass.getName(), e);
        }
        return templateObjectFactory.create(createAnnotationContext(this.testFixtureClass), annotation);
    }

    private <XTO extends TemplateObject> XTO toTemplateObject(TemplateObject templateObject, Class<XTO> targetClass) {
        assertThat("Expected TemplateObject class?", templateObject, is(instanceOf(targetClass)));
        return targetClass.cast(templateObject);
    }

    final boolean hasAtLeastOneAnnotation() {
        return annotationHelper.hasAnyAnnotations();
    }

    /**
     * Resolves the {@code nth} TOA from your TestFixture class.
     *
     * @param nth the nth TOA annotation (starting from 0).
     * @return the nth TOA
     */
    protected final TOA resolveTestFixtureAnnotation(int nth) {
        return annotationHelper.getAnnotationFromClass(nth);
    }

    protected final TOA resolveTestFixtureAnnotation(String methodName, int index) {
        return annotationHelper.getAnnotationFromMethod(methodName, index);
    }

    /**
     * @return the template object error messages.
     */
    protected TemplateObjectErrorMessages getTemplateObjectErrorMessages() {
        return messageFactory;
    }
}
