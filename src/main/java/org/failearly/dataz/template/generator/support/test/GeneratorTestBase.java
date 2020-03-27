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

package org.failearly.dataz.template.generator.support.test;

import org.failearly.dataz.internal.template.support.test.message.basic.TemplateObjectErrorMessages;
import org.failearly.dataz.internal.template.support.test.message.generator.NoDevelopmentGeneratorErrorMessages;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.generator.Generator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.support.test.DevelopmentTemplateObjectTestBase;
import org.failearly.dataz.template.support.test.TemplateObjectTestBase;

import java.lang.annotation.Annotation;

/**
 * GeneratorTestBase is the base test support class for all {@link Generator} and {@link GeneratorFactoryBase} implementations.
 *
 * While you are developing a generator, you should use {@link DevelopmentLimitedGeneratorTestBase} or {@link DevelopmentUnlimitedGeneratorTestBase} instead of this base class. After
 * finish the development go back to this base class, which will drop all additional tests of
 * {@link DevelopmentTemplateObjectTestBase}.
 *
 * @see LimitedGeneratorTestBase
 * @see UnlimitedGeneratorTestBase
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class GeneratorTestBase<T, TOA extends Annotation, TOF extends TemplateObjectFactory, TO extends Generator<T>>
        extends TemplateObjectTestBase<TOA, TOF, TO> implements GeneratorTemplates {

    private final static TemplateObjectErrorMessages noDevelopmentGeneratorErrorMessages = new NoDevelopmentGeneratorErrorMessages();

    protected GeneratorTestBase() {
    }

    protected GeneratorTestBase(
            Class<TOA> templateObjectAnnotationClass,
            Class<TOF> templateObjectFactoryClass,
            Class<TO> templateObjectClass,
            Class<?> testFixtureClass
    ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, templateObjectClass, testFixtureClass);
    }

    @Override
    protected TemplateObjectErrorMessages getTemplateObjectErrorMessages() {
        return noDevelopmentGeneratorErrorMessages;
    }
}
