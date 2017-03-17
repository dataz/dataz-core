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
import org.failearly.dataz.template.TemplateObjectBase;
import org.failearly.dataz.template.encoder.Encoder;

import java.lang.annotation.Annotation;

/**
 * EncoderBase should be the base class for all {@link Encoder} to prevent custom encoders from future interface
 * changes.
 */
public abstract class EncoderBase<T, R> extends TemplateObjectBase implements Encoder<T, R> {
    protected EncoderBase() {
    }

    /**
     * The standard constructor.
     * @param annotation your annotation
     * @param context the context object
     */
    protected EncoderBase(Annotation annotation, TemplateObjectAnnotationContext context) {
        super(annotation, context);
    }

    @Override
    public final void __extend_EncoderBase__instead_of_implementing_Encoder() {
        throw new UnsupportedOperationException(
            "__extend_EncoderBase__instead_of_implementing_Encoder must no be called"
        );
    }
}
