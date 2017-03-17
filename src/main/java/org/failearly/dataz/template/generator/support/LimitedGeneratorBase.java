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

package org.failearly.dataz.template.generator.support;

import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;

import java.lang.annotation.Annotation;

/**
 * LimitedGeneratorBase - the base implementation for limited generators.
 */
public abstract class LimitedGeneratorBase<T> extends GeneratorBase<T> implements LimitedGenerator<T> {

    /**
     * Decorate the other template object (here a generator)
     * @param other the template object to be decorated
     */
    protected LimitedGeneratorBase(TemplateObject other) {
        super(other);
    }

    /**
     * The standard constructor.
     * @param annotation your annotation
     * @param context the context object
     */
    protected LimitedGeneratorBase(Annotation annotation, TemplateObjectAnnotationContext context) {
        super(annotation, context);
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
