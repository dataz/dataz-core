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

import org.failearly.dataz.template.TemplateObjectAnnotationContext;

import java.lang.annotation.Annotation;

/**
 * StringEncoderBase is responsible for ...
 */
public abstract class StringEncoderBase extends EncoderBase<String, String> {
    protected StringEncoderBase() {
    }

    /**
     * The standard constructor.
     * @param annotation your impl
     * @param context the context object
     */
    protected StringEncoderBase(Annotation annotation, TemplateObjectAnnotationContext context) {
        super(annotation, context);
    }
}
