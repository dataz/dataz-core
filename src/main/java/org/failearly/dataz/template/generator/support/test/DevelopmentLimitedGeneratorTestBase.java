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
import org.failearly.dataz.internal.template.support.test.message.generator.limited.DevelopmentLimitedGeneratorErrorMessages;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.generator.Generator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.support.test.DevelopmentTemplateObjectTestBase;

import java.lang.annotation.Annotation;

/**
 * DevelopmentLimitedGeneratorTestBase is the base test support class for all {@link Generator} and
 * {@link GeneratorFactoryBase} implementations. After finishing the development, you should
 * replace {@code DevelopmentLimitedGeneratorTestBase} with {@link GeneratorTestBase}.
 * <p>
 * <br><br>
 * Start with something like this:
 * <br><br>
 * <pre>
 *      public class MyGeneratorTest extends {@link DevelopmentLimitedGeneratorTestBase}{@literal
 * <Object,NullTemplateObjectAnnotation,NullGeneratorFactory>} {
 *          public MyGeneratorTest () {
 *              super(..
 *          }
 *      }
 * </pre>
 */
@SuppressWarnings("unused")
public abstract class DevelopmentLimitedGeneratorTestBase<T, TOA extends Annotation, TOF extends TemplateObjectFactory, TO extends Generator<T>>
    extends DevelopmentTemplateObjectTestBase<TOA, TOF, TO> implements GeneratorTemplates {

    protected DevelopmentLimitedGeneratorTestBase() {
    }

    protected DevelopmentLimitedGeneratorTestBase(
            Class<TOA> templateObjectAnnotationClass,
            Class<TOF> templateObjectFactoryClass,
            Class<TO> templateObjectClass,
            Class<?> testFixtureClass
    ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, templateObjectClass, testFixtureClass);
    }

    @Override
    protected TemplateObjectErrorMessages getTemplateObjectErrorMessages() {
        return new DevelopmentLimitedGeneratorErrorMessages();
    }
}
