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

package org.failearly.dataz.template.encoder;

import org.failearly.dataz.template.TemplateObject;

/**
 * An Encoder encodes a given value to different format or type.
 *
 * @see org.failearly.dataz.template.encoder.support.EncoderBase
 */
public interface Encoder<T, R> extends TemplateObject {
    /**
     * Encodes given {@code value} to type {@code R}.
     * @param value a value of type T.
     * @return a value of type R.
     * @throws Exception exception of the encoder provider.
     */
    R encode(T value) throws Exception;

    @SuppressWarnings("unused")
    void __extend_EncoderBase__instead_of_implementing_Encoder();

}
