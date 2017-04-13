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

package org.failearly.dataz.template.generator.support.test;

import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.generator.Generator;

import java.lang.annotation.Annotation;

/**
 * UnlimitedGeneratorTestBase is the no development version of {@link DevelopmentUnlimitedGeneratorTestBase}. If you use
 * {@link GeneratorTestBase} it will be also ok.
 *
 * @see GeneratorTestBase
 * @see DevelopmentUnlimitedGeneratorTestBase
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class UnlimitedGeneratorTestBase<T, TOA extends Annotation, TOF extends TemplateObjectFactory, TO extends Generator<T>>
        extends GeneratorTestBase<T, TOA, TOF, TO> {


    protected UnlimitedGeneratorTestBase() {
    }

    protected UnlimitedGeneratorTestBase(
            Class<TOA> templateObjectAnnotationClass,
            Class<TOF> templateObjectFactoryClass,
            Class<TO> templateObjectClass,
            Class<?> testFixtureClass
    ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, templateObjectClass, testFixtureClass);
    }
}
