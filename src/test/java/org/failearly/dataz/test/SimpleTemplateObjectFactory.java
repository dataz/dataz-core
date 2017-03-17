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

import org.failearly.dataz.template.*;

/**
 * TemplateObjectAnnotationFactory is the factory for making from the annotation {@link SimpleTemplateObject} a
 * {@link SimpleTemplateObjectImpl} object.
 */
public final class SimpleTemplateObjectFactory extends TemplateObjectFactoryBase<SimpleTemplateObject> {
    public SimpleTemplateObjectFactory() {
        super(SimpleTemplateObject.class);
    }

    @Override
    protected TemplateObject doCreate(SimpleTemplateObject annotation, TemplateObjectAnnotationContext context) {
        return new SimpleTemplateObjectImpl(annotation, context);
    }

    @Override
    protected String doResolveName(SimpleTemplateObject annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(SimpleTemplateObject annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(SimpleTemplateObject annotation) {
        return annotation.scope();
    }

    public final class SimpleTemplateObjectImpl extends TemplateObjectBase {

        private final String description;

        private SimpleTemplateObjectImpl(SimpleTemplateObject annotation, TemplateObjectAnnotationContext context) {
            super(annotation, context);
            this.description = annotation.description();
        }

        public String getDescription() {
            return description;
        }


        @Override
        public String toString() {
            final SimpleTemplateObject annotation=getAnnotation(SimpleTemplateObject.class);
            return "@"+annotation.annotationType().getName()
                    +"(description="+annotation.description()
                    +", datasets="+toSet(annotation.datasets())
                    +", name="+annotation.name()
                    +")";
        }
    }
}
