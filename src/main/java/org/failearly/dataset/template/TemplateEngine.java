/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.template;

import java.io.IOException;
import java.io.InputStream;

/**
 * TemplateEngine provides the interface for any template engines.
 * <p>
 * Currently there is only one template engine supported: {@link org.apache.velocity.app.VelocityEngine}.
 */
public interface TemplateEngine {
    /**
     * Merge the input stream.
     *
     * @param inputStream the input stream (of the template).
     * @return the input stream of the generated file.
     *
     * @throws IOException the template engine has an issue with the input stream.
     */
    InputStream mergeToInputStream(InputStream inputStream) throws IOException;
}
