/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */

package org.failearly.dataset.template.generator.support.test;

import org.failearly.dataset.template.generator.Generator;
import org.failearly.dataset.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.template.support.test.DevelopmentTemplateObjectTestBase;
import org.failearly.dataset.template.support.test.mb.DevelopmentMessageBuilders;
import org.failearly.dataset.template.support.test.mb.TemplateObjectMessageBuilder;
import org.failearly.common.test.mb.MessageBuilder;

import java.lang.annotation.Annotation;

/**
 * DevelopmentGeneratorTestBase is the base test support class for all {@link Generator} and
 * {@link GeneratorFactoryBase} implementations. After finishing the development, you should
 * replace {@code DevelopmentGeneratorTestBase} with {@link GeneratorTestBase}.
 * <p>
 * <br><br>
 * Start with something like this:
 * <br><br>
 * <pre>
 *      public class MyGeneratorTest extends {@link DevelopmentGeneratorTestBase}{@literal
 * <Object,NullTemplateObjectAnnotation,NullGeneratorFactory>} {
 *          public MyGeneratorTest () {
 *              super(..
 *          }
 *      }
 * </pre>
 */
@SuppressWarnings("unused")
public abstract class DevelopmentGeneratorTestBase<T, GA extends Annotation, GF extends GeneratorFactoryBase>
    extends DevelopmentTemplateObjectTestBase<GA, GF> implements GeneratorTemplates {

    protected DevelopmentGeneratorTestBase() {
        super(null, null, null);
    }

    protected DevelopmentGeneratorTestBase(Class<GA> generatorAnnotationClass, Class<GF> generatorFactoryClass, Class<?> testFixtureClass) {
        super(generatorAnnotationClass, generatorFactoryClass, testFixtureClass);
    }

    @Override
    protected String getTemplateObjectFactoryBaseClass() {
        return toTypeName(GeneratorFactoryBase.class);
    }

    protected String[] getTemplateObjectFactoryGenerics() {
        return toStringArray(
                "/*TODO replace*/" + toTypeName(Object.class),
                toTypeName(this.templateObjectAnnotationClass)
        );
    }

    @Override
    protected String[] getAdditionalGenerics() {
        return toTypeNames(Object.class);
    }

    @Override
    protected String getTemplateObjectName() {
        return "Generator";
    }

    @Override
    protected String getTemplateObjectType() {
        return Generator.class.getSimpleName();
    }

    @Override
    protected TemplateObjectMessageBuilder missingTemplateObjectMessage(MessageBuilder mb) {
        return DevelopmentMessageBuilders.missingGeneratorMessage(mb);
    }

    @SuppressWarnings("unchecked")
    protected Generator<T> createGenerator(int index) throws Exception {
        return (Generator<T>) super.createTemplateObjectFromAnnotationIndex(index);
    }
}
