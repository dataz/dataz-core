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

package org.failearly.dataset.template.engine;

import org.failearly.dataset.internal.template.TemplateObjects;

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
