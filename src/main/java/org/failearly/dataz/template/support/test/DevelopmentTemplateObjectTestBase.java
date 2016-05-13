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

package org.failearly.dataz.template.support.test;

import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.support.test.message.DevelopmentTemplateObjectMessageFactory;
import org.failearly.dataz.template.support.test.message.TemplateObjectMessageFactory;

import java.lang.annotation.Annotation;

/**
 * TemplateObjectTestBase is is a test support class for creating {@link TemplateObject} annotations and the
 * factory class {@link TemplateObjectFactory}. This class contains tests which must be true for all template object
 * annotations, the associated {@link TemplateObject} implementation and {@link TemplateObjectFactory}. Use this class
 * when you start developing a new template object (annotation). After you have developed your annotation, replace
 * this base class with {@link TemplateObjectTestBase}.
 *
 * @param <TOA>   the template object annotation
 * @param <TOF> the template object factory
 *
 * @see TemplateObjectTestBase
 * @see org.failearly.dataz.template.generator.support.test.DevelopmentGeneratorTestBase
 */
@SuppressWarnings("unused")
public abstract class DevelopmentTemplateObjectTestBase<TOA extends Annotation, TOF extends TemplateObjectFactory, TO extends TemplateObject>
    extends TemplateObjectTestBase<TOA, TOF, TO> {

    private static final TemplateObjectMessageFactory messageFactory=new DevelopmentTemplateObjectMessageFactory();

    /**
     * JUST for the first step.
     */
    protected DevelopmentTemplateObjectTestBase() {
        super(null, null, null, null);
    }

    protected DevelopmentTemplateObjectTestBase(
            Class<TOA> templateObjectAnnotationClass,
            Class<TOF> templateObjectFactoryClass,
            Class<TO> templateObjectClass,
            Class<?> testFixtureClass
    ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, templateObjectClass, testFixtureClass);
    }

    @Override
    protected TemplateObjectMessageFactory getTemplateObjectMessageFactory() {
        return messageFactory;
    }
}
