/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.template.generator.support.test;

import org.failearly.dataset.template.generator.Generator;
import org.failearly.dataset.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataset.template.support.test.DevelopmentTemplateObjectTestBase;
import org.failearly.dataset.template.support.test.TemplateObjectTestBase;

import java.lang.annotation.Annotation;

/**
 * GeneratorTestBase is the base test support class for all {@link Generator} and {@link GeneratorFactoryBase} implementations.
 * When you start developing, you should use {@link DevelopmentGeneratorTestBase} instead of this base class. After
 * finish the development go back to this base class, which will drop all additional tests of
 * {@link DevelopmentTemplateObjectTestBase}.
 * <br><br>
 * Example:
 * <br>
 * <pre>
 *      public class MyGeneratorTest extends {@link GeneratorTestBase}{@literal <Object,MyGenerator,MyGeneratorFactory>} {
 *          public MyGeneratorTest() {
 *              super(TestFixture.class, MyGenerator.class, MyGeneratorFactory.class);
 *          }
 *
 *          // Tests skipped for brevity
 *
 *         {@literal @}MyGenerator(name={@link #TEMPLATE_OBJECT_NAME}, other attributes)
 *          private static class TestFixture {}
 *      }
 * </pre>
 *
 * @see DevelopmentGeneratorTestBase
 * @see DevelopmentTemplateObjectTestBase
 */
public abstract class GeneratorTestBase<T, GA extends Annotation, GF extends GeneratorFactoryBase>
        extends TemplateObjectTestBase<GA, GF> implements GeneratorTemplates {

    protected GeneratorTestBase(Class<GA> generatorAnnotationClass, Class<GF> generatorFactoryClass, Class<?> testFixtureClass) {
        super(generatorAnnotationClass, generatorFactoryClass, testFixtureClass);
    }


    @SuppressWarnings("unchecked")
    protected Generator<T> createGenerator(int index) throws Exception {
        return (Generator<T>) super.createTemplateObjectFromAnnotationIndex(index);
    }
}
