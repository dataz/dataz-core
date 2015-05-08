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
package org.failearly.dataset.generator.support;

import java.lang.annotation.Annotation;

/**
 * GeneratorFactory is responsible for creating a generator.
 *
 * @see GeneratorFactoryDefinition#generatorFactory()
 */
public interface GeneratorFactory<T, A extends Annotation> {
    /**
     * Create an instance of the generator using the annotation.
     *
     * @param annotation the annotation instance.
     *
     * @return an instance of current generator.
     */
    Generator<T> create(A annotation);

    /**
     * Resolves the data set name of the generator annotation.
     *
     * @param annotation the annotation;
     * @return the data set name of the generator annotation.
     */
    String resolveDataSetName(Annotation annotation);
}
