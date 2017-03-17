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

package org.failearly.dataz.internal.template.simple;

import org.failearly.common.annotations.Tests;
import org.failearly.dataz.template.*;
import org.failearly.dataz.template.simple.Constant;

/**
 * ConstantGeneratorFactory is responsible for creating of implementation instances for {@link Constant}.
 */
@Tests("ConstantTest")
public final class ConstantFactory extends TemplateObjectFactoryBase<Constant> {

    public ConstantFactory() {
        super(Constant.class);
    }

    @Override
    protected TemplateObject doCreate(Constant annotation, TemplateObjectAnnotationContext context) {
        return new ConstantImpl(annotation, context);
    }

    @Override
    protected String doResolveName(Constant annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(Constant annotation) {
        return annotation.datasets();
    }


    @Override
    protected Scope doResolveScope(Constant annotation) {
        return annotation.scope();
    }

    // Must be public for Velocity!
    @Tests("ConstantTest")
    public static class ConstantImpl extends TemplateObjectBase {
        private final String value;

        ConstantImpl(Constant annotation, TemplateObjectAnnotationContext context) {
            super(annotation, context);
            this.value = annotation.value();
        }

        public String getValue() {
            return value;
        }


        @Override
        public String toString() {
            return getValue();
        }
    }
}
