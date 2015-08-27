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

import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObject;
import org.failearly.dataset.template.generator.support.GeneratorFactoryBase;

import java.lang.annotation.Annotation;

/**
 * NullGeneratorFactory is responsible for ...
 */
@SuppressWarnings("unused")
public final class NullGeneratorFactory<T, A extends Annotation> extends GeneratorFactoryBase<T, A> {
    private static final RuntimeException UNSUPPORTED_OPERATION_EXCEPTION=
            new UnsupportedOperationException("Provide your own implementation of GeneratorFactoryBase");

    @SuppressWarnings("unchecked")
    public NullGeneratorFactory() {
        super((Class<A>) Annotation.class);
    }

    @Override
    protected TemplateObject doCreate(A annotation) {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

    @Override
    protected String doResolveDataSetName(A annotation) {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

    @Override
    protected Scope doResolveScope(A annotation) {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }
}
