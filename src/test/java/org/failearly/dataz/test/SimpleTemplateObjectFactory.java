/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
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
