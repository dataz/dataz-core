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

package org.failearly.dataz.template.encoder.support;

import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.TemplateObjectBase;
import org.failearly.dataz.template.encoder.Encoder;

import java.lang.annotation.Annotation;

/**
 * EncoderBase should be the base class for all {@link Encoder} to prevent custom encoders from future interface
 * changes.
 */
public abstract class EncoderBase<T, R> extends TemplateObjectBase implements Encoder<T, R> {
    protected EncoderBase() {
    }

    protected EncoderBase(TemplateObjectAnnotationContext context, Annotation annotation) {
        super(context, annotation);
    }

    @Override
    public final void __extend_EncoderBase__instead_of_implementing_Encoder() {
        throw new UnsupportedOperationException(
            "__extend_EncoderBase__instead_of_implementing_Encoder must no be called"
        );
    }
}
