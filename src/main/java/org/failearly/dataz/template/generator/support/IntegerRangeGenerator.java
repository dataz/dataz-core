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

import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;

import java.lang.annotation.Annotation;

/**
 * IntegerRangeGenerator is the base class for Integer based range generators.
 */
public abstract class IntegerRangeGenerator extends RangeGeneratorBase<Integer> {
    protected IntegerRangeGenerator(Annotation annotation, TemplateObjectAnnotationContext context, int from, int to, int step) {
        super(annotation, context, from, to, step);
    }

    @Override
    protected final void doInit() throws DataSetException {
        super.doInit();
        checkInvariant(from <= to, fromToInvariant());
        checkInvariant(step > 0, stepInvariant());
    }

    protected String stepInvariant() {
        return "step > 0";
    }

    protected String fromToInvariant() {
        return "from <= to";
    }

    @Override
    protected final boolean hasNextValue(Integer curr) {
        return curr <= to;

    }

    @Override
    protected final Integer nextValue(Integer curr) {
        return curr + step;
    }
}
