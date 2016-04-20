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

package org.failearly.dataset.template.engine;

import org.failearly.dataset.config.DataSetProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * TemplateEngineBase is the base class for template engines.
 */
public abstract class TemplateEngineBase implements TemplateEngine {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected TemplateEngineBase() {
    }

    protected final File createTempFile(String resourceName, String resourceSuffix, String resourcePath) throws IOException {
        final File targetFile = File.createTempFile(resourceName, resourceSuffix, DataSetProperties.createTempDir(resourcePath));
        LOGGER.debug("Create target file '{}'", targetFile);
        if (DataSetProperties.isDropTempFile()) {
            targetFile.deleteOnExit();
        } else {
            LOGGER.warn("The target file ({}) won't be deleted. Don't forget to remove the file.", targetFile);
        }
        return targetFile;
    }


}
