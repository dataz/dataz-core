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

package org.failearly.dataz.template;

import java.lang.annotation.*;
import java.util.Set;

/**
 * TemplateObjectFactory creates from a Template Object Annotation a {@link TemplateObject}. The class object of any implementation must be assigned
 * to the associated annotation by using the meta annotation {@link Definition#value()}.
 * <br><br>
 * Remark: Please extend  {@link TemplateObjectFactoryBase} instead of implementing this interface!!
 * <br><br>
 * The Template Object Annotation:<br><br>
 * <pre>
 *    {@literal @}Target({ElementType.METHOD, ElementType.TYPE})
 *    {@literal @}Retention(RetentionPolicy.RUNTIME)
 *    {@literal @}Definition(factory = <b>MyTemplateObjectFactory.class</b>) // Assign the factory
 *     public @interface MyTemplateObjectAnnotation {
 *        // Mandatory attributes
 *        String name();
 *        String dataz();
 *
 *        // Other attributes
 *        // ...
 *     }
 * </pre>
 * <br><br>
 * The appropriate Template Object Factory implementation:<br><br>
 * <pre>
 *      public final class MyTemplateObjectFactory extends TemplateObjectFactoryBase{@literal <}MyTemplateObjectAnnotation{@literal >} {
 *           public MyTemplateObjectFactory() {
 *               super(MyTemplateObjectAnnotation.class);
 *           }
 *
 *          {@literal @}Override
 *           protected TemplateObject doCreate(MyTemplateObjectAnnotation annotation) {
 *              // Creates <i>MyTemplateObject</i> from the template object annotation!!!
 *              return new MyTemplateObject(annotation);
 *           }
 *
 *          {@literal @}Override
 *           protected String doResolveDataSetNames(MyTemplateObjectAnnotation annotation) {
 *              return annotation.dataz();
 *           }
 *
 *          // The implementation of {@link TemplateObject}!!!
 *          public final class MyTemplateObject extends TemplateObjectBase {
 *              public MyTemplateObject(MyTemplateObjectAnnotation annotation) {
 *                  super(annotation, annotation.dataz(), annotation.name() );
 *              }
 *          }
 *      }
 * </pre>
 *
 * @see Definition#value()
 * @see TemplateObject
 */
public interface TemplateObjectFactory {
    /**
     * Create an instance of the template object using the annotation.
     *
     * @param annotation the annotation instance.
     *
     * @return a new instance of template object.
     */
    TemplateObject create(Annotation annotation);

    /**
     * Resolves the name of the template object annotation.
     *
     * @param annotation the annotation
     *
     * @return the name of the template object annotation.
     */
    String resolveName(Annotation annotation);

    /**
     * Resolves the associated datasets of the template object annotation.
     *
     * @param annotation the annotation
     *
     * @return the data set name of the template object annotation.
     */
    Set<String> resolveDataSetNames(Annotation annotation);

    /**
     * Resolves the scope from the template object annotation.
     *
     * @param annotation the annotation
     *
     * @return the {@link Scope} of the template object.
     */
    Scope resolveScope(Annotation annotation);

    @SuppressWarnings("unused")
    void __extend_TemplateObjectFactoryBase__instead_of_implementing_TemplateObjectFactory();


    /**
     * Definition is a meta annotation used for {@link TemplateObject} annotations (TOA).
     * <br><br>
     * Example:<br><br>
     * <pre>
     *     {@literal @Target}({ElementType.METHOD, ElementType.TYPE})
     *     {@literal @Retention}(RetentionPolicy.RUNTIME)
     *     <b>{@literal @Definition}(factory = MyGeneratorFactory.class)</b>
     *     {@literal @}{@link java.lang.annotation.Repeatable}(MyTemplateObjectAnnotation.MyTemplateObjectAnnotations.class)
     *     public {@literal @interface} MyTemplateObjectAnnotation {
     *         // mandatory attribute
     *         String name();
     *
     *         // mandatory attribute
     *         String dataz();
     *
     *         // additional elements omitted for brevity
     *         ...
     *
     *         // Necessary for Repeatable
     *         {@literal @Target}({ElementType.METHOD, ElementType.TYPE})
     *         {@literal @Retention}(RetentionPolicy.RUNTIME)
     *         {@literal @interface} List {
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
    @interface Definition {
        /**
         * The responsible generator factory class.
         *
         * @return a {@link TemplateObjectFactory} class which is associated to the template object annotation.
         */
        Class<? extends TemplateObjectFactory> value();
    }
}
