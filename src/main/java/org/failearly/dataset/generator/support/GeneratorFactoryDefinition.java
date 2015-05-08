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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GeneratorFactoryDefinition is a meta annotation used for {@link org.failearly.dataset.generator.support.Generator} annotations.
 * <br><br>
 * Example:<br><br>
 * <pre>
 *     {@literal @Target}({ElementType.METHOD, ElementType.TYPE})
 *     {@literal @Retention}(RetentionPolicy.RUNTIME)
 *     <b>{@literal @GeneratorFactoryDefinition}(generatorFactory = MyGeneratorFactory.class)</b>
 *     {@literal @}{@link java.lang.annotation.Repeatable}(MyGenerator.MyGenerators.class)
 *     public {@literal @interface} MyGenerator {
 *         // mandatory element
 *         String name();
 *
 *         // mandatory element
 *         String dataset();
 *
 *         // additional elements omitted for brevity
 *         ...
 *
 *         // Necessary for Repeatable
 *         {@literal @Target}({ElementType.METHOD, ElementType.TYPE})
 *         {@literal @Retention}(RetentionPolicy.RUNTIME)
 *         {@literal @interface} MyGenerators {
 *         MyGenerator[] value();
 *         }
 *     }
 * </pre>
 *
 * @see org.failearly.dataset.generator.ConstantGenerator
 * @see org.failearly.dataset.generator.ListGenerator
 * @see org.failearly.dataset.generator.RangeGenerator
 * @see org.failearly.dataset.generator.LoopGenerator
 * @see org.failearly.dataset.generator.RandomBooleanGenerator
 * @see org.failearly.dataset.generator.RandomRangeGenerator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface GeneratorFactoryDefinition {
    /**
     * The responsible generator factory class.
     *
     * @return a {@link org.failearly.dataset.generator.support.GeneratorFactory} class which is associated to the generator annotation.
     */
    Class<? extends GeneratorFactory> generatorFactory();
}
