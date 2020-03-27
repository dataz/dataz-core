/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.template.support.test;

import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.internal.template.support.test.message.basic.DevelopmentTemplateObjectErrorMessages;
import org.failearly.dataz.internal.template.support.test.message.basic.TemplateObjectErrorMessages;
import org.failearly.dataz.template.generator.support.test.DevelopmentLimitedGeneratorTestBase;

import java.lang.annotation.Annotation;

/**
 * TemplateObjectTestBase is is a test support class for creating {@link TemplateObject} annotations and the
 * factory class {@link TemplateObjectFactory}. This class contains tests which must be true for all template object
 * annotations, the associated {@link TemplateObject} implementation and {@link TemplateObjectFactory}. Use this class
 * when you createTransactionContext developing a new template object (impl). After you have developed your impl, replace
 * this base class with {@link TemplateObjectTestBase}.
 *
 * @param <TOA>   the template object impl
 * @param <TOF> the template object factory
 *
 * @see TemplateObjectTestBase
 * @see DevelopmentLimitedGeneratorTestBase
 */
@SuppressWarnings("unused")
public abstract class DevelopmentTemplateObjectTestBase<TOA extends Annotation, TOF extends TemplateObjectFactory, TO extends TemplateObject>
    extends TemplateObjectTestBase<TOA, TOF, TO> {

    private static final TemplateObjectErrorMessages messageFactory=new DevelopmentTemplateObjectErrorMessages();

    /**
     * JUST for the first step.
     */
    protected DevelopmentTemplateObjectTestBase() {
        super();
    }

    protected DevelopmentTemplateObjectTestBase(
            Class<TOA> templateObjectAnnotationClass,
            Class<TOF> templateObjectFactoryClass,
            Class<TO> templateObjectClass,
            Class<?> testFixtureClass
    ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, templateObjectClass, testFixtureClass);
    }

    @Override
    protected TemplateObjectErrorMessages getTemplateObjectErrorMessages() {
        return messageFactory;
    }
}
