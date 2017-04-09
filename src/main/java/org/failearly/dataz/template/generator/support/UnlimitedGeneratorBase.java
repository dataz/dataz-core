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
import org.failearly.dataz.template.generator.GeneratorConstants;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Random;

/**
 * UnlimitedGeneratorBase - the base implementation for unlimited generators.
 */
public abstract class UnlimitedGeneratorBase<T> extends GeneratorBase<T> implements UnlimitedGenerator<T> {

    /**
     * Decorate the other template object (here a generator)
     * @param other the template object to be decorated
     */
    protected UnlimitedGeneratorBase(TemplateObject other) {
        super(other);
    }

    /**
     * The standard constructor.
     * @param annotation your impl
     * @param context the context object
     */
    protected UnlimitedGeneratorBase(Annotation annotation, TemplateObjectAnnotationContext context) {
        super(annotation, context);
    }

    /**
     * Creates a random value generator.
     *
     * @param seed seed value or {@link GeneratorConstants#DEFAULT_SEED}.
     *
     * @return the random instance.
     */
    protected static Random random(int seed) {
        return new Random(seed);
    }

    @Override
    protected T doNext() {
        if( ! internalIterator().hasNext() ) {
            resetInternalIterator();
        }

        return setLastValue(internalIterator().next());
    }

    @Override
    public final Iterator<T> iterator() {
        throw new UnsupportedOperationException("Don't use iterator() for unlimited generators! Use next() instead.");
    }

    @Override
    public final void __extend_UnlimitedGeneratorBase__instead_of_implementing_UnlimitedGenerator() {
        throw new UnsupportedOperationException("__extend_UnlimitedGeneratorBase__instead_of_implementing_UnlimitedGenerator must not be called");
    }

    @Override
    public final void __do_not_implement_Generator__instead_extend_GeneratorBase() {
        throw new UnsupportedOperationException("__do_not_implement_Generator must not be called");

    }
}
