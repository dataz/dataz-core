/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */

package org.failearly.dataset.template.support.test;

import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObject;
import org.failearly.dataset.template.TemplateObjectFactory;

import java.lang.annotation.Annotation;

/**
 * NullTemplateObjectFactory is just a NOOP implementation for {@link TemplateObjectFactory}.
 *
 * @see TemplateObjectTestBase
 * @see NullTemplateObjectAnnotation
 */
@SuppressWarnings("unused")
public final class NullTemplateObjectFactory implements TemplateObjectFactory {

    private static final RuntimeException UNSUPPORTED_OPERATION_EXCEPTION = new UnsupportedOperationException("Provide your own implementation of TemplateObjectFactory");

    @Override
    public TemplateObject create(Annotation annotation) {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

    @Override
    public String resolveDataSetName(Annotation annotation) {
        throw UNSUPPORTED_OPERATION_EXCEPTION;

    }

    @Override
    public Scope resolveScope(Annotation annotation) {
        throw UNSUPPORTED_OPERATION_EXCEPTION;

    }

    @Override
    public void __extend_TemplateObjectFactoryBase__instead_of_implementing_TemplateObjectFactory() {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }
}
