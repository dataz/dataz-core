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

package org.failearly.dataz.template.generator.support;

import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;

import java.lang.annotation.Annotation;

/**
 * LongRangeGenerator provides support for Long based range generators.
 */
public abstract class LongRangeGenerator extends RangeGeneratorBase<Long> {

    protected LongRangeGenerator(Annotation annotation, TemplateObjectAnnotationContext context, long from, long to, long step) {
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
    protected final boolean hasNextValue(Long curr) {
        return curr <= to;

    }

    @Override
    protected final Long nextValue(Long curr) {
        return curr + step;
    }
}
