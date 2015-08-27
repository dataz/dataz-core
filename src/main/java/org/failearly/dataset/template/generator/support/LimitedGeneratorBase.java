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

package org.failearly.dataset.template.generator.support;

import org.failearly.dataset.template.Scope;

import java.lang.annotation.Annotation;

/**
 * LimitedGeneratorBase - the base implementation for limited generators.
 */
public abstract class LimitedGeneratorBase<T> extends GeneratorBase<T> implements LimitedGenerator<T> {

    protected LimitedGeneratorBase(String dataset, String name, Scope scope) {
        super(dataset, name, scope);
    }

    protected LimitedGeneratorBase(Annotation annotation, String dataset, String name, Scope scope) {
        super(annotation, dataset, name, scope);
    }

    @Override
    public final void __extend_LimitedGeneratorBase__instead_of_implementing_LimitedGenerator() {
        throw new UnsupportedOperationException("__extend_LimitedGeneratorBase__instead_of_implementing_LimitedGenerator must not be called");
    }

    @Override
    public final void __do_not_implement_Generator__instead_extend_GeneratorBase() {
        throw new UnsupportedOperationException("__do_not_implement_Generator must not be called");
    }
}
