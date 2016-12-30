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

import org.failearly.dataz.internal.template.support.test.message.basic.TemplateObjectErrorMessages;
import org.failearly.dataz.internal.template.support.test.message.generator.NoDevelopmentGeneratorErrorMessages;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.generator.Generator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.support.test.DevelopmentTemplateObjectTestBase;
import org.failearly.dataz.template.support.test.TemplateObjectTestBase;

import java.lang.annotation.Annotation;

/**
 * GeneratorTestBase is the base test support class for all {@link Generator} and {@link GeneratorFactoryBase} implementations.
 *
 * While you are developing a generator, you should use {@link DevelopmentLimitedGeneratorTestBase} or {@link DevelopmentUnlimitedGeneratorTestBase} instead of this base class. After
 * finish the development go back to this base class, which will drop all additional tests of
 * {@link DevelopmentTemplateObjectTestBase}.
 *
 * @see LimitedGeneratorTestBase
 * @see UnlimitedGeneratorTestBase
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class GeneratorTestBase<T, TOA extends Annotation, TOF extends TemplateObjectFactory, TO extends Generator<T>>
        extends TemplateObjectTestBase<TOA, TOF, TO> implements GeneratorTemplates {

    private final static TemplateObjectErrorMessages noDevelopmentGeneratorErrorMessages = new NoDevelopmentGeneratorErrorMessages();

    protected GeneratorTestBase() {
    }

    protected GeneratorTestBase(
            Class<TOA> templateObjectAnnotationClass,
            Class<TOF> templateObjectFactoryClass,
            Class<TO> templateObjectClass,
            Class<?> testFixtureClass
    ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, templateObjectClass, testFixtureClass);
    }

    @Override
    protected TemplateObjectErrorMessages getTemplateObjectErrorMessages() {
        return noDevelopmentGeneratorErrorMessages;
    }
}
