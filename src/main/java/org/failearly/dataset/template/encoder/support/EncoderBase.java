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

package org.failearly.dataset.template.encoder.support;

import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObjectBase;
import org.failearly.dataset.template.encoder.Encoder;

import java.lang.annotation.Annotation;

/**
 * EncoderBase should be the base class for all {@link Encoder} to prevent custom encoders from future interface
 * changes.
 */
public abstract class EncoderBase<T, R> extends TemplateObjectBase implements Encoder<T, R> {
    protected EncoderBase() {
    }

    protected EncoderBase(Annotation annotation) {
        super(annotation);
    }

    protected EncoderBase(String dataset, String name, Scope scope) {
        super(dataset, name, scope);
    }

    @Override
    public final void __extend_EncoderBase__instead_of_implementing_Encoder() {
        throw new UnsupportedOperationException(
            "__extend_EncoderBase__instead_of_implementing_Encoder must no be called"
        );
    }
}
