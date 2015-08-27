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

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.failearly.dataset.template.TemplateObject;
import org.failearly.dataset.template.TemplateObjectFactory;
import org.junit.Before;

import java.io.StringWriter;
import java.lang.annotation.Annotation;

/**
 * TemplateObjectTestBase is is a test support class for creating {@link TemplateObject} annotations and the
 * factory class {@link TemplateObjectFactory}.
 * <br><br>
 * Remark: If you start developing use {@link DevelopmentTemplateObjectTestBase} instead of this base class.
 *
 * @param <A>   the template object annotation
 * @param <TOF> the template object factory
 *
 * @see DevelopmentTemplateObjectTestBase
 */
public abstract class TemplateObjectTestBase<A extends Annotation, TOF extends TemplateObjectFactory> {
    protected static final String TEMPLATE_OBJECT_NAME="TO";
    protected static final String DATASET="DS";

    private static final String INNER_TEMPLATE="%inner-template%";
    private static final String TEMPLATE_LOOP="#foreach($i in [1 .. %loop-counter%])" + INNER_TEMPLATE + "#end";


    protected final Class<A> templateObjectAnnotationClass;
    protected final Class<TOF> templateObjectFactoryClass;
    protected final Class<?> testFixtureClass;

    protected AnnotationHelper<A> annotationHelper;

    private final VelocityEngine engine=new VelocityEngine();

    protected TemplateObjectTestBase(
        Class<A> templateObjectAnnotationClass, Class<TOF> templateObjectFactoryClass, Class<?> testFixtureClass
    ) {
        this.templateObjectAnnotationClass=templateObjectAnnotationClass;
        this.templateObjectFactoryClass=templateObjectFactoryClass;
        this.testFixtureClass=testFixtureClass;
    }

    @Before
    public void setup() {
        checkConditions();
        this.annotationHelper=AnnotationHelper
            .createAnnotationHelper(templateObjectAnnotationClass)
            .withFixtureClass(testFixtureClass);
        checkForAnnotations(testFixtureClass);
        this.engine.init();
    }

    protected void checkConditions() {
        // do nothing.
    }

    /**
     * Creates a velocity template by replacing %var% with {@code templateObjectName}.
     * Used by first parameter of {@link #generate(String, TemplateObject)}.
     *
     * @param template           a template with %var% (%var% will be replaced)
     * @param templateObjectName the name of the template object (here generator)
     *
     * @return the raw velocity template
     *
     * @see #generate(String, TemplateObject)
     */
    protected static String template(String template, String templateObjectName) {
        return template.replace("%var%", "$" + templateObjectName);
    }

    /**
     * Creates a velocity template by replacing %var% with {@link #TEMPLATE_OBJECT_NAME}.
     * Used by first parameter of {@link #generate(String, TemplateObject)}.
     *
     * @param template a template with %var% (%var% will be replaced)
     *
     * @return the raw velocity template
     *
     * @see #generate(String, TemplateObject)
     * @see #template(String, String)
     */
    protected static String template(String template) {
        return template(template, TEMPLATE_OBJECT_NAME);
    }

    /**
     * Creates a velocity template by replacing %var% with {@code templateObjectName} and surrounds it with
     * "{@value #TEMPLATE_LOOP}".
     * Used by first parameter of {@link #generate(String, TemplateObject)}.
     *
     * @param template           a template with %var% (%var% will be replaced)
     * @param templateObjectName the name of the template object (here generator)
     * @param loopCounter        the number of repeating the inner part of the template
     *
     * @return the raw velocity template
     *
     * @see #generate(String, TemplateObject)
     * @see #template(String, String)
     */
    protected static String template(String template, String templateObjectName, int loopCounter) {
        return surroundWithLoop(template(template, templateObjectName), loopCounter);
    }

    /**
     * Creates a velocity template by replacing %var% with {@link #TEMPLATE_OBJECT_NAME} and surrounds it with
     * "{@value #TEMPLATE_LOOP}".
     * Used by first parameter of {@link #generate(String, TemplateObject)}.
     *
     * @param template    a template with %var% (%var% will be replaced)
     * @param loopCounter the number of repeating the inner part of the template
     *
     * @return the raw velocity template
     *
     * @see #template(String, String, int)
     * @see #generate(String, TemplateObject)
     */
    protected static String template(String template, int loopCounter) {
        return template(template, TEMPLATE_OBJECT_NAME, loopCounter);
    }

    private static String surroundWithLoop(String innerTemplate, int loopCounter) {
        return TEMPLATE_LOOP.replace(INNER_TEMPLATE, innerTemplate).replace("%loop-counter%", "" + loopCounter);
    }

    private static Context createVelocityContext(TemplateObject templateObject) {
        final Context context=new VelocityContext();
        context.put(templateObject.name(), templateObject);
        return context;
    }

    private void checkForAnnotations(Class<?> testFixtureClass) {
        if (this.annotationHelper.hasNoAnnotations()) {
            throw new IllegalArgumentException(
                "The test fixture class " + testFixtureClass.getName()
                    + " has no annotations of type " + templateObjectAnnotationClass
            );
        }
    }

    /**
     * Merge/generate the template object into the {@code template} using {@link org.apache.velocity.app.Velocity}.
     *
     * @param template       the template as String
     * @param templateObject the template object
     *
     * @return the merged/generated result.
     *
     * @see #template(String, String)
     * @see #template(String, String, int)
     * @see #createTemplateObject()
     * @see #createTemplateObjectFromAnnotationIndex(int)
     */
    protected final String generate(String template, TemplateObject templateObject) {
        final StringWriter stringWriter=new StringWriter();
        this.engine.evaluate(createVelocityContext(templateObject), stringWriter, "<dataSet>", template);
        return stringWriter.toString();
    }

    /**
     * Convenient method for {@code createTemplateObjectFromAnnotationIndex(0)}.
     *
     * @return first {@link TemplateObject} created from first annotation.
     *
     * @throws Exception any Exception
     */
    protected final TemplateObject createTemplateObject() throws Exception {
        return createTemplateObjectFromAnnotationIndex(0);
    }


    /**
     * Create a {@link TemplateObject} from {@code annotationNumber} annotation of {@code testFixtureClass}.
     *
     * @param annotationNumber the number (or index) of the template object annotation on the {@code testFixtureClass}.
     *
     * @return the template object
     *
     * @throws Exception any Exception
     */
    protected final TemplateObject createTemplateObjectFromAnnotationIndex(int annotationNumber) throws Exception {
        return doCreateTemplateObjectFromAnnotation(getDeclaredAnnotation(annotationNumber));
    }

    private TemplateObject doCreateTemplateObjectFromAnnotation(A annotation) throws Exception {
        final TOF templateObjectFactory=templateObjectFactoryClass.newInstance();

        return templateObjectFactory.create(annotation);
    }

    protected final A getDeclaredAnnotation(int index) {
        return annotationHelper.getAnnotation(index);
    }
}
