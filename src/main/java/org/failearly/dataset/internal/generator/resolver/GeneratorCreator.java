/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.internal.generator.resolver;

import org.failearly.dataset.generator.support.Generator;
import org.failearly.dataset.generator.support.GeneratorFactory;

import java.lang.annotation.Annotation;

/**
 * GeneratorCreator is for creating {@link org.failearly.dataset.generator.support.Generator}s based on
 * the {@link org.failearly.dataset.generator.support.GeneratorFactory} and the generator annotation.
 */
@SuppressWarnings("unchecked")
public final class GeneratorCreator {
    private final GeneratorFactory factory;
    private final Annotation annotation;

    GeneratorCreator(GeneratorFactory factory, Annotation annotation) {
        this.factory = factory;
        this.annotation = annotation;
    }

    public String dataSet() {
        return factory.resolveDataSetName(annotation);
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public String getDataSet() {
        return dataSet();
    }

    public Generator createGeneratorInstance() {
        return factory.create(annotation);
    }
}
