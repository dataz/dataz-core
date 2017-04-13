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

package org.failearly.dataz.template.generator.support;

import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;

import java.lang.annotation.Annotation;

/**
 * LimitedGeneratorBase - the base implementation for limited generators.
 */
public abstract class LimitedGeneratorBase<T> extends GeneratorBase<T> implements LimitedGenerator<T> {

    /**
     * Decorate the other template object (here a generator)
     * @param other the template object to be decorated
     */
    protected LimitedGeneratorBase(TemplateObject other) {
        super(other);
    }

    /**
     * The standard constructor.
     * @param annotation your impl
     * @param context the context object
     */
    protected LimitedGeneratorBase(Annotation annotation, TemplateObjectAnnotationContext context) {
        super(annotation, context);
    }

    @Override
    public final void __extend_LimitedGeneratorBase__instead_of_implementing_LimitedGenerator() {
        throw new UnsupportedOperationException("__extend_LimitedGeneratorBase__instead_of_implementing_LimitedGenerator must not be called");
    }

    @Override
    public final void __do_not_implement_Generator__instead_extend_GeneratorBase() {
        throw new UnsupportedOperationException("__do_not_implement_Generator must not be called");
    }
}
