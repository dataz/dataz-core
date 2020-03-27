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

package org.failearly.dataz.template.encoder.support.test;

import org.failearly.dataz.template.encoder.Encoder;
import org.failearly.dataz.template.encoder.support.EncoderFactoryBase;
import org.failearly.dataz.template.support.test.TemplateObjectTestBase;

import java.lang.annotation.Annotation;

/**
 * EncoderTestBase is responsible for ...
 */
@SuppressWarnings("unused")
public abstract class EncoderTestBase<T, R, TOA extends Annotation, TOF extends EncoderFactoryBase, TO extends Encoder<T, R>>
    extends TemplateObjectTestBase<TOA, TOF, TO> {

    protected EncoderTestBase() {
    }

    protected EncoderTestBase(
            Class<TOA> templateObjectAnnotationClass,
            Class<TOF> templateObjectFactoryClass,
            Class<TO> templateObjectClass,
            Class<?> testFixtureClass
    ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, templateObjectClass, testFixtureClass);
    }
}
