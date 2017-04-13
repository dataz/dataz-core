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

package org.failearly.dataz.template.engine;

import org.failearly.dataz.internal.template.TemplateObjects;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * TemplateEngine provides the interface for any template engines.
 * <p>
 * Currently there is only one template engine supported: {@link org.apache.velocity.app.VelocityEngine}.
 */
public interface TemplateEngine {

    /**
     * Generate target resource from template ({@code templateStream}).
     *
     * @param templateStream the input stream (of the template).
     * @param templateResource the template resource (name).
     * @param templateObjects all template objects.
     *
     * @return the generated file (object).
     *
     * @throws IOException the template engine has an issue with the input stream.
     */
    File generate(InputStream templateStream, String templateResource, TemplateObjects templateObjects) throws IOException;
}
