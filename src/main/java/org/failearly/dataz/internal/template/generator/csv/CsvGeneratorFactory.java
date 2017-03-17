/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.failearly.dataz.internal.template.generator.csv;

import org.failearly.common.annotations.Tests;
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
