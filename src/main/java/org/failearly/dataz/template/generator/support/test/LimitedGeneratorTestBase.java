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

package org.failearly.dataz.template.generator.support.test;

import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.generator.Generator;

import java.lang.annotation.Annotation;

/**
 * LimitedGeneratorTestBase is the no development version of {@link DevelopmentLimitedGeneratorTestBase}. If you use
 * {@link GeneratorTestBase} it will be also ok.
 *
 * @see GeneratorTestBase
 * @see DevelopmentLimitedGeneratorTestBase
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class LimitedGeneratorTestBase<T, TOA extends Annotation, TOF extends TemplateObjectFactory, TO extends Generator<T>>
        extends GeneratorTestBase<T, TOA, TOF, TO> {


    protected LimitedGeneratorTestBase() {
    }

    protected LimitedGeneratorTestBase(
            Class<TOA> templateObjectAnnotationClass,
            Class<TOF> templateObjectFactoryClass,
            Class<TO> templateObjectClass,
            Class<?> testFixtureClass
    ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, templateObjectClass, testFixtureClass);
    }
}
