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

package org.failearly.dataz.internal.template.generator;

import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;
import org.failearly.dataz.template.generator.ListGenerator;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * ListGeneratorFactory is responsible for ...
 */
public final class ListGeneratorFactory extends GeneratorFactoryBase<String,ListGenerator>{

    public ListGeneratorFactory() {
        super(ListGenerator.class);
    }

    @Override
    protected TemplateObject doCreate(ListGenerator annotation, TemplateObjectAnnotationContext context) {
        return doCreateGenerator(annotation, context, annotation.limit(), annotation.values().length);
    }

    @Override
    protected ListGeneratorImpl doCreateLimitedGenerator(ListGenerator generatorAnnotation, TemplateObjectAnnotationContext context, Integer limitValue) {
        return new ListGeneratorImpl(generatorAnnotation, context);
    }

    @Override
    protected String doResolveName(ListGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(ListGenerator annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(ListGenerator annotation) {
        return annotation.scope();
    }

    public static class ListGeneratorImpl extends LimitedGeneratorBase<String> {
        private final List<String> values;

        private ListGeneratorImpl(ListGenerator annotation, TemplateObjectAnnotationContext context) {
            super(annotation, context);
            this.values = Arrays.asList(annotation.values());
        }

        @Override
        public Iterator<String> createIterator() {
            return values.iterator();
        }
    }
}
