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

package org.failearly.dataset.template;

import java.lang.annotation.Annotation;

/**
 * TemplateObjectFactory creates from a Template Object Annotation a {@link TemplateObject}. The class object of any implementation must be assigned
 * to the associated annotation by using the meta annotation {@link TemplateObjectFactoryDefinition#factory()}.
 * <br><br>
 * Remark: Please extend  {@link TemplateObjectFactoryBase} instead of implementing this interface!!
 * <br><br>
 * The Template Object Annotation:<br><br>
 * <pre>
 *    {@literal @}Target({ElementType.METHOD, ElementType.TYPE})
 *    {@literal @}Retention(RetentionPolicy.RUNTIME)
 *    {@literal @}TemplateObjectFactoryDefinition(factory = <b>MyTemplateObjectFactory.class</b>) // Assign the factory
 *     public @interface MyTemplateObjectAnnotation {
 *        // Mandatory attributes
 *        String name();
 *        String dataset();
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
 *           protected String doResolveDataSetName(MyTemplateObjectAnnotation annotation) {
 *              return annotation.dataset();
 *           }
 *
 *          // The implementation of {@link TemplateObject}!!!
 *          public final class MyTemplateObject extends TemplateObjectBase {
 *              public MyTemplateObject(MyTemplateObjectAnnotation annotation) {
 *                  super(annotation, annotation.dataset(), annotation.name() );
 *              }
 *          }
 *      }
 * </pre>
 *
 *
 * @see TemplateObjectFactoryDefinition#factory()
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
     * Resolves the data set name of the template object annotation.
     *
     * @param annotation the annotation
     * @return the data set name of the generator annotation.
     */
    String resolveDataSetName(Annotation annotation);

    /**
     * Resolves the scope from the template object annotation.
     * @param annotation the annotation
     * @return the {@link Scope} of the template object.
     */
    Scope resolveScope(Annotation annotation);

    @SuppressWarnings("unused")
    void __extend_TemplateObjectFactoryBase__instead_of_implementing_TemplateObjectFactory();

}
