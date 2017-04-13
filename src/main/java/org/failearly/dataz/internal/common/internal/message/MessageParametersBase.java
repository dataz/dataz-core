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

package org.failearly.dataz.internal.common.internal.message;

import java.lang.annotation.Annotation;

/**
 * MessageTemplateBase is responsible for ...
 */
public abstract class MessageParametersBase<A extends Annotation> implements MessageParameters {
    private final Class<A> annotationClass;

    protected MessageParametersBase(Class<A> annotationClass) {
        this.annotationClass=annotationClass;
    }

    @Override
    public final MessageParameters create(Annotation annotation) {
        return doCreate(annotationClass.cast(annotation));
    }

    protected abstract MessageParameters doCreate(A annotation);
}
