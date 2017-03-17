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

package org.failearly.dataz.template.encoder.support;

import org.failearly.dataz.template.TemplateObjectFactoryBase;

import java.lang.annotation.Annotation;

/**
 * EncoderFactoryBase is the base class for creating encoders.
 */
public abstract class EncoderFactoryBase<T extends Annotation> extends TemplateObjectFactoryBase<T>{
    protected EncoderFactoryBase(Class<T> annotationClass) {
        super(annotationClass);
    }
}
