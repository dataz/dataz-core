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

package org.failearly.dataset.template.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TemplateObjectFactoryDefinition is a meta annotation used for {@link TemplateObject} annotations.
 * <br><br>
 * Example:<br><br>
 * <pre>
 *     {@literal @Target}({ElementType.METHOD, ElementType.TYPE})
 *     {@literal @Retention}(RetentionPolicy.RUNTIME)
 *     <b>{@literal @TemplateObjectFactoryDefinition}(factory = MyGeneratorFactory.class)</b>
 *     {@literal @}{@link java.lang.annotation.Repeatable}(MyTemplateObjectAnnotation.MyTemplateObjectAnnotations.class)
 *     public {@literal @interface} MyTemplateObjectAnnotation {
 *         // mandatory attribute
 *         String name();
 *
 *         // mandatory attribute
 *         String dataset();
 *
 *         // additional elements omitted for brevity
 *         ...
 *
 *         // Necessary for Repeatable
 *         {@literal @Target}({ElementType.METHOD, ElementType.TYPE})
 *         {@literal @Retention}(RetentionPolicy.RUNTIME)
 *         {@literal @interface} MyGenerators {
 *             MyTemplateObjectAnnotation[] value();
 *         }
 *     }
 * </pre>
 *
 * @see TemplateObjectFactory
 * @see TemplateObjectFactoryBase
 * @see TemplateObject
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface TemplateObjectFactoryDefinition {
    /**
     * The responsible generator factory class.
     *
     * @return a {@link TemplateObjectFactory} class which is associated to the template object annotation.
     */
    Class<? extends TemplateObjectFactory> factory();
}
