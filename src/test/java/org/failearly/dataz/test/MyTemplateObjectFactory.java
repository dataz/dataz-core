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

package org.failearly.dataz.test;

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectBase;
import org.failearly.dataz.template.TemplateObjectFactoryBase;

/**
 * TemplateObjectAnnotationFactory is the factory for making from the annotation {@link MyTemplateObjectAnnotation} a
 * {@link MyTemplateObjectFactory.MyTemplateObject} object.
 */
public final class MyTemplateObjectFactory extends TemplateObjectFactoryBase<MyTemplateObjectAnnotation> {
    public MyTemplateObjectFactory() {
        super(MyTemplateObjectAnnotation.class);
    }

    @Override
    protected TemplateObject doCreate(MyTemplateObjectAnnotation annotation) {
        return new MyTemplateObject(annotation);
    }

    @Override
    protected String doResolveDataSetName(MyTemplateObjectAnnotation annotation) {
        return annotation.dataset();
    }

    @Override
    protected Scope doResolveScope(MyTemplateObjectAnnotation annotation) {
        return annotation.scope();
    }

    public final class MyTemplateObject extends TemplateObjectBase {

        private final String description;

        public MyTemplateObject(MyTemplateObjectAnnotation annotation) {
            super(annotation);
            this.description = annotation.description();
        }

        public String getDescription() {
            return description;
        }


        @Override
        public String toString() {
            final MyTemplateObjectAnnotation annotation=getAnnotation(MyTemplateObjectAnnotation.class);
            return "@"+annotation.annotationType().getName()
                    +"(description="+annotation.description()
                    +", dataz="+annotation.dataset()
                    +", name="+annotation.name()
                    +")";
        }
    }
}
