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
import org.failearly.dataz.internal.template.support.test.message.generator.unlimited.DevelopmentUnlimitedGeneratorErrorMessages;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.generator.Generator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.support.test.DevelopmentTemplateObjectTestBase;

import java.lang.annotation.Annotation;

/**
 * DevelopmentLimitedGeneratorTestBase is the base test support class for all {@link Generator} and
 * {@link GeneratorFactoryBase} implementations. After finishing the development, you should
 * replace {@code DevelopmentLimitedGeneratorTestBase} with {@link GeneratorTestBase}.
 * <p>
 * <br><br>
 * Start with something like this:
 * <br><br>
 * <pre>
 *      public class MyGeneratorTest extends {@link DevelopmentUnlimitedGeneratorTestBase}{@literal
 * <Object,NullTemplateObjectAnnotation,NullGeneratorFactory>} {
 *          public MyGeneratorTest () {
 *              super(..
 *          }
 *      }
 * </pre>
 */
@SuppressWarnings("unused")
public abstract class DevelopmentUnlimitedGeneratorTestBase<T, TOA extends Annotation, TOF extends TemplateObjectFactory, TO extends Generator<T>>
    extends DevelopmentTemplateObjectTestBase<TOA, TOF, TO> implements GeneratorTemplates {

    protected DevelopmentUnlimitedGeneratorTestBase() {
    }

    protected DevelopmentUnlimitedGeneratorTestBase(
            Class<TOA> templateObjectAnnotationClass,
            Class<TOF> templateObjectFactoryClass,
            Class<TO> templateObjectClass,
            Class<?> testFixtureClass
    ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, templateObjectClass, testFixtureClass);
    }

    @Override
    protected TemplateObjectErrorMessages getTemplateObjectErrorMessages() {
        return new DevelopmentUnlimitedGeneratorErrorMessages();
    }
}
