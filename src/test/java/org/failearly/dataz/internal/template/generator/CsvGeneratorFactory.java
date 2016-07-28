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

package org.failearly.dataz.internal.template.generator;

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.generator.CsvGenerator;
import org.failearly.dataz.template.generator.Generator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;

/**
 * CsvGeneratorFactory creates a {@link Generator} from {@link CsvGenerator} annotation.
 */
public class CsvGeneratorFactory extends GeneratorFactoryBase</*TODO replace*/Object,CsvGenerator> {
    public CsvGeneratorFactory() {
        super(CsvGenerator.class);
    }

    @Override
    protected Generator doCreate(CsvGenerator annotation) {
        return null;
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
}
