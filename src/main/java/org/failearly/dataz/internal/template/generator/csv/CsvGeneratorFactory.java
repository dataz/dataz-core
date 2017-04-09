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

package org.failearly.dataz.internal.template.generator.csv;

import org.failearly.dataz.common.Tests;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.generator.csv.CsvGenerator;
import org.failearly.dataz.template.generator.csv.CsvRecord;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;

/**
 * CsvGeneratorFactory creates a {@link CsvGeneratorImpl} from {@link CsvGenerator}.
 */
@Tests("CsvGeneratorTest")
public class CsvGeneratorFactory extends GeneratorFactoryBase<CsvRecord, CsvGenerator> {
    public CsvGeneratorFactory() {
        super(CsvGenerator.class);
    }

    @Override
    protected String doResolveName(CsvGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(CsvGenerator annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(CsvGenerator annotation) {
        return annotation.scope();
    }

    @Override
    protected TemplateObject doCreate(CsvGenerator annotation, TemplateObjectAnnotationContext context) {
        return doCreateGenerator(annotation, context, annotation.limit());
    }

    @Override
    protected LimitedGeneratorBase<CsvRecord> doCreateLimitedGenerator(final CsvGenerator annotation, TemplateObjectAnnotationContext context, final Integer limitValue) {
        return new CsvGeneratorImpl(annotation, context);
    }
}
