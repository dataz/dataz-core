/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
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
